package com.sistema_energia.controller.dao;

import java.lang.reflect.Method;

import com.google.gson.Gson;
import com.sistema_energia.controller.dao.implement.AdapterDao;
import com.sistema_energia.controller.dao.implement.Contador;
import com.sistema_energia.controller.model.Inversionista;
import com.sistema_energia.controller.model.Provincia;
import com.sistema_energia.controller.model.Sector;
import com.sistema_energia.controller.tda.list.LinkedList;

@SuppressWarnings({ "unchecked", "ConvertToTryWithResources" })
public class InversionistaDao extends AdapterDao<Inversionista> {
    private Inversionista inversionista;
    private LinkedList<Inversionista> listAll;

    public InversionistaDao() {
        super(Inversionista.class);
    }

    public Inversionista getInversionista() {
        if (this.inversionista == null) {
            this.inversionista = new Inversionista();
        }
        return this.inversionista;
    }

    public void setInversionista(Inversionista inversionista) {
        this.inversionista = inversionista;
    }

    public LinkedList<Inversionista> getListAll() throws Exception {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public boolean save() throws Exception {
        Integer id = Contador.obtenerValorActual(Inversionista.class);
        try {
            this.inversionista.setId(id);
            this.persist(this.inversionista);
            Contador.actualizarContador(Inversionista.class);
            this.listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al guardar el inversionista: " + e.getMessage());
        }
    }

    public Boolean update() throws Exception {
        if (this.inversionista == null || this.inversionista.getId() == null) {
            throw new Exception("No se ha seleccionado un inversionista para actualizar.");
        }
        if (listAll == null) {
            listAll = listAll();
        }
        Integer index = getInversionistaIndex("id", this.inversionista.getId());
        if (index == -1) {
            throw new Exception("Inversionista no encontrado.");
        }
        try {
            this.merge(this.inversionista, index);
            listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al actualizar el inversionista: " + e.getMessage());
        }
    }

    public Boolean delete() throws Exception {
        if (this.inversionista == null || this.inversionista.getId() == null) {
            throw new Exception("No se ha seleccionado un inversionista para eliminar.");
        }
        if (listAll == null) {
            listAll = listAll();
        }
        Integer index = getInversionistaIndex("id", this.inversionista.getId());
        if (index == -1) {
            throw new Exception("Inversionista no encontrado.");
        }
        try {
            this.delete(index);
            listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al eliminar el inversionista: " + e.getMessage());
        }
    }

    private LinkedList<Inversionista> linearBinarySearch(String attribute, Object value) throws Exception {
        LinkedList<Inversionista> lista = this.listAll().quickSort(attribute, 1);
        LinkedList<Inversionista> inversionistas = new LinkedList<>();
        if (!lista.isEmpty()) {
            Inversionista[] aux = lista.toArray();
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
                return inversionistas;
            }

            Integer i = index;
            while (i < aux.length
                    && obtenerAttributeValue(aux[i], attribute).toString().toLowerCase().startsWith(searchValue)) {
                inversionistas.add(aux[i]);
                System.out.println("Agregando: " + aux[i].getNombre());
                i++;
            }
        }
        return inversionistas;
    }

    public LinkedList<Inversionista> buscarLista(String attribute, Object value) throws Exception {
        return linearBinarySearch(attribute, value);
    }

    public Inversionista buscarObjeto(String attribute, Object value) throws Exception {
        return binarySearch(attribute, value);
    }

    private Inversionista binarySearch(String attribute, Object value) throws Exception {
        LinkedList<Inversionista> lista = listAll().quickSort(attribute, 1);

        if (!lista.isEmpty()) {
            Inversionista[] inversionistas = lista.toArray();
            Integer inicio = 0;
            Integer fin = inversionistas.length - 1;
            Integer medio;
            while (inicio <= fin) {
                medio = (inicio + fin) / 2;
                String midValue = obtenerAttributeValue(inversionistas[medio], attribute).toString().toLowerCase();
                if (midValue.equals(value.toString().toLowerCase())) {
                    inversionista = inversionistas[medio];
                    break;
                } else if (midValue.compareToIgnoreCase(value.toString().toLowerCase()) < 0) {
                    inicio = medio + 1;
                } else {
                    fin = medio - 1;
                }
            }
            return inversionista;
        }
        return inversionista;
    }

    private Integer getInversionistaIndex(String attribute, Object value) throws Exception {
        if (this.listAll == null) {
            this.listAll = listAll();
        }
        Integer index = -1;
        if (!this.listAll.isEmpty()) {
            Inversionista[] inversionistas = this.listAll.toArray();
            for (int i = 0; i < inversionistas.length; i++) {
                if (obtenerAttributeValue(inversionistas[i], attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public String[] getInversionistaAttributeLists() {
        LinkedList<String> attributes = new LinkedList<>();
        for (Method m : Inversionista.class.getDeclaredMethods()) {
            if (m.getName().startsWith("get")) {
                String attribute = m.getName().substring(3);
                if (!attribute.equalsIgnoreCase("id") && !attribute.equalsIgnoreCase("descripcion")) {
                    attributes.add(attribute.substring(0, 1).toLowerCase() + attribute.substring(1));
                }
            }
        }
        return attributes.toArray();
    }

    private Object obtenerAttributeValue(Object object, String attribute) throws Exception {
        String clazzAtr = "get" + attribute.substring(0, 1).toUpperCase()
                + attribute.substring(1).toLowerCase();
        Method[] methods = object.getClass().getMethods();

        for (Method m : methods) {
            if (m.getName().equalsIgnoreCase(clazzAtr) && m.getParameterCount() == 0) {
                return m.invoke(object);
            }
        }

        throw new NoSuchMethodException("No se encontor el atributo: " + attribute);
    }

    public LinkedList<Inversionista> selectOrder(String attribute, Integer order, String method) throws Exception {
        LinkedList<Inversionista> lista = listAll();
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
        Gson g = new Gson();
        return g.toJson(this.inversionista);
    }

    public Inversionista getInversionistaById(Integer id) throws Exception {
        return get(id);
    }

    public String getInversionistaJasonByIndex(Integer index) throws Exception {
        Gson g = new Gson();
        return g.toJson(get(index));
    }

    public Provincia getProvincia(String provincia) {
        return Provincia.valueOf(provincia);
    }

    public Provincia[] getProvincia() {
        return Provincia.values();
    }

    public String getInversionistaJson(Integer Index) throws Exception {
        Gson g = new Gson();
        return g.toJson(get(Index));
    }

    public Sector getSector(String sector) {
        return Sector.valueOf(sector);
    }

    public Sector[] getSector() {
        return Sector.values();
    }
}
