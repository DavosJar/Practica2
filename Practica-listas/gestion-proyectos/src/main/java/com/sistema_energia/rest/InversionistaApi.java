package com.sistema_energia.rest;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sistema_energia.controller.dao.services.EventoCrudServices;
import com.sistema_energia.controller.dao.services.InversionistaServices;
import com.sistema_energia.controller.excepction.ListEmptyException;
import com.sistema_energia.eventos.TipoCrud;

@Path("/inversionista")
public class InversionistaApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAllInversionistas()throws ListEmptyException, Exception{
        HashMap<String, Object> map = new HashMap<>();
        InversionistaServices is = new InversionistaServices();
        try {
            map.put("msg", "OK");
            map.put("data", is.getAllInversionistas());
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "Error al obtener la lista de inversionistas: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }
    
    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap map) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        EventoCrudServices ec = new EventoCrudServices();
        
        try {
            
            if (map.get("nombre") == null || map.get("nombre").toString().isEmpty()) {
                throw new IllegalArgumentException("El nombre es obligatorio.");
            }
            if (map.get("registro") == null || map.get("registro").toString().isEmpty()) {
                throw new IllegalArgumentException("El registro es obligatorio.");
            }
            if (map.get("montoInvertido") == null) {
                throw new IllegalArgumentException("El monto invertido es obligatorio.");
            }

            InversionistaServices is = new InversionistaServices();
            is.getInversionista().setNombre(map.get("nombre").toString());
            is.getInversionista().setRegistro(map.get("registro").toString());
            is.getInversionista().setMontoInvertido(Double.parseDouble(map.get("montoInvertido").toString()));
            is.getInversionista().setSector(is.getSector(map.get("sector").toString()));
            is.getInversionista().setUbicacion(is.getProvincia(map.get("ubicacion").toString()));
            is.getInversionista().setProyectoId(Integer.valueOf(map.get("proyectoId").toString()));

            // Guardar inversionista
            is.save();
            res.put("MSG", "OK");
            res.put("DATA", is.toJson() + " Guardado con exito");
            ec.registrarEvento(TipoCrud.CREATE, "Se ha creado un nuevo inversionista: " + is.getInversionista().getNombre());
            return Response.ok(res).build();

        } catch (IllegalArgumentException e) {
            res.put("MSG", "ERROR");
            res.put("ERROR", e.getMessage());
            ec.registrarEvento(TipoCrud.CREATE, "Error al crear un nuevo inversionista: " + e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            e.printStackTrace();
            res.put("MSG", "ERROR");
            res.put("ERROR", e.toString());
            ec.registrarEvento(TipoCrud.CREATE, "Error al crear un nuevo inversionista: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getProyectoById(@PathParam("id") String id) throws Exception{
        HashMap<String, Object> map = new HashMap<>();
        InversionistaServices is = new InversionistaServices();
        EventoCrudServices ec = new EventoCrudServices();
        try {
            map.put("msg", "OK");
            map.put("data", is.getInversionistaById(Integer.valueOf(id)));
            ec.registrarEvento(TipoCrud.READ, "Se ha consultado un inversionista con id: " + id);
            return Response.ok(map).build();
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "ERROR");
            map.put("error", e.toString());
            ec.registrarEvento(TipoCrud.READ, "Error al consultar un inversionista con id: " + id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/provincia")
    public Response getProvincia()throws ListEmptyException, Exception{
        HashMap<String, Object> map = new HashMap<>();
        InversionistaServices is = new InversionistaServices();
        map.put("msg", "OK");
        map.put("data", is.getProvincia());
        return Response.ok(map).build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sector")
    public Response getSector()throws ListEmptyException, Exception{
        HashMap<String, Object> map = new HashMap<>();
        InversionistaServices is = new InversionistaServices();
        map.put("msg", "OK");
        map.put("data", is.getSector());
        return Response.ok(map).build();
    }
    

}
