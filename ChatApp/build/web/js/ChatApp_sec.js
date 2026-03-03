async function validarAcceso() {
    let user = document.getElementById("txtUsuario").value;
    let pass = document.getElementById("txtPassword").value;

    let params = new URLSearchParams({ nombre: user, contrasenia: pass });
    let resp = await fetch("api/usuario/login", {
        method: "POST",
        body: params
    });
    let data = await resp.json();

    if (data.idUsuario) {
        localStorage.setItem("usuario", JSON.stringify(data));
        // 1: Vendedor (inicio.html), 2: Tienda (tienda.html)
        if (data.idRol === 2) window.location.href = "modulo/tienda.html";
        else window.location.href = "modulo/inicio.html";
    } else {
        alert("Credenciales incorrectas");
    }
}