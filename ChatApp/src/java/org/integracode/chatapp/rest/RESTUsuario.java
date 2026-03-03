package org.integracode.chatapp.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.*; 
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.integracode.chatapp.controller.ControllerUsuario;
import org.integracode.chatapp.model.Usuario;

@Path("usuario")
public class RESTUsuario {

    @Path("login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("nombre") String nombre, 
                          @FormParam("contrasenia") String contra) {
        try {
            ControllerUsuario cu = new ControllerUsuario();
            // CORRECCIÓN: Llamar a validate() en lugar de login()
            Usuario u = cu.validate(nombre, contra); 
            
            if (u == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .entity("{\"error\":\"Usuario o clave incorrectos\"}")
                               .build();
            }
            return Response.ok(new Gson().toJson(u)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"Error de servidor\"}")
                           .build();
        }
    }
}