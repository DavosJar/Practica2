/**
 * Clase que contiene los servicios REST para la entidad Proyecto.
 * metodos: guardar, listar, obtener por id, obtener provincias, obtener tipos de energia, obtener estados, guardar inversionista en proyecto, obtener inversionistas por proyecto.
 */

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
import com.sistema_energia.controller.dao.services.ProyectoServices;
import com.sistema_energia.controller.excepction.ListEmptyException;
import com.sistema_energia.eventos.TipoCrud;

@SuppressWarnings({ "unchecked", "ConvertToTryWithResources", "rawtypes", "CallToPrintStackTrace" })
@Path("/proyecto")
public class ProyectoApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAllProyects() throws ListEmptyException, Exception {
        HashMap<String, Object> res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            res.put("status", "OK");
            res.put("msg", "Consulta exitosa.");
            res.put("data", ps.listAll().toArray());
            if (ps.listAll().isEmpty()) {
                res.put("data", new Object[] {});
            }
            ev.registrarEvento(TipoCrud.LIST, "Se ha consultado la lista de proyectos.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de proyectos: " + e.getMessage());
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
        EventoCrudServices ev = new EventoCrudServices();
        ProyectoServices ps = new ProyectoServices();

        try {
            if (map.get("nombre") == null || map.get("nombre").toString().isEmpty()) {
                throw new IllegalArgumentException("El nombre del proyecto no puede ser nulo o vacío.");
            }
            if (map.get("nombre").toString().contains(" ")) {
                throw new IllegalArgumentException("El nombre del proyecto no debe contener espacios.");
            }
            if (map.get("costoEstimadoInicial") == null) {
                throw new IllegalArgumentException("El costo estimado inicial es obligatorio.");
            }
            if (map.get("fechaInicio") == null || map.get("fechaInicio").toString().isEmpty()) {
                throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
            }
            if (map.get("tiempoDeVida") == null) {
                throw new IllegalArgumentException("El tiempo de vida es obligatorio.");
            }
            if (map.get("tipoEnergia") == null || map.get("tipoEnergia").toString().isEmpty()) {
                throw new IllegalArgumentException("El tipo de energía es obligatorio.");
            }
            if (map.get("ubicacion") == null || map.get("ubicacion").toString().isEmpty()) {
                throw new IllegalArgumentException("La ubicación es obligatoria.");
            }

            ps.getProyecto().setNombre(map.get("nombre").toString());
            ps.getProyecto().setCostoEstimadoInicial(Double.parseDouble(map.get("costoEstimadoInicial").toString()));
            ps.getProyecto().setFechaInicio(map.get("fechaInicio").toString());
            ps.getProyecto().setTiempoDeVida(Integer.valueOf(map.get("tiempoDeVida").toString()));
            ps.getProyecto()
                    .setCapacidad(map.get("capacidad") != null ? Integer.valueOf(map.get("capacidad").toString()) : 0);
            ps.getProyecto().setTipoEnergia(ps.getTipoEnergia(map.get("tipoEnergia").toString()));
            ps.getProyecto().setUbicacion(ps.getProvincia(map.get("ubicacion").toString()));
            ps.getProyecto().setDescripcion(map.get("descripcion").toString());
            ps.getProyecto().setEstado(ps.getEstado(map.get("estado").toString()));

            ps.save();
            res.put("msg", "OK");
            res.put("data", "Guardado con exito.");
            String nombreProyecto = ps.getProyecto().getNombre();
            ev.registrarEvento(TipoCrud.CREATE, "Se ha creado un nuevo proyecto: " + nombreProyecto);
            return Response.ok(res).build();

        } catch (IllegalArgumentException e) {
            res.put("msg", "ERROR");
            res.put("error", e.getMessage());
            ev.registrarEvento(TipoCrud.CREATE, e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            e.printStackTrace();
            res.put("msg", "ERROR");
            res.put("error", "Error inesperado: " + e.toString());
            ev.registrarEvento(TipoCrud.CREATE, "Error inesperado: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Integer id) throws Exception {

        HashMap res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            ps.getProyecto().setId(id);
            ps.delete();
            res.put("estado", "Ok");
            res.put("data", "Registro eliminado con exito.");
            ev.registrarEvento(TipoCrud.DELETE, "Se ha eliminado el proyecto con id: " + id);
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
    public Response getProyectoById(@PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        EventoCrudServices ev = new EventoCrudServices();
        ProyectoServices ps = new ProyectoServices();
        try {
            map.put("msg", "OK");
            map.put("data", ps.getProyectoById(id));
            if (ps.getProyectoById(id) == null) {
                map.put("msg", "ERROR");
                map.put("error", "No se encontro el proyecto con id: " + id);
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            ev.registrarEvento(TipoCrud.READ, "Se ha consultado el proyecto con id: " + id);
            return Response.ok(map).build();
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "ERROR");
            map.put("error", "Error inesperado: " + e.getMessage());
            ev.registrarEvento(TipoCrud.READ, "Error inesperado: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/provincia")
    public Response getProvincia() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        map.put("msg", "OK");
        map.put("data", ps.getProvincia());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/tipos")
    public Response getTipoEnergia() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        map.put("msg", "OK");
        map.put("data", ps.getTipoEnergia());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/estado")
    public Response getEstado() throws ListEmptyException, Exception {
        HashMap<String, Object> map = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        map.put("msg", "OK");
        map.put("data", ps.getEstado());
        return Response.ok(map).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list/criteria")
    public Response getCrrioList() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        map.put("msg", "OK");
        map.put("data", ps.getProyectoAttributeLists());
        return Response.ok(map).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/update")
    public Response update(HashMap<String, Object> map) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        EventoCrudServices ev = new EventoCrudServices();

        try {

            ProyectoServices ps = new ProyectoServices();
            ps.setProyecto(ps.getProyectoById(Integer.valueOf(map.get("id").toString())));
            ps.getProyecto().setNombre(map.get("nombre").toString());
            ps.getProyecto().setCostoEstimadoInicial(Double.parseDouble(map.get("costoEstimadoInicial").toString()));
            ps.getProyecto().setFechaInicio(map.get("fechaInicio").toString());
            ps.getProyecto().setTiempoDeVida(Integer.valueOf(map.get("tiempoDeVida").toString()));
            ps.getProyecto()
                    .setCapacidad(map.get("capacidad") != null ? Integer.valueOf(map.get("capacidad").toString()) : 0);
            ps.getProyecto().setTipoEnergia(ps.getTipoEnergia(map.get("tipoEnergia").toString()));
            ps.getProyecto().setUbicacion(ps.getProvincia(map.get("ubicacion").toString()));
            ps.getProyecto().setDescripcion(map.get("descripcion").toString());
            ps.getProyecto().setEstado(ps.getEstado(map.get("estado").toString()));
            if (map.containsKey("fechaFin") && map.get("fechaFin") != null) {
                ps.getProyecto().setFechaFin(map.get("fechaFin").toString());
            } else {
                ps.getProyecto().setFechaFin("none");
            }

            ps.update();
            res.put("msg", "OK");
            res.put("data", "Registro actualizado con exito.");
            ev.registrarEvento(TipoCrud.UPDATE,
                    "Se ha actualizado el proyecto con id: " + ps.getProyecto().getNombre());
            return Response.ok(res).build();

        } catch (IllegalArgumentException e) {
            res.put("msg", "ERROR");
            res.put("error", e.getMessage());
            ev.registrarEvento(TipoCrud.UPDATE, e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            e.printStackTrace();
            res.put("msg", "ERROR");
            res.put("error", "Ocurrio un error inesperado: " + e.toString());
            ev.registrarEvento(TipoCrud.UPDATE, "Error inesperado: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/search/nombre/{value}")
    public Response buscarPorNombre(@PathParam("value") String value) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            if (value == null || value.isEmpty()) {
                throw new IllegalArgumentException("El valor de busqueda no puede ser nulo o vacio.");
            }
            if (ps.obtenerProyectoPor("nombre", value) != null) {
                res.put("status", "OK");
                res.put("msg", "Consulta exitosa.");
                res.put("data", ps.obtenerProyectoPor("nombre", value));
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
        ProyectoServices ps = new ProyectoServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            res.put("status", "OK");
            res.put("msg", "Consulta exitosa.");
            res.put("data", ps.selectOrder(attribute, type, method).toArray());
            if (ps.selectOrder(attribute, type, method).isEmpty()) {
                res.put("data", new Object[] {});
            }
            ev.registrarEvento(TipoCrud.LIST, "Se ha consultado la lista de proyectos.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de proyectos: " + e.getMessage());
            ev.registrarEvento(TipoCrud.LIST, "Error inesperado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

}
