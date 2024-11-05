/**
 * Clase que contiene los servicios REST para la entidad Proyecto.
 * metodos: guardar, listar, obtener por id, obtener provincias, obtener tipos de energia, obtener estados, guardar inversionista en proyecto, obtener inversionistas por proyecto.
 */

package com.sistema_energia.rest;

import java.util.HashMap;

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
import com.sistema_energia.controller.dao.services.InversionistaServices;
import com.sistema_energia.controller.dao.services.ProyectoServices;
import com.sistema_energia.controller.excepction.ListEmptyException;
import com.sistema_energia.controller.model.Inversionista;
import com.sistema_energia.controller.tda.list.LinkedList;
import com.sistema_energia.eventos.TipoCrud; 


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
    @SuppressWarnings("CallToPrintStackTrace")
    public Response save(HashMap<String, Object> map) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        EventoCrudServices ev = new EventoCrudServices();
        
        try {
            if (map.get("nombre") == null || map.get("nombre").toString().isEmpty()) {
                throw new IllegalArgumentException("El nombre es obligatorio.");
            }
            if (map.get("costoEstimadoInicial") == null || Double.parseDouble(map.get("costoEstimadoInicial").toString()) < 0) {
                throw new IllegalArgumentException("El costo estimado inicial debe ser un numero positivo.");
            }
            if (map.get("inversion") == null || Double.parseDouble(map.get("inversion").toString()) < 0) {
                throw new IllegalArgumentException("La inversion debe ser un numero positivo.");
            }
            if (map.get("fechaInicio") == null || map.get("fechaInicio").toString().isEmpty()) {
                throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
            }
            if (map.get("fechaFin") == null || map.get("fechaFin").toString().isEmpty()) {
                throw new IllegalArgumentException("La fecha de fin es obligatoria.");
            }
            if (map.get("tiempoDeVida") == null || Integer.parseInt(map.get("tiempoDeVida").toString()) <= 0) {
                throw new IllegalArgumentException("El tiempo de vida debe ser un numero entero positivo.");
            }

            ProyectoServices ps = new ProyectoServices();
            ps.getProyecto().setNombre(map.get("nombre").toString());
            ps.getProyecto().setCostoEstimadoInicial(Double.parseDouble(map.get("costoEstimadoInicial").toString()));
            ps.getProyecto().setFechaInicio(map.get("fechaInicio").toString());
            ps.getProyecto().setTiempoDeVida(Integer.valueOf(map.get("tiempoDeVida").toString()));
            ps.getProyecto().setCapacidad(map.get("capacidad") != null ? Integer.valueOf(map.get("capacidad").toString()) : 0);
            ps.getProyecto().setTipoEnergia(ps.getTipoEnergia(map.get("tipoEnergia").toString()));
            ps.getProyecto().setUbicacion(ps.getProvincia(map.get("ubicacion").toString()));
            ps.getProyecto().setDescripcion(map.get("descripcion").toString());
            ps.getProyecto().setEstado(ps.getEstado(map.get("estado").toString()));

            ps.save();
            res.put("msg", "OK");
            res.put("data", ps.toJson() + " Guardado con exito");
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
    public Response delete(@PathParam("id") Integer id) throws Exception{
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
    public Response getProyectoById(@PathParam("id") String id) throws Exception{
        HashMap<String, Object> map = new HashMap<>();
        EventoCrudServices ev = new EventoCrudServices();
        ProyectoServices ps = new ProyectoServices();
        try {
            map.put("msg", "OK");
            map.put("data", ps.getProyectoByIndex(Integer.valueOf(id)));
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
    public Response getProvincia()throws ListEmptyException, Exception{
        HashMap<String, Object> map = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        map.put("msg", "OK");
        map.put("data", ps.getProvincia());
        return Response.ok(map).build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/tipo-energia")
    public Response getTipoEnergia()throws ListEmptyException, Exception{
        HashMap<String, Object> map = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        map.put("msg", "OK");
        map.put("data", ps.getTipoEnergia());
        return Response.ok(map).build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/estado")
    public Response getEstado()throws ListEmptyException, Exception{
        HashMap<String, Object> map = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        map.put("msg", "OK");
        map.put("data", ps.getEstado());
        return Response.ok(map).build();
    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{proyectoId}/save-inversionista")
    public Response saveInversionista(@PathParam("proyectoId") int proyectoId, Inversionista inversionista) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        InversionistaServices is = new InversionistaServices();
        EventoCrudServices ev = new EventoCrudServices();
        
        try {
            if (inversionista.getNombre() == null || inversionista.getNombre().isEmpty()) {
                throw new IllegalArgumentException("El nombre es obligatorio.");
            }
            if (inversionista.getRegistro() == null || inversionista.getRegistro().isEmpty()) {
                throw new IllegalArgumentException("El registro es obligatorio.");
            }
            if (inversionista.getMontoInvertido() <= 0) {
                throw new IllegalArgumentException("El monto invertido debe ser un uúmero positivo.");
            }
            if (inversionista.getSector() == null) {
                throw new IllegalArgumentException("El sector es obligatorio.");
            }
            if (inversionista.getUbicacion() == null) {
                throw new IllegalArgumentException("La ubicacion es obligatoria.");
            }
    
            is.getInversionista().setNombre(inversionista.getNombre());
            is.getInversionista().setRegistro(inversionista.getRegistro());
            is.getInversionista().setMontoInvertido(Double.valueOf(inversionista.getMontoInvertido().toString()));
            is.getInversionista().setSector(is.getSector(inversionista.getSector().toString()));
            is.getInversionista().setUbicacion(is.getProvincia(inversionista.getUbicacion().toString()));
            is.getInversionista().setProyectoId(proyectoId);    
    
            boolean success = ps.saveInversionistaProyecto(is.getInversionista(), proyectoId);
    
            String nuevoInversionista = is.getInversionista().getNombre(); 
            String nombreProyecto = ps.getProyectoByIndex(proyectoId).getNombre();
    
            if (success) {
                map.put("msg", "OK");
                map.put("data", nuevoInversionista + " guardado con exito.");
                ev.registrarEvento(TipoCrud.CREATE, "Se ha creado un nuevo inversionista: " + nuevoInversionista + " para el proyecto: " + nombreProyecto);
                return Response.ok(map).build();
            } else {
                map.put("msg", "ERROR");
                map.put("data", "No se pudo guardar el inversionista.");
                ev.registrarEvento(TipoCrud.CREATE, "No se pudo guardar el inversionista: " + nuevoInversionista + " para el proyecto: " + nombreProyecto);
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
            }
        } catch (IllegalArgumentException e) {
            map.put("msg", "ERROR");
            map.put("error", e.getMessage());
            ev.registrarEvento(TipoCrud.CREATE, e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(map).build();
        } catch (Exception e) {
            map.put("msg", "ERROR");
            map.put("error", "Error inesperado: " + e.getMessage());
            ev.registrarEvento(TipoCrud.CREATE, "Error inesperado: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{proyectoId}/inversionistas")
    public Response getInversionistasByProyectoId(@PathParam("proyectoId") int proyectoId) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {      
            LinkedList<Inversionista> inversionistas = ps.getInversionistasByProyectoId(proyectoId);
            if (inversionistas == null || inversionistas.getSize() == 0) {
                throw new ListEmptyException("No hay inversionistas asociados al proyecto.");
            }
            res.put("msg", "OK");
            res.put("data", inversionistas.toArray());
            ev.registrarEvento(TipoCrud.LIST, "Se ha consultado los inversionistas del proyecto con id: " + proyectoId);

        } catch (ListEmptyException e) {
            res.put("msg", "ERROR");
            res.put("error", e.getMessage());
            ev.registrarEvento(TipoCrud.LIST, e.getMessage());
            return Response.status(Status.NOT_FOUND).entity(res).build();
        } catch (Exception e) {
            res.put("msg", "ERROR");
            res.put("error", "Ocurrió un error inesperado: " + e.getMessage());
            ev.registrarEvento(TipoCrud.LIST, "Error inesperado: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
        return Response.ok(res).build();
    }
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/update")
    public Response update(@PathParam("id") Integer id, HashMap<String, Object> map) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        EventoCrudServices ev = new EventoCrudServices();
        try {
            if (map.get("nombre") == null || map.get("nombre").toString().isEmpty()) {
                throw new IllegalArgumentException("El nombre es obligatorio.");
            }
            if (map.get("costoEstimadoInicial") == null || Double.parseDouble(map.get("costoEstimadoInicial").toString()) < 0) {
                throw new IllegalArgumentException("El costo estimado inicial debe ser un numero positivo.");
            }
            if (map.get("inversion") == null || Double.parseDouble(map.get("inversion").toString()) < 0) {
                throw new IllegalArgumentException("La inversion debe ser un numero positivo.");
            }
            if (map.get("fechaInicio") == null || map.get("fechaInicio").toString().isEmpty()) {
                throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
            }
            if (map.get("tiempoDeVida") == null || Integer.parseInt(map.get("tiempoDeVida").toString()) <= 0) {
                throw new IllegalArgumentException("El tiempo de vida debe ser un numero entero positivo.");
            }

            ps.getProyecto().setId(id);
            ps.getProyecto().setNombre(map.get("nombre").toString());
            ps.getProyecto().setCostoEstimadoInicial(Double.parseDouble(map.get("costoEstimadoInicial").toString()));
            ps.getProyecto().setInversion(Double.parseDouble(map.get("inversion").toString()));
            ps.getProyecto().setFechaInicio(map.get("fechaInicio").toString());
            ps.getProyecto().setTiempoDeVida(Integer.valueOf(map.get("tiempoDeVida").toString()));
            ps.getProyecto().setCapacidad(map.get("capacidad") != null ? Integer.valueOf(map.get("capacidad").toString()) : 0);
            ps.getProyecto().setTipoEnergia(ps.getTipoEnergia(map.get("tipoEnergia").toString()));
            ps.getProyecto().setUbicacion(ps.getProvincia(map.get("ubicacion").toString()));
            ps.getProyecto().setDescripcion(map.get("descripcion").toString());
            ps.getProyecto().setEstado(ps.getEstado(map.get("estado").toString()));

            ps.update();
            res.put("msg", "OK");
            res.put("data", ps.toJson() + " Actualizado con exito");
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

}