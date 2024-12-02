package com.sistema_energia.controller.dao;

import java.lang.reflect.Method;

import com.google.gson.Gson;
import com.sistema_energia.controller.dao.implement.AdapterDao;
import com.sistema_energia.controller.dao.implement.Contador;
import com.sistema_energia.controller.dao.services.ProyectoServices;
import com.sistema_energia.controller.model.Estado;
import com.sistema_energia.controller.model.Participacion;
import com.sistema_energia.controller.model.Provincia;
import com.sistema_energia.controller.model.TipoEnergia;
import com.sistema_energia.controller.tda.list.LinkedList;

@SuppressWarnings({ "unchecked", "ConvertToTryWithResources" })
public class ParticipacionDao extends AdapterDao<Participacion> {
    private Participacion participacion;
    private LinkedList<Participacion> listAll;

    private Gson g = new Gson();

    public ParticipacionDao() {
        super(Participacion.class);
    }

    public Participacion getParticipacion() {
        if (this.participacion == null) {
            this.participacion = new Participacion();
        }
        return this.participacion;
    }

    public void setParticipacion(Participacion participacion) {
        this.participacion = participacion;
    }

    public LinkedList<Participacion> getListAll() throws Exception {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public boolean save() throws Exception {
        Integer id = Contador.obtenerValorActual(Participacion.class);
        try {
            this.participacion.setId(id);
            this.persist(this.participacion);
            Contador.actualizarContador(Participacion.class);
            this.listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al guardar el participacion: " + e.getMessage());
        }
    }

    public Boolean update() throws Exception {
        if (this.participacion == null || this.participacion.getId() == null) {
            throw new Exception("No se ha seleccionado un participacion para actualizar.");
        }
        if (listAll == null) {
            listAll = listAll();
        }
        Integer index = getParticipacionIndex("id", this.participacion.getId());
        if (index == -1) {
            throw new Exception("Participacion no encontrado.");
        }
        try {
            this.merge(this.participacion, index);
            listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al actualizar el participacion: " + e.getMessage());
        }
    }

    public Boolean delete() throws Exception {
        if (this.participacion == null || this.participacion.getId() == null) {
            throw new Exception("No se ha seleccionado un participacion para eliminar.");
        }
        if (listAll == null) {
            listAll = listAll();
        }
        Integer index = getParticipacionIndex("id", this.participacion.getId());
        if (index == -1) {
            throw new Exception("Participacion no encontrado.");
        }
        try {
            this.delete(index);
            listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al eliminar el participacion: " + e.getMessage());
        }
    }

    public LinkedList<Participacion> buscar(String attribute, Object value) throws Exception {
        LinkedList<Participacion> lista = listAll();
        LinkedList<Participacion> participacions = new LinkedList<>();

        if (!lista.isEmpty()) {
            Participacion[] participacionsArray = lista.toArray();
            for (Participacion p : participacionsArray) {
                if (obtenerAttributeValue(p, attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    participacions.add(p);
                }
            }
        }
        return participacions;
    }

    public Participacion buscarPor(String attribute, Object value) throws Exception {
        LinkedList<Participacion> lista = listAll();
        Participacion p = null;

        if (!lista.isEmpty()) {
            Participacion[] participacions = lista.toArray();
            for (int i = 0; i < participacions.length; i++) {
                if (obtenerAttributeValue(participacions[i], attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    p = participacions[i];
                    break;
                }
            }
        }
        return p;
    }

    private Integer getParticipacionIndex(String attribute, Object value) throws Exception {
        if (this.listAll == null) {
            this.listAll = listAll();
        }
        Integer index = -1;
        if (!this.listAll.isEmpty()) {
            Participacion[] participacions = this.listAll.toArray();
            for (int i = 0; i < participacions.length; i++) {
                if (obtenerAttributeValue(participacions[i], attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public String[] getParticipacionAttributeLists() {
        LinkedList<String> attributes = new LinkedList<>();
        for (Method m : Participacion.class.getDeclaredMethods()) {
            if (m.getName().startsWith("get")) {
                String attribute = m.getName().substring(3);
                if (!attribute.equalsIgnoreCase("id")) {
                    attributes.add(attribute.substring(0, 1).toLowerCase() + attribute.substring(1));
                }
            }
        }
        return attributes.toArray();
    }

    public void actualizrInversionProyecto(Integer idProyecto, Double montoInvertido) throws Exception {
        Double resultado = montoInvertido;
        ProyectoServices ps = new ProyectoServices();
        LinkedList<Participacion> lista = obtenerParticipacionesProyecto(idProyecto);
        if (!lista.isEmpty()) {
            Participacion[] participacions = lista.toArray();
            for (Participacion p : participacions) {
                resultado += p.getMontoInvertido();
            }
        }
        ps.setProyecto(ps.getProyectoById(idProyecto));
        ps.getProyecto().setInversion(resultado);

        ps.update();
    }

    public LinkedList<Participacion> obtenerParticipacionesProyecto(Integer id) {
        if (id == null) {
            return new LinkedList<>();
        }

        LinkedList<Participacion> lista = listAll();
        LinkedList<Participacion> listaFiltrada = new LinkedList<>();
        if (!lista.isEmpty()) {
            Participacion[] partList = lista.toArray();
            for (Participacion p : partList) {
                if (p.getIdProyecto().equals(id)) {
                    listaFiltrada.add(p);
                }
            }
        }
        return listaFiltrada;
    }

    public LinkedList<Participacion> obtenerParticipacionesInverionista(Integer id) {
        if (id == null) {
            return new LinkedList<>();
        }

        LinkedList<Participacion> lista = listAll();
        LinkedList<Participacion> listaFiltrada = new LinkedList<>();
        if (!lista.isEmpty()) {
            Participacion[] partList = lista.toArray();
            for (Participacion p : partList) {
                if (p.getIdInversionista().equals(id)) {
                    listaFiltrada.add(p);
                }
            }
        }
        return listaFiltrada;
    }

    public LinkedList<Participacion> orderList(String attribute, Integer order) throws Exception {
        LinkedList<Participacion> lista = listAll();

        if (!lista.isEmpty()) {
            lista.mergeSort(attribute, order);
        }
        return lista;
    }

    private Object obtenerAttributeValue(Object object, String attribute) throws Exception {
        String attr = "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);

        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(attr) && method.getParameterCount() == 0) {
                return method.invoke(object);
            }
        }

        throw new IllegalArgumentException(
                "No se encontró el atributo '" + attribute + "' en la clase " + object.getClass().getName());
    }

    public LinkedList<Participacion> selectOrder(String attribute, Integer order, String method) throws Exception {
        LinkedList<Participacion> lista = listAll();
        if (!lista.isEmpty()) {
            switch (method) {
                case "merge":
                    return lista.mergeSort(attribute, order);
                case "quick":
                    return lista.quickSort(attribute, order);
                case "shell":
                    return lista.shellSort(attribute, order);
                default:
                    throw new Exception("Metodo de ordenamiento no encontrado.");
            }
        }
        return lista;
    }

    public String toJson() throws Exception {
        return g.toJson(this.participacion);
    }

    public Participacion getParticipacionById(Integer id) throws Exception {
        return get(id);
    }

    public String getParticipacionJasonByIndex(Integer index) throws Exception {
        return g.toJson(get(index));
    }

    public TipoEnergia getTipoEnergia(String tipo) {
        return TipoEnergia.valueOf(tipo);
    }

    public TipoEnergia[] getTipoEnergia() {
        return TipoEnergia.values();
    }

    public Provincia getProvincia(String provincia) {
        return Provincia.valueOf(provincia);
    }

    public Provincia[] getProvincia() {
        return Provincia.values();
    }

    public Estado getEstado(String estado) {
        return Estado.valueOf(estado);
    }

    public Estado[] getEstado() {
        return Estado.values();
    }

    public String getParticipacionJson(Integer Index) throws Exception {
        return g.toJson(get(Index));
    }
}
