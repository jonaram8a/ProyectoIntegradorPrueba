package org.integracode.chatapp.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.integracode.chatapp.model.Reciclaje;
import org.integracode.chatapp.model.Producto;
import org.integracode.chatapp.controller.ControllerVenta;

@Path("reciclaje")
public class RESTReciclaje {

    @POST
    @Path("guardar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarVenta(Reciclaje r) {
        try {
            ControllerVenta cv = new ControllerVenta();
            cv.registrarVenta(r);
            return Response.ok(new Gson().toJson(r)).build();
        } catch (Exception e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("rutas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerRutasPendientes() {
        try {
            List<Reciclaje> rutas = new ControllerVenta().obtenerRutas();
            return Response.ok(new Gson().toJson(rutas)).build();
        } catch (Exception e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("materiales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerMateriales() {
        try {
            List<Producto> lista = new ControllerVenta().obtenerProductos();
            return Response.ok(new Gson().toJson(lista)).build();
        } catch (Exception e) { 
            return Response.status(500).build(); 
        }
    }

    @POST
    @Path("precio")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarPrecio(Producto p) {
        try {
            new ControllerVenta().actualizarPrecio(p.getIdProducto(), p.getPrecio());
            return Response.ok("{\"mensaje\":\"OK\"}").build();
        } catch (Exception e) { 
            return Response.status(500).build(); 
        }
    }

    @GET
    @Path("atender")
    @Produces(MediaType.APPLICATION_JSON)
    public Response atenderVenta(@QueryParam("idVenta") int id) {
        try {
            new ControllerVenta().atenderVenta(id);
            return Response.ok("{\"mensaje\":\"OK\"}").build();
        } catch (Exception e) { 
            return Response.status(500).build(); 
        }
    }

    @GET
    @Path("pendiente")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verificarPendiente(@QueryParam("idUsuario") int id) {
        try {
            boolean tiene = new ControllerVenta().tienePendiente(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("tienePendiente", tiene);
            return Response.ok(new Gson().toJson(res)).build();
        } catch (Exception e) { 
            return Response.status(500).build(); 
        }
    }
}