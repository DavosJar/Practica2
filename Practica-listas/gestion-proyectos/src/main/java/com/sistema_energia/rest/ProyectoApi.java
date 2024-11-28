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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sistema_energia.controller.dao.services.EventoCrudServices;
import com.sistema_energia.controller.dao.services.ProyectoServices;
import com.sistema_energia.controller.excepction.ListEmptyException;
import com.sistema_energia.controller.model.Proyecto;
import com.sistema_energia.controller.tda.list.LinkedList;
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
            LinkedList lista = ps.listAll();
            res.put("msg", "Consulta exitosa.");
            res.put("data", lista.toArray());
            if (lista.isEmpty()) {
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
        try {

            ps.save();
            res.put("msg", "OK");
            res.put("data", " Guardado con exito");
            String nombreProyecto = ps.getProyecto().getNombre();
            ev.registrarEvento(TipoCrud.CREATE, "Se ha creado un nuevo proyecto." + nombreProyecto);
            return Response.ok(res).build();

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
                map.put("data", "No se encontro el proyecto con id: " + id);
                ev.registrarEvento(TipoCrud.READ, "No se encontro el proyecto con id: " + id);
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            ev.registrarEvento(TipoCrud.READ, "Se ha consultado el proyecto con id: " + id);
            return Response.ok(map).build();
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "ERROR");
            map.put("error", e.toString());
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

    /*
     * @POST
     * 
     * @Produces(MediaType.APPLICATION_JSON)
     * 
     * @Path("/{proyectoId}/save-inversionista")
     * public Response saveInversionista(@PathParam("proyectoId") int proyectoId,
     * Inversionista inversionista) throws Exception {
     * HashMap<String, Object> map = new HashMap<>();
     * ProyectoServices ps = new ProyectoServices();
     * InversionistaServices is = new InversionistaServices();
     * EventoCrudServices ev = new EventoCrudServices();
     * 
     * try {
     * if (inversionista.getNombre() == null || inversionista.getNombre().isEmpty())
     * {
     * throw new IllegalArgumentException("El nombre es obligatorio.");
     * }
     * if (inversionista.getRegistro() == null ||
     * inversionista.getRegistro().isEmpty()) {
     * throw new IllegalArgumentException("El registro es obligatorio.");
     * }
     * if (inversionista.getMontoInvertido() <= 0) {
     * throw new
     * IllegalArgumentException("El monto invertido debe ser un uúmero positivo.");
     * }
     * if (inversionista.getSector() == null) {
     * throw new IllegalArgumentException("El sector es obligatorio.");
     * }
     * if (inversionista.getUbicacion() == null) {
     * throw new IllegalArgumentException("La ubicacion es obligatoria.");
     * }
     * 
     * is.getInversionista().setNombre(inversionista.getNombre());
     * is.getInversionista().setRegistro(inversionista.getRegistro());
     * is.getInversionista().setMontoInvertido(Double.valueOf(inversionista.
     * getMontoInvertido().toString()));
     * is.getInversionista().setSector(is.getSector(inversionista.getSector().
     * toString()));
     * is.getInversionista().setUbicacion(is.getProvincia(inversionista.getUbicacion
     * ().toString()));
     * is.getInversionista().setProyectoId(proyectoId);
     * 
     * boolean success = ps.saveInversionistaProyecto(is.getInversionista(),
     * proyectoId);
     * 
     * String nuevoInversionista = is.getInversionista().getNombre();
     * String nombreProyecto = ps.getProyectoByIndex(proyectoId).getNombre();
     * 
     * if (success) {
     * map.put("msg", "OK");
     * map.put("data", nuevoInversionista + " guardado con exito.");
     * ev.registrarEvento(TipoCrud.CREATE, "Se ha creado un nuevo inversionista: " +
     * nuevoInversionista + " para el proyecto: " + nombreProyecto);
     * return Response.ok(map).build();
     * } else {
     * map.put("msg", "ERROR");
     * map.put("data", "No se pudo guardar el inversionista.");
     * ev.registrarEvento(TipoCrud.CREATE, "No se pudo guardar el inversionista: " +
     * nuevoInversionista + " para el proyecto: " + nombreProyecto);
     * return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
     * }
     * } catch (IllegalArgumentException e) {
     * map.put("msg", "ERROR");
     * map.put("error", e.getMessage());
     * ev.registrarEvento(TipoCrud.CREATE, e.getMessage());
     * return Response.status(Status.BAD_REQUEST).entity(map).build();
     * } catch (Exception e) {
     * map.put("msg", "ERROR");
     * map.put("error", "Error inesperado: " + e.getMessage());
     * ev.registrarEvento(TipoCrud.CREATE, "Error inesperado: " + e.getMessage());
     * return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
     * }
     * }
     * 
     * @GET
     * 
     * @Produces(MediaType.APPLICATION_JSON)
     * 
     * @Path("/{proyectoId}/inversionistas")
     * public Response getInversionistasByProyectoId(@PathParam("proyectoId") int
     * proyectoId) throws Exception {
     * HashMap<String, Object> res = new HashMap<>();
     * ProyectoServices ps = new ProyectoServices();
     * EventoCrudServices ev = new EventoCrudServices();
     * try {
     * LinkedList<Inversionista> inversionistas =
     * ps.getInversionistasByProyectoId(proyectoId);
     * if (inversionistas == null || inversionistas.getSize() == 0) {
     * throw new ListEmptyException("No hay inversionistas asociados al proyecto.");
     * }
     * res.put("msg", "OK");
     * res.put("data", inversionistas.toArray());
     * ev.registrarEvento(TipoCrud.LIST,
     * "Se ha consultado los inversionistas del proyecto con id: " + proyectoId);
     * 
     * } catch (ListEmptyException e) {
     * res.put("msg", "ERROR");
     * res.put("error", e.getMessage());
     * ev.registrarEvento(TipoCrud.LIST, e.getMessage());
     * return Response.status(Status.NOT_FOUND).entity(res).build();
     * } catch (Exception e) {
     * res.put("msg", "ERROR");
     * res.put("error", "Ocurrió un error inesperado: " + e.getMessage());
     * ev.registrarEvento(TipoCrud.LIST, "Error inesperado: " + e.getMessage());
     * return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
     * }
     * return Response.ok(res).build();
     * }
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/update")
    public Response update(@PathParam("id") Integer id, HashMap<String, Object> map) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        EventoCrudServices ev = new EventoCrudServices();
        ps.getProyecto().setId(id);
        ps.getProyecto().setNombre(map.get("nombre").toString());
        ps.getProyecto().setCostoEstimadoInicial(Double.parseDouble(map.get("costoEstimadoInicial").toString()));
        ps.getProyecto().setInversion(Double.parseDouble(map.get("inversion").toString()));
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
            ps.getProyecto().setFechaFin(null);
        }
        try {

            ps.update();
            res.put("msg", "OK");
            res.put("data", "Registro actualizado con exito.");
            ev.registrarEvento(TipoCrud.UPDATE, "Se ha actualizado el proyecto con id: " + id);
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
            if (attribute.equals("id") && !value.matches("[0-9]+")) {
                throw new IllegalArgumentException("El valor de busqueda debe ser un numero entero.");
            }
            if (attribute.equals("nombre") && !value.matches("[a-zA-Z]+")) {
                throw new IllegalArgumentException("El valor de busqueda debe ser una cadena de texto.");
            }

            if (attribute.equals("id") || attribute.equals("nombre")) {
                Object proyecto = ps.obtenerProyectoPor(attribute, value);
                if (proyecto == null) {
                    res.put("status", "ERROR");
                    res.put("msg", "No se encontró el proyecto con " + attribute + ": " + value);
                    return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
                } else {
                    res.put("status", "OK");
                    res.put("msg", "Consulta exitosa.");
                    res.put("data", proyecto);
                    ev.registrarEvento(TipoCrud.LIST, "Se ha consultado el proyecto con " + attribute + ": " + value);
                    return Response.ok(res).build();
                }
            } else {
                LinkedList<Proyecto> proyectos = ps.getProyectosBy(attribute, value);
                res.put("status", "OK");
                res.put("msg", "Consulta exitosa.");
                res.put("data", proyectos.toArray());
                if (proyectos.isEmpty()) {
                    res.put("data", new Object[] {});

                }
                ev.registrarEvento(TipoCrud.LIST, "Se ha consultado el proyecto con " + attribute + ": " + value);
                return Response.ok(res).build();
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
    @Path("/list/order/{attribute}/{type}")
    public Response orderList(@PathParam("attribute") String attribute, @PathParam("type") Integer type)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            if (attribute == null || attribute.isEmpty() || type == null || type.toString().isEmpty()) {
                throw new IllegalArgumentException("Los parametros no pueden ser nulos o vacios.");
            }
            if (type != 1 && type != 0) {
                throw new IllegalArgumentException("El tipo de ordenamiento no es valido.");
            }

            LinkedList<Proyecto> proyectos = ps.orderListBy(attribute, type);
            res.put("status", "OK");
            res.put("msg", "Consulta exitosa.");
            res.put("data", proyectos.toArray());
            if (proyectos.isEmpty()) {
                res.put("data", new Object[] {});
            }
            ev.registrarEvento(TipoCrud.LIST, "Se ha consultado la lista de proyectos ordenada por " + attribute + " "
                    + type + ".");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al ordenar la lista: " + e.getMessage());
            ev.registrarEvento(TipoCrud.LIST, "Error inesperado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

}
