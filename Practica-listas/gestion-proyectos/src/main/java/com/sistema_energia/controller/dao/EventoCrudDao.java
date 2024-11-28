package com.sistema_energia.controller.dao;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.sistema_energia.controller.dao.implement.AdapterDao;
import com.sistema_energia.controller.dao.implement.Contador;
import com.sistema_energia.controller.tda.list.LinkedList;
import com.sistema_energia.eventos.EventoCrud;
import com.sistema_energia.eventos.TipoCrud;

@SuppressWarnings({ "unchecked", "ConvertToTryWithResources", "FieldMayBeFinal" })
public class EventoCrudDao extends AdapterDao<EventoCrud> {
    private EventoCrud eventoCrud;
    private LinkedList<EventoCrud> listAll;
    private Gson g = new Gson();

    public void setListAll(LinkedList<EventoCrud> listAll) {
        this.listAll = listAll;
    }

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

    public LinkedList<EventoCrud> getListAll() throws Exception {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public Boolean save() throws Exception {
        int id = Contador.obtenerValorActual(EventoCrud.class);
        try {
            this.eventoCrud.setId(id);
            this.persist(this.eventoCrud);
            Contador.actualizarContador(EventoCrud.class);
            this.listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al guardar el evento: " + e.getMessage());
        }
    }

    public Boolean registrarEvento(TipoCrud tipo, String mensaje) throws Exception {
        String horaFecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        EventoCrud evento = new EventoCrud(tipo, mensaje, horaFecha);
        setEventoCrud(evento);
        return save();
    }

    public LinkedList<EventoCrud> buscar(String atributo, Object valor) throws Exception {
        if (listAll == null) {
            listAll = listAll();
        }
        LinkedList<EventoCrud> listaFiltrada = new LinkedList<>();
        if (!listAll.isEmpty()) {
            EventoCrud[] lista = listAll.toArray();
            for (EventoCrud ec : lista) {
                if (obtenerValorAtributo(ec, atributo).equals(valor)) {
                    listaFiltrada.add(ec);
                }
            }
        }
        return listaFiltrada;
    }

    public EventoCrud buscarPor(String atributo, Object valor) throws Exception {
        if (listAll == null) {
            listAll = listAll();
        }
        EventoCrud evento = null;
        if (!listAll.isEmpty()) {
            EventoCrud[] lista = listAll.toArray();
            for (EventoCrud ec : lista) {
                if (obtenerValorAtributo(ec, atributo).equals(valor)) {
                    evento = ec;
                    break;
                }
            }
        }
        return evento;
    }

    public String[] getAttributes() {
        LinkedList<String> attributes = new LinkedList<>();
        for (Method m : EventoCrud.class.getDeclaredMethods()) {
            if (m.getName().startsWith("get")) {
                attributes.add(m.getName().substring(3).toLowerCase());
            }
        }
        return attributes.toArray();
    }

    private Object obtenerValorAtributo(Object obj, String attribute) throws Exception {
        Method method = obj.getClass()
                .getMethod("get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1));
        return method.invoke(obj);
    }

    private Integer getEventoIndex(String attribute, Object value) throws Exception {
        if (listAll == null) {
            listAll = listAll();
        }
        Integer index = -1;
        if (!listAll.isEmpty()) {
            EventoCrud[] eventos = listAll.toArray();
            for (int i = 0; i < eventos.length; i++) {
                if (obtenerValorAtributo(eventos[i], attribute).equals(value)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
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
