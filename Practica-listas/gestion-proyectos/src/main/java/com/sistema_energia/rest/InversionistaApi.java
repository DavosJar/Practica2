package com.sistema_energia.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import com.sistema_energia.controller.dao.services.ProyectoServices;
import com.sistema_energia.controller.excepction.ListEmptyException;
import com.sistema_energia.controller.tda.list.LinkedList;
import com.sistema_energia.eventos.TipoCrud;

@SuppressWarnings("rawtypes")
@Path("/inversionista")
public class InversionistaApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAll() throws ListEmptyException, Exception {
        HashMap<String, Object> res = new HashMap<>();
        InversionistaServices is = new InversionistaServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            if (is.listAll().isEmpty()) {
                res.put("data", new Object[] {});
            }
            ev.registrarEvento(TipoCrud.LIST, "Se ha consultado la lista de inversionistas.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de inversionistas: " + e.getMessage());
            ev.registrarEvento(TipoCrud.LIST, "Error inesperado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
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
            if (map.get("sector") == null || map.get("sector").toString().isEmpty()) {
                throw new IllegalArgumentException("El sector es obligatorio.");
            }
            if (map.get("ubicacion") == null || map.get("ubicacion").toString().isEmpty()) {
                throw new IllegalArgumentException("La ubicacion es obligatoria.");
            }

            InversionistaServices is = new InversionistaServices();
            is.getInversionista().setNombre(map.get("nombre").toString());
            is.getInversionista().setRegistro(map.get("registro").toString());
            is.getInversionista().setSector(is.getSector(map.get("sector").toString()));
            is.getInversionista().setUbicacion(is.getProvincia(map.get("ubicacion").toString()));

            is.save();
            res.put("msg", "OK");
            res.put("data", " Guardado con exito");
            ec.registrarEvento(TipoCrud.CREATE,
                    "Se ha creado un nuevo inversionista: " + is.getInversionista().getNombre());
            return Response.ok(res).build();

        } catch (IllegalArgumentException e) {
            res.put("msg", "ERROR");
            res.put("error", e.getMessage());
            ec.registrarEvento(TipoCrud.CREATE, "Error al crear un nuevo inversionista: " + e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            res.put("msg", "error");
            res.put("error", e.toString());
            ec.registrarEvento(TipoCrud.CREATE, "Error al crear un nuevo inversionista: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getInversionistaById(@PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        InversionistaServices is = new InversionistaServices();
        EventoCrudServices ec = new EventoCrudServices();
        try {
            map.put("msg", "OK");
            map.put("data", is.getInversionistaById(id));
            if (is.getInversionistaById(id) == null) {
                map.put("msg", "ERROR");
                map.put("data", "No se encontro el inversionista con id: " + id);
                ec.registrarEvento(TipoCrud.READ, "No se encontro el inversionista con id: " + id);
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            ec.registrarEvento(TipoCrud.READ, "Se ha consultado el inversionista con id: " + id);
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "ERROR");
            map.put("error", e.toString());
            ec.registrarEvento(TipoCrud.READ, "Error inesperado: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/update")
    public Response update(HashMap<String, Object> map) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        EventoCrudServices ec = new EventoCrudServices();
        try {
            InversionistaServices is = new InversionistaServices();
            is.getInversionista().setId(Integer.valueOf(map.get("id").toString()));
            is.getInversionista().setNombre(map.get("nombre").toString());
            is.getInversionista().setRegistro(map.get("registro").toString());
            is.getInversionista().setSector(is.getSector(map.get("sector").toString()));
            is.getInversionista().setUbicacion(is.getProvincia(map.get("ubicacion").toString()));
            is.update();
            res.put("msg", "OK");
            res.put("data", "Actualizado con exito");
            ec.registrarEvento(TipoCrud.UPDATE,
                    "Se ha actualizado el inversionista con id: " + is.getInversionista().getId());
            return Response.ok(res).build();
        } catch (IllegalArgumentException e) {
            res.put("msg", "ERROR");
            res.put("error", e.getMessage());
            ec.registrarEvento(TipoCrud.UPDATE, e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            res.put("msg", "ERROR");
            res.put("error", "Ocurrio un error inesperado: " + e.toString());
            ec.registrarEvento(TipoCrud.UPDATE, "Error inesperado: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @SuppressWarnings("unchecked")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Integer id) throws Exception {

        HashMap res = new HashMap<>();
        InversionistaServices ps = new InversionistaServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            ps.getInversionista().setId(id);
            ps.delete();
            res.put("estado", "Ok");
            res.put("data", "Registro eliminado con exito.");
            ev.registrarEvento(TipoCrud.DELETE, "Se ha eliminado el inversionista con id: " + id);
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
    @Path("/list/search/registro/{value}")
    public Response buscarPorNombre(@PathParam("value") String value) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        InversionistaServices ps = new InversionistaServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            if (value == null || value.isEmpty()) {
                throw new IllegalArgumentException("El valor de busqueda no puede ser nulo o vacio.");
            }
            if (ps.obtenerInversionistaPor("nombre", value) != null) {
                res.put("status", "OK");
                res.put("msg", "Consulta exitosa.");
                res.put("data", ps.obtenerInversionistaPor("registro", value));
                ev.registrarEvento(TipoCrud.LIST, "Se ha consultado el proyecto con nombre: " + value);
                return Response.ok(res).build();
            } else {
                res.put("status", "ERROR");
                res.put("msg", "No se encontraron resultados para la busqueda.");
                ev.registrarEvento(TipoCrud.LIST, "No se encontraron resultados para la busqueda.");
                return Response.status(Response.Status.NOT_FOUND).entity(res).build();
            }

        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error realizar la busqueda: " + e.getMessage());
            ev.registrarEvento(TipoCrud.LIST, "Error inesperado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/search/{attribute}/{value}")
    public Response buscar(@PathParam("attribute") String attribute, @PathParam("value") String value)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            if (attribute == null || attribute.isEmpty() || value == null || value.isEmpty()) {
                throw new IllegalArgumentException("Los parametros no pueden ser nulos o vacios.");
            }
            if (attribute.equals("id")) {
                throw new IllegalArgumentException("No se puede buscar por id.");
            }
            res.put("status", "OK");
            res.put("msg", "Consulta exitosa.");
            res.put("data", ps.getProyectosBy(attribute, value).toArray());
            if (ps.getProyectosBy(attribute, value).isEmpty()) {
                res.put("data", new Object[] {});

            }
            ev.registrarEvento(TipoCrud.LIST, "Se ha consultado el proyecto con " + attribute + ": " + value);
            return Response.ok(res).build();

        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error realizar la busqueda: " + e.getMessage());
            ev.registrarEvento(TipoCrud.LIST, "Error inesperado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list/order/{attribute}/{type}/{method}")
    public Response listOrder(@PathParam("attribute") String attribute, @PathParam("type") Integer type,
            @PathParam("method") String method)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        InversionistaServices ps = new InversionistaServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            res.put("status", "OK");
            LinkedList lista = ps.selectOrder(attribute, type, method);
            res.put("msg", "Consulta exitosa.");
            res.put("data", ps.selectOrder(attribute, type, method).toArray());
            if (lista.isEmpty()) {
                res.put("data", new Object[] {});
            }
            ev.registrarEvento(TipoCrud.LIST, "Se ha consultado la lista de inversionistas.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de inversionistas: " + e.getMessage());
            ev.registrarEvento(TipoCrud.LIST, "Error inesperado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/provincia")
    public Response getProvincia() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        InversionistaServices is = new InversionistaServices();
        map.put("msg", "OK");
        map.put("data", is.getProvincia());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sector")
    public Response getSector() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        InversionistaServices is = new InversionistaServices();
        map.put("msg", "OK");
        map.put("data", is.getSector());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list/criteria")
    public Response getCrrioList() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        InversionistaServices is = new InversionistaServices();
        map.put("msg", "OK");
        map.put("data", is.getInversionistaAttributeLists());
        return Response.ok(map).build();
    }

}
