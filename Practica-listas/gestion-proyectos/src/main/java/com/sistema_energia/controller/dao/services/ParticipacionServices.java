package com.sistema_energia.controller.dao.services;

import com.sistema_energia.controller.dao.ParticipacionDao;
import com.sistema_energia.controller.model.Estado;
import com.sistema_energia.controller.model.Participacion;
import com.sistema_energia.controller.model.Provincia;
import com.sistema_energia.controller.model.TipoEnergia;
import com.sistema_energia.controller.tda.list.LinkedList;

public class ParticipacionServices {

    @SuppressWarnings("FieldMayBeFinal")
    private ParticipacionDao obj;

    public ParticipacionServices() {
        obj = new ParticipacionDao();
    }

    public Participacion getParticipacion() {
        return obj.getParticipacion();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public LinkedList<Participacion> listAll() throws Exception {
        return obj.getListAll();
    }

    public void setParticipacion(Participacion participacion) {
        obj.setParticipacion(participacion);
    }

    public Participacion getParticipacionById(Integer id) throws Exception {
        return obj.getParticipacionById(id);

    }

    public String toJson() throws Exception {
        return obj.toJson();

    }

    public LinkedList<Participacion> getParticipacionsBy(String atributo, Object valor) throws Exception {
        return obj.buscar(atributo, valor);
    }

    public LinkedList<Participacion> orderListBy(String atributo, Integer orden) throws Exception {
        return obj.orderList(atributo, orden);
    }

    public Participacion obtenerParticipacionPor(String atributo, Object valor) throws Exception {
        return obj.buscarPor(atributo, valor);
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public LinkedList<Participacion> getParticipacionBy(String atributo, Object valor) throws Exception {
        return obj.buscar(atributo, valor);
    }

    public Participacion buscarPor(String atributo, Object valor) throws Exception {
        return obj.buscarPor(atributo, valor);
    }

    public LinkedList<Participacion> obtenerParicipacionesPorProyectoId(Integer id) throws Exception {
        return obj.obtenerParticipacionesProyecto(id);
    }

    public LinkedList<Participacion> obtenerParicipacionesPorInversionistaId(Integer id) throws Exception {
        return obj.obtenerParticipacionesInverionista(id);
    }

    public Double obtenerPorcentajeParticipacion() throws Exception {
        return obj.getPorcentaje();
    }

    public TipoEnergia getTipoEnergia(String tipo) {
        return obj.getTipoEnergia(tipo);
    }

    public TipoEnergia[] getTipoEnergia() throws Exception {
        return obj.getTipoEnergia();
    }

    public Provincia getProvincia(String provincia) throws Exception {
        return obj.getProvincia(provincia);
    }

    public Provincia[] getProvincia() throws Exception {
        return obj.getProvincia();
    }

    public Estado getEstado(String estado) throws Exception {
        return obj.getEstado(estado);
    }

    public Estado[] getEstado() throws Exception {
        return obj.getEstado();
    }

    public String[] getParticipacionAttributeLists() {
        return obj.getParticipacionAttributeLists();
    }
}
