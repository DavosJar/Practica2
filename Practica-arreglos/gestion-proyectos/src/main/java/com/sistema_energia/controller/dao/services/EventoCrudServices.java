package com.sistema_energia.controller.dao.services;

import com.sistema_energia.controller.dao.EventoCrudDao;
import com.sistema_energia.eventos.EventoCrud;
import com.sistema_energia.eventos.TipoCrud;

public class EventoCrudServices {
    private EventoCrudDao obj;

    public EventoCrudServices() {
        obj = new EventoCrudDao();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean registrarEvento(TipoCrud tipo, String mensaje) throws Exception {
        return obj.registrarEvento(tipo,  mensaje);
    }

    public EventoCrud[] listAll() throws Exception {
        return obj.getAllEventosCrud();
    }

    public void setEventoCrud(EventoCrud eventoCrud) {
        obj.setEventoCrud(eventoCrud);
    }

    public String toJson() throws Exception {
        return obj.toJson();
    }

    public EventoCrud getEventoCrud() {
        return obj.getEventoCrud();
    }


    public EventoCrud getEventoCrudById(Integer id) throws Exception {
        return obj.getEventoCrudById(id);
    }

    public String getEventoCrudJsonById(Integer id) throws Exception {
        return obj.getEventoCrudJsonById(id);
    }

    public EventoCrud[] getListAll() {
        return obj.getListAll();
    }

    public void setListAll(EventoCrud[] listAll) {
        obj.setListAll(listAll);
    }
}
