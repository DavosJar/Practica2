package com.sistema_energia.controller.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.sistema_energia.controller.dao.implement.AdapterDao;
import com.sistema_energia.eventos.EventoCrud;
import com.sistema_energia.eventos.TipoCrud;


public class EventoCrudDao extends AdapterDao<EventoCrud>{
    private EventoCrud eventoCrud;
    private EventoCrud[] listAll;
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


    public EventoCrud[] getAllEventosCrud() {
        try {
            EventoCrud[] listAll = listAll();
            if (listAll.length == 0) {
                throw new Exception("Error: No hay eventos disponibles.");
            }
            return listAll;
        } catch (Exception e) {
            e.printStackTrace();
            return new EventoCrud[0];
        }
    }

    public boolean save() throws Exception {
        Integer id = getAllEventosCrud().length + 1;
        getEventoCrud().setId(id);
        this.persist(this.eventoCrud);
        listAll = listAll();
        return true;
    }

    public boolean registrarEvento(TipoCrud tipo,  String mensaje) throws Exception {
        String Fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String Hora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String horaFecha = Fecha.concat(" --- ").concat(Hora);
        EventoCrud evento = new EventoCrud(tipo, horaFecha, mensaje);
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
    
    
    public EventoCrud[] getListAll() {
        return listAll;
    }

    public void setListAll(EventoCrud[] listAll) {
        this.listAll = listAll;
    }

    public TipoCrud getTipoEvento(String tipoEvento) {
        return TipoCrud.valueOf(tipoEvento);
    }

    public TipoCrud[] getTipoEvento() {
        return TipoCrud.values();
    }

    
}
