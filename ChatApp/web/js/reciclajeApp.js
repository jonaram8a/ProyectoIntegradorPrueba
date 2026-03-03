const URL_BASE = "../api/reciclaje";
let latitudReal = 0;
let longitudReal = 0;

document.addEventListener("DOMContentLoaded", () => {
    cargarMateriales();
    verificarSiTienePendientes();
});

// Llena la lista desplegable con los materiales de la BD
async function cargarMateriales() {
    let resp = await fetch(URL_BASE + "/materiales");
    let materiales = await resp.json();
    let select = document.getElementById("txtMaterial");
    
    select.innerHTML = ""; 
    materiales.forEach(m => {
        select.innerHTML += `<option value="${m.nombre}">${m.nombre} ($${m.precio}/kg)</option>`;
    });
}

// EVITA QUE HAGAN TRAMPA PIDIENDO DOBLE RECOLECTOR
async function verificarSiTienePendientes() {
    let userSession = JSON.parse(localStorage.getItem("usuario"));
    if(!userSession) return;
    
    try {
        let resp = await fetch(URL_BASE + "/pendiente?idUsuario=" + userSession.idUsuario);
        let data = await resp.json();
        
        if (data.tienePendiente) {
            Swal.fire({
                icon: 'info', 
                title: 'Recolector en camino 🚚',
                text: 'Ya tienes una solicitud de recolección pendiente. Espera a que la tienda pase por tus materiales para pedir otra.',
                allowOutsideClick: false
            });
            // Bloqueamos los botones para que no saturen el sistema
            document.querySelector("button[onclick='enviarDatosReciclaje();']").disabled = true;
            document.querySelector("button[onclick='obtenerUbicacion()']").disabled = true;
        }
    } catch(e) { console.error("Error al verificar pendientes"); }
}

async function enviarDatosReciclaje() {
    let material = document.getElementById("txtMaterial").value;
    let pesoInput = document.getElementById("txtPeso").value;

    if (!pesoInput || pesoInput <= 0) { 
        Swal.fire('Atención', 'Ingresa un peso válido.', 'warning'); 
        return; 
    }
    if (latitudReal === 0 || longitudReal === 0) { 
        Swal.fire('Falta GPS', 'Activa tu ubicación primero.', 'error'); 
        return; 
    }

    let userSession = JSON.parse(localStorage.getItem("usuario"));
    let datos = { 
        idUsuario: userSession ? userSession.idUsuario : 1,
        material: material, 
        cantidad: parseFloat(pesoInput),
        lat: latitudReal, 
        lon: longitudReal
    };

    try {
        let resp = await fetch(URL_BASE + "/guardar", {
            method: "POST", 
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos)
        });
        let data = await resp.json();
        
        document.getElementById("txtResultado").innerHTML = "$ " + data.pago.toFixed(2);
        
        Swal.fire('¡Enviada!', 'El recolector va en camino.', 'success').then(() => {
            location.reload(); // Recarga la página para bloquearla
        });
        
    } catch (error) { 
        Swal.fire('Error', 'Problema al conectar con el servidor.', 'error'); 
    }
}

function obtenerUbicacion() {
    let mapStatus = document.getElementById("mapStatus");
    mapStatus.innerHTML = "Buscando satélites... 🛰️"; 
    mapStatus.className = "fs-5 fw-bold text-warning mb-0"; 
    
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((pos) => {
            latitudReal = pos.coords.latitude; 
            longitudReal = pos.coords.longitude;
            
            mapStatus.innerHTML = "✅ Coordenadas Listas"; 
            mapStatus.className = "fs-5 fw-bold text-success mb-0";
            Swal.fire('GPS Activo', 'Tus coordenadas se enviarán con la solicitud.', 'success');
        }, (error) => {
            Swal.fire('Atención', 'Debes permitir el acceso a tu ubicación.', 'warning');
        });
    } else {
        Swal.fire('Error', 'Tu navegador no soporta geolocalización.', 'error');
    }
}