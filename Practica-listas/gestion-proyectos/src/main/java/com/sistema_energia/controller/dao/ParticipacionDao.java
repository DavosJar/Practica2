package com.sistema_energia.controller.dao;

import java.lang.reflect.Method;

import com.google.gson.Gson;
import com.sistema_energia.controller.dao.implement.AdapterDao;
import com.sistema_energia.controller.dao.implement.Contador;
import com.sistema_energia.controller.model.Participacion;
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
                "No se encontro el atributo '" + attribute + "' en la clase " + object.getClass().getName());
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

    private LinkedList<Participacion> linearBinarySearch(String attribute, Object value) throws Exception {
        LinkedList<Participacion> lista = this.listAll().quickSort(attribute, 1);
        LinkedList<Participacion> participaciones = new LinkedList<>();
        if (!lista.isEmpty()) {
            Participacion[] aux = lista.toArray();
            Integer low = 0;
            Integer high = aux.length - 1;
            Integer mid;
            Integer index = -1;
            String searchValue = value.toString().toLowerCase();
            while (low <= high) {
                mid = (low + high) / 2;

                String midValue = obtenerAttributeValue(aux[mid], attribute).toString().toLowerCase();

                if (midValue.startsWith(searchValue)) {
                    if (mid == 0 || !obtenerAttributeValue(aux[mid - 1], attribute).toString().toLowerCase()
                            .startsWith(searchValue)) {
                        index = mid;
                        break;
                    } else {
                        high = mid - 1;
                    }
                } else if (midValue.compareToIgnoreCase(searchValue) < 0) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }

            if (index.equals(-1)) {
                return participaciones;
            }

            Integer i = index;
            while (i < aux.length
                    && obtenerAttributeValue(aux[i], attribute).toString().toLowerCase().startsWith(searchValue)) {
                participaciones.add(aux[i]);
                i++;
            }
        }
        return participaciones;
    }

    public Participacion buscarObjeto(String attribute, Object value) throws Exception {
        return binarySearch(attribute, value);
    }

    private Participacion binarySearch(String attribute, Object value) throws Exception {
        LinkedList<Participacion> lista = listAll().quickSort(attribute, 1);

        if (!lista.isEmpty()) {
            Participacion[] participacions = lista.toArray();
            Integer inicio = 0;
            Integer fin = participacions.length - 1;
            Integer medio;
            while (inicio <= fin) {
                medio = (inicio + fin) / 2;
                String midValue = obtenerAttributeValue(participacions[medio], attribute).toString().toLowerCase();
                if (midValue.equals(value.toString().toLowerCase())) {
                    participacion = participacions[medio];
                    break;
                } else if (midValue.compareToIgnoreCase(value.toString().toLowerCase()) < 0) {
                    inicio = medio + 1;
                } else {
                    fin = medio - 1;
                }
            }
            return participacion;
        }
        return participacion;
    }

    public LinkedList<Participacion> buscar(String attribute, Object value) throws Exception {
        return linearBinarySearch(attribute, value);
    }

    public Participacion getParticipacionById(Integer id) throws Exception {
        return get(id);
    }

}
