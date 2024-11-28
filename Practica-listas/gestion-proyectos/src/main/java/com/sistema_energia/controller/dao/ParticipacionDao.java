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

@SuppressWarnings({ "unchecked", "ConvertToTryWithResources", "FieldMayBeFinal" })
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

    /*
     * private void actualizarInversionParticipacion(Double nuevoMontoInvertido)
     * throws
     * Exception {
     * if (this.participacion == null) {
     * throw new
     * Exception("No hay un participacion actual para actualizar la inversión.");
     * }
     * this.participacion.setInversion(this.participacion.getInversion() +
     * nuevoMontoInvertido);
     * this.merge(this.participacion, this.participacion.getId() - 1);
     * }
     * 
     * // Metodo que guarda un inversionistaa en un participacion
     * public boolean saveInversionistaaParticipacion(Inversionistaa inversionistaa,
     * Integer
     * participacionId) throws Exception {
     * Participacion participacionExistente =
     * getParticipacionByIndex(participacionId);
     * if (participacionExistente == null) {
     * throw new Exception("Participacion con ID " + participacionId +
     * " no existe.");
     * }
     * 
     * this.participacion = participacionExistente;
     * InversionistaaServices is = new InversionistaaServices();
     * inversionistaa.setParticipacionId(participacionId);
     * actualizarInversionParticipacion(inversionistaa.getMontoInvertido());
     * 
     * is.setInversionistaa(inversionistaa);
     * is.save();
     * 
     * return true;
     * }
     * 
     * // Metodo que actualiza un inversionistaa en un participacion
     * private LinkedList<Inversionistaa> getAllInversionistaas() throws Exception {
     * if (listInversionistaas == null) {
     * InversionistaaServices is = new InversionistaaServices();
     * listInversionistaas = is.getAllInversionistaas();
     * }
     * return listInversionistaas;
     * }
     * 
     * // Metodo que obtiene los inversionistaas de un participacion
     * public LinkedList getInversionistaasByParticipacionId(Integer
     * participacionId)
     * throws
     * Exception {
     * Inversionistaa[] allInversionistaas = getAllInversionistaas().toArray();
     * 
     * LinkedList inversionistaasAsociados = new LinkedList<>();
     * for (Inversionistaa inversionistaa : allInversionistaas) {
     * if (inversionistaa.getParticipacionId() == participacionId) {
     * inversionistaasAsociados.add(inversionistaa);
     * }
     * }
     * return inversionistaasAsociados;
     * }
     */
    public Double getPorcentaje() throws Exception {
        ProyectoServices ps = new ProyectoServices();
        Double monto = this.participacion.getMontoInvertido();
        Double porcentaje = (monto * 100) / ps.getProyectoById(this.participacion.getIdProyecto()).getInversion();
        return Math.round(porcentaje * 100.0) / 100.0;
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
                attributes.add(m.getName().substring(3).toLowerCase());
            }
        }
        return attributes.toArray();
    }

    public LinkedList<Participacion> orderList(String attribute, Integer order) throws Exception {
        LinkedList<Participacion> lista = listAll();

        if (!lista.isEmpty()) {
            lista.order(attribute, order);
        }
        return lista;
    }

    private Object obtenerAttributeValue(Object object, String attribute) throws Exception {
        Method method = object.getClass()
                .getMethod("get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1));
        return method.invoke(object);
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

    public Participacion getParticipacionB(Integer Index) throws Exception {
        return get(Index);
    }

    public String getParticipacionJson(Integer Index) throws Exception {
        return g.toJson(get(Index));
    }
}
