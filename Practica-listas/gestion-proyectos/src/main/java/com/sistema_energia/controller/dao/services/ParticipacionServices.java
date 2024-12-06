package com.sistema_energia.controller.dao.services;

import com.sistema_energia.controller.dao.ParticipacionDao;
import com.sistema_energia.controller.model.Participacion;
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

    public LinkedList<Participacion> getParticipacionsBy(String atributo, Object valor) throws Exception {
        return obj.buscar(atributo, valor);
    }

    public LinkedList<Participacion> orderListBy(String atributo, Integer orden) throws Exception {
        return obj.orderList(atributo, orden);
    }

    public LinkedList<Participacion> selectOrder(String atributo, Integer orden, String method) throws Exception {
        return obj.selectOrder(atributo, orden, method);
    }

    public Participacion obtenerParticipacionPor(String atributo, Object valor) throws Exception {
        return obj.buscarObjeto(atributo, valor);
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public String[] getParticipacionAttributeLists() {
        return obj.getParticipacionAttributeLists();
    }

    public LinkedList<Participacion> getParticipacionesByProyecto(Integer idProyecto) throws Exception {
        return obj.obtenerParticipacionesProyecto(idProyecto);
    }

    public LinkedList<Participacion> getParticipacionesByIversionista(Integer idInversionista) throws Exception {
        return obj.obtenerParticipacionesInverionista(idInversionista);
    }

}
