document.addEventListener("DOMContentLoaded", () => {
    cargarTablaPrecios();
    cargarRutasGPS();
});

// DESCARGA LA LISTA DE MATERIALES DE MYSQL
async function cargarTablaPrecios() {
    let resp = await fetch("../api/reciclaje/materiales");
    let materiales = await resp.json();
    
    let html = "<thead><tr class='text-light'><th>Material</th><th>Precio ($/kg)</th><th>Acción</th></tr></thead><tbody>";
    
    materiales.forEach(m => {
        html += `
            <tr>
                <td class="align-middle">${m.nombre}</td>
                <td><input type="number" class="form-control form-control-sm bg-dark text-white border-secondary" value="${m.precio}" id="val-${m.idProducto}"></td>
                <td><button class="btn btn-sm btn-success w-100" onclick="guardarPrecio(${m.idProducto}, '${m.nombre}')">💾 Guardar</button></td>
            </tr>`;
    });
    
    document.getElementById("tablaPrecios").innerHTML = html + "</tbody>";
}

// MANDA EL NUEVO PRECIO A LA BASE DE DATOS
async function guardarPrecio(id, nombre) {
    let nuevoPrecio = document.getElementById("val-" + id).value;
    let datos = { idProducto: id, precio: parseFloat(nuevoPrecio) };
    
    try {
        await fetch("../api/reciclaje/precio", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos)
        });
        alert(`¡Guardado! El precio de ${nombre} se actualizó a $${nuevoPrecio} en la Base de Datos.`);
    } catch (error) {
        alert("Error al guardar en la base de datos.");
    }
}

// DESCARGA LAS RUTAS PENDIENTES
async function cargarRutasGPS() {
    let listaRutas = document.getElementById("listaRutas");
    try {
        let resp = await fetch("../api/reciclaje/rutas");
        let data = await resp.json();
        
        if(!data || data.length === 0) {
            listaRutas.innerHTML = `<div class="alert alert-secondary text-center">✅ No hay recolecciones pendientes.</div>`;
            return;
        }
        
        let html = "";
        data.forEach(ruta => {
            html += `
            <div class="p-3 mb-3 bg-dark border border-warning rounded shadow-sm d-flex justify-content-between align-items-center">
                <div>
                    <h5 class="mb-1 text-warning fw-bold">Folio #${ruta.idVenta} - ${ruta.nombreUsuario}</h5>
                    <p class="mb-0 text-light fs-6">📦 ${ruta.cantidad} kg de ${ruta.material} <br>💵 A pagar: <strong>$${ruta.pago.toFixed(2)}</strong></p>
                </div>
                <div class="text-end">
                    <button class="btn btn-primary fw-bold me-2 mb-1" onclick="abrirMapa(${ruta.lat}, ${ruta.lon})">📍 MAPA</button>
                    <button class="btn btn-success fw-bold mb-1" onclick="marcarAtendida(${ruta.idVenta})">✅ ATENDIDA</button>
                </div>
            </div>`;
        });
        listaRutas.innerHTML = html;
        
    } catch(e) {
        listaRutas.innerHTML = `<div class="alert alert-danger text-center">❌ Error de conexión.</div>`;
    }
}

// BORRA LA SOLICITUD DEL MAPA (Cambia estatus a Finalizado)
async function marcarAtendida(idVenta) {
    if(confirm("¿Seguro que el recolector ya terminó esta ruta?")) {
        await fetch("../api/reciclaje/atender?idVenta=" + idVenta);
        cargarRutasGPS(); // Recargamos la lista limpia
    }
}

// ABRE GOOGLE MAPS SEGURO (Sin símbolos extraños)
function abrirMapa(lat, lon) {
    let url = "https://www.google.com/maps?q=" + lat + "," + lon;
    window.open(url, '_blank');
}