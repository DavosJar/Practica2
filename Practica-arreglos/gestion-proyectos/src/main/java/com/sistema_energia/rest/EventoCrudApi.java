package com.sistema_energia.rest;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sistema_energia.controller.dao.services.EventoCrudServices;
import com.sistema_energia.controller.excepction.ListEmptyException;
import com.sistema_energia.eventos.EventoCrud;


/**
 * Clase que implementa los servicios REST para la entidad Evento
 * los metodos almacenaran la informacion en un archivo JSON
 */

@Path("/evento")
public class EventoCrudApi {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAllEventos() throws ListEmptyException, Exception{
        HashMap<String, Object> res = new HashMap<>();
        EventoCrudServices es = new EventoCrudServices();
        try {
            EventoCrud[] eventos = es.listAll();
            if(eventos.length == 0){
                throw new ListEmptyException("Error: No hay eventos Registrados.");
            }
            res.put("msg", "OK");
            res.put("data", eventos);
        } catch (Exception e) {
            res.put("msg", "Error al obtener la lista de eventos: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
        return Response.ok(res).build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/{id}")
    public Response getEventoById(@PathParam("id") Integer id) throws ListEmptyException, Exception{
        HashMap<String, Object> map = new HashMap<>();
        EventoCrudServices es = new EventoCrudServices();
        try {
            EventoCrud evento = es.getEventoCrudById(id);
            if(evento == null){
                throw new ListEmptyException("Error: No hay eventos Registrados.");
            }
            map.put("msg", "OK");
            map.put("data", evento);
        } catch (Exception e) {
            map.put("msg", "Error al obtener la lista de eventos: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
        return Response.ok(map).build();
    }

}

