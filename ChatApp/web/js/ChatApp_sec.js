async function login()
{
    let nombreUsuario = document.getElementById("txtUsuario").value;
    let contrasenia = document.getElementById("txtPassword").value;

    let url = "api/usuario/login";

    let params = {
        nombre: nombreUsuario,
        contrasenia: contrasenia
    };

    let confServ = {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8" },
        body: new URLSearchParams(params)
    };

    let resp = await fetch(url, confServ);
    let data = await resp.json();

    console.log(data);

    if (data.error != null) {
        Swal.fire('Error', data.error, 'warning');
        return;
    } 
    else if (data.exception != null) {
        Swal.fire("Error en el servidor", data.exception, 'error');
        return;
    } 
    else {
        Swal.fire({
    icon: 'success',
    title: 'Bienvenido',
    text: 'Usuario autenticado correctamente 🎉',
    confirmButtonText: 'Continuar'
}).then(() => {
    window.location.href = "modulos/inicio.html";
});
    }
}

function validarAcceso(){
    login();
}

function logout(){
}