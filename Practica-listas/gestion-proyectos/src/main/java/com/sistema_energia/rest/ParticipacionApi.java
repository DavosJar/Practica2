/**
 * Clase que contiene los servicios REST para la entidad Participacion.
 * metodos: guardar, listar, obtener por id, obtener provincias, obtener tipos de energia, obtener estados, guardar inversionista en participacion, obtener inversionistas por participacion.
 */

package com.sistema_energia.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.sistema_energia.controller.dao.services.EventoCrudServices;
import com.sistema_energia.controller.dao.services.InversionistaServices;
import com.sistema_energia.controller.dao.services.ParticipacionServices;
import com.sistema_energia.controller.dao.services.ProyectoServices;
import com.sistema_energia.controller.excepction.ListEmptyException;
import com.sistema_energia.controller.tda.list.LinkedList;
import com.sistema_energia.eventos.TipoCrud;

@SuppressWarnings({ "unchecked", "ConvertToTryWithResources", "rawtypes" })
@Path("/participacion")
public class ParticipacionApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAllProyects() throws ListEmptyException, Exception {
        HashMap<String, Object> res = new HashMap<>();
        ParticipacionServices ps = new ParticipacionServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            res.put("status", "OK");
            LinkedList lista = ps.listAll();
            res.put("msg", "Consulta exitosa.");
            res.put("data", lista.toArray());
            if (lista.isEmpty()) {
                res.put("data", new Object[] {});
            }
            ev.registrarEvento(TipoCrud.LIST, "Se ha consultado la lista de participacions.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de participacions: " + e.getMessage());
            ev.registrarEvento(TipoCrud.LIST, "Error inesperado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @SuppressWarnings("CallToPrintStackTrace")
    public Response save(HashMap<String, Object> map) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        Gson g = new Gson();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            if (map.get("inversionista") != null || map.get("proyecto") != null) {
                ProyectoServices p = new ProyectoServices();
                p.setProyecto(p.getProyectoById(Integer.valueOf(map.get("proyecto").toString())));
                InversionistaServices i = new InversionistaServices();
                i.setInversionista(i.getInversionistaById(Integer.valueOf(map.get("inversionista").toString())));
                if (p.getProyecto().getId() != null && i.getInversionista().getId() != null) {
                    ParticipacionServices ps = new ParticipacionServices();
                    ps.getParticipacion().setIdInversionist(i.getInversionista().getId());
                    ps.getParticipacion().setIdProyecto(p.getProyecto().getId());
                    ps.getParticipacion().setMontoInvertido(Double.valueOf(map.get("montoInvertido").toString()));
                    ps.getParticipacion().setFechaRegistro(map.get("fechaRegistro").toString());
                    p.actualizarInversionProyecto(ps.getParticipacion().getIdProyecto(),
                            ps.getParticipacion().getMontoInvertido());
                    ps.getParticipacion().setPorcentajePaticipacio(ps.obtenerPorcentajeParticipacion());

                    ps.save();

                    res.put("msg", "OK");
                    res.put("data", " Guardado con exito");
                    String nombreParticipacion = ps.getParticipacion().getIdInversionista().toString();
                    ev.registrarEvento(TipoCrud.CREATE, "Se ha creado un nuevo participacion." + nombreParticipacion);
                    return Response.ok(res).build();
                } else {
                    res.put("msg", "ERROR");
                    res.put("data", "Debe ingresar un inversionista y un proyecto.");
                    ev.registrarEvento(TipoCrud.CREATE, "Debe ingresar un inversionista y un proyecto.");
                    return Response.status(Status.BAD_REQUEST).entity(res).build();
                }

            } else {
                res.put("msg", "ERROR");
                res.put("data", "Debe ingresar un inversionista y un proyecto.");
                return Response.status(Status.BAD_REQUEST).entity(res).build();
            }

        } catch (IllegalArgumentException e) {
            res.put("msg", "ERROR");
            res.put("error", e.getMessage());
            ev.registrarEvento(TipoCrud.CREATE, e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            e.printStackTrace();
            res.put("msg", "ERROR");
            res.put("error", "Ocurrio un error inesperado: " + e.toString());
            ev.registrarEvento(TipoCrud.CREATE, "Error inesperado: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/update")
    public Response update(@PathParam("id") Integer id, HashMap<String, Object> map) throws Exception {
        HashMap res = new HashMap<>();
        ParticipacionServices ps = new ParticipacionServices();
        EventoCrudServices ev = new EventoCrudServices();
        ps.setParticipacion(ps.getParticipacionById(id));
        if (ps.getParticipacion().getId() == null) {
            res.put("estado", "error");
            res.put("data", "No se ha encontrado la participacion");
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        } else {
            if (map.get("inversionista") != null || map.get("proyecto") != null) {
                ProyectoServices p = new ProyectoServices();
                p.setProyecto(p.getProyectoById(Integer.valueOf(map.get("proyecto").toString())));
                InversionistaServices i = new InversionistaServices();
                i.setInversionista(i.getInversionistaById(Integer.valueOf(map.get("inversionista").toString())));
                if (p.getProyecto().getId() != null && i.getInversionista().getId() != null) {
                    ps.getParticipacion().setIdInversionist(i.getInversionista().getId());
                    ps.getParticipacion().setIdProyecto(p.getProyecto().getId());
                    ps.getParticipacion().setMontoInvertido(Double.valueOf(map.get("montoInvertido").toString()));
                    ps.update();
                    res.put("msg", "OK");
                    res.put("data", " Guardado con exito");
                    String nombreParticipacion = ps.getParticipacion().getIdInversionista().toString();
                    ev.registrarEvento(TipoCrud.CREATE, "Se ha creado un nuevo participacion." + nombreParticipacion);
                    return Response.ok(res).build();
                } else {
                    res.put("msg", "ERROR");
                    res.put("data", "Debe ingresar un inversionista y un proyecto.");
                    ev.registrarEvento(TipoCrud.CREATE, "Debe ingresar un inversionista y un proyecto.");
                    return Response.status(Status.BAD_REQUEST).entity(res).build();
                }

            } else {
                res.put("msg", "ERROR");
                res.put("data", "Debe ingresar un inversionista y un proyecto.");
                return Response.status(Status.BAD_REQUEST).entity(res).build();
            }

        }

    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Integer id) throws Exception {

        HashMap res = new HashMap<>();
        ParticipacionServices ps = new ParticipacionServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            ps.getParticipacion().setId(id);
            ps.delete();
            res.put("estado", "Ok");
            res.put("data", "Registro eliminado con exito.");
            ev.registrarEvento(TipoCrud.DELETE, "Se ha eliminado el participacion con id: " + id);
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            ev.registrarEvento(TipoCrud.DELETE, "Error inesperado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getParticipacionById(@PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        EventoCrudServices ev = new EventoCrudServices();
        ParticipacionServices ps = new ParticipacionServices();
        try {
            map.put("msg", "OK");
            map.put("data", ps.getParticipacionById(id));
            ev.registrarEvento(TipoCrud.READ, "Se ha consultado el participacion con id: " + id);
            return Response.ok(map).build();
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "ERROR");
            map.put("error", e.toString());
            ev.registrarEvento(TipoCrud.READ, "Error inesperado: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }
}