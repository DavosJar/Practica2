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

    private Gson g = new Gson();

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

    public LinkedList<Inversionista> buscar(String attribute, Object value) throws Exception {
        LinkedList<Inversionista> lista = listAll();
        LinkedList<Inversionista> inversionistas = new LinkedList<>();

        if (!lista.isEmpty()) {
            Inversionista[] inversionistasArray = lista.toArray();
            for (Inversionista p : inversionistasArray) {
                if (obtenerAttributeValue(p, attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    inversionistas.add(p);
                }
            }
        }
        return inversionistas;
    }

    public Inversionista buscarPor(String attribute, Object value) throws Exception {
        LinkedList<Inversionista> lista = listAll();
        Inversionista p = null;

        if (!lista.isEmpty()) {
            Inversionista[] inversionistas = lista.toArray();
            for (int i = 0; i < inversionistas.length; i++) {
                if (obtenerAttributeValue(inversionistas[i], attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    p = inversionistas[i];
                    break;
                }
            }
        }
        return p;
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
                if (!attribute.equalsIgnoreCase("id")) {
                    attributes.add(attribute.substring(0, 1).toLowerCase() + attribute.substring(1));
                }
            }
        }
        return attributes.toArray();
    }

    public LinkedList<Inversionista> orderList(String attribute, Integer order) throws Exception {
        LinkedList<Inversionista> lista = listAll();

        if (!lista.isEmpty()) {
            lista.mergeSort(attribute, order);
        }
        return lista;
    }

    private Object obtenerAttributeValue(Object object, String attribute) throws Exception {
        Method method = object.getClass()
                .getMethod("get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1));
        return method.invoke(object);
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
        return g.toJson(this.inversionista);
    }

    public Inversionista getInversionistaById(Integer id) throws Exception {
        return get(id);
    }

    public String getInversionistaJasonByIndex(Integer index) throws Exception {
        return g.toJson(get(index));
    }

    public Provincia getProvincia(String provincia) {
        return Provincia.valueOf(provincia);
    }

    public Provincia[] getProvincia() {
        return Provincia.values();
    }

    public String getInversionistaJson(Integer Index) throws Exception {
        return g.toJson(get(Index));
    }

    public Sector getSector(String sector) {
        return Sector.valueOf(sector);
    }

    public Sector[] getSector() {
        return Sector.values();
    }
}
