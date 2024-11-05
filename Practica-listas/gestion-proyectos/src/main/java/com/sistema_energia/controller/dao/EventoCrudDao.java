package com.sistema_energia.controller.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.sistema_energia.controller.dao.implement.AdapterDao;
import com.sistema_energia.controller.tda.list.LinkedList;
import com.sistema_energia.eventos.EventoCrud;
import com.sistema_energia.eventos.TipoCrud;


public class EventoCrudDao extends AdapterDao<EventoCrud>{
    private EventoCrud eventoCrud;
    private LinkedList<EventoCrud> listAll;

    public void setListAll(LinkedList<EventoCrud> listAll) {
        this.listAll = listAll;
    }

    private Gson g = new Gson();

    public EventoCrudDao() {
        super(EventoCrud.class);
    }

    public EventoCrud getEventoCrud() {
        if (this.eventoCrud == null) {
            this.eventoCrud = new EventoCrud();
        }
        return this.eventoCrud;
    }

    public void setEventoCrud(EventoCrud eventoCrud) {
        this.eventoCrud = eventoCrud;
    }

    public LinkedList<EventoCrud> getAllEventosCrud() throws Exception {
        if (listAll == null) {
            listAll = listAll();
        }
        return listAll;
    }

    public Boolean save() throws Exception {
        Integer id = getAllEventosCrud().getSize() + 1;
        getEventoCrud().setId(id);
        this.persist(this.eventoCrud);
        listAll = null;
        return true;
    }

    public Boolean registrarEvento(TipoCrud tipo,  String mensaje) throws Exception {
        String horaFecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        EventoCrud evento = new EventoCrud(tipo, mensaje, horaFecha);
        setEventoCrud(evento);
        return save();
    }
    public String toJson() throws Exception {
        return g.toJson(this.eventoCrud);
    }

    public EventoCrud getEventoCrudById(Integer id) throws Exception {
        return get(id);
    }

    public String getEventoCrudJsonById(Integer id) throws Exception {
        return g.toJson(getEventoCrudById(id));

    }

    
    public EventoCrud getEventoCrudByIndex(Integer index) throws Exception {
        return get(index);
    }

    public String getEventoCrudJsonByIndex(Integer index) throws Exception {
        return g.toJson(get(index));
    }
    

    public TipoCrud getTipoEvento(String tipoEvento) {
        return TipoCrud.valueOf(tipoEvento);
    }

    public TipoCrud[] getTipoEvento() {
        return TipoCrud.values();
    }

    
}
