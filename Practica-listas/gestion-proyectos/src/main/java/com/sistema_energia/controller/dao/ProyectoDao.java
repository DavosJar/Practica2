package com.sistema_energia.controller.dao;

import java.lang.reflect.Method;

import com.google.gson.Gson;
import com.sistema_energia.controller.dao.implement.AdapterDao;
import com.sistema_energia.controller.dao.implement.Contador;
import com.sistema_energia.controller.model.Estado;
import com.sistema_energia.controller.model.Provincia;
import com.sistema_energia.controller.model.Proyecto;
import com.sistema_energia.controller.model.TipoEnergia;
import com.sistema_energia.controller.tda.list.LinkedList;

@SuppressWarnings({ "unchecked", "ConvertToTryWithResources" })
public class ProyectoDao extends AdapterDao<Proyecto> {
    private Proyecto proyecto;
    private LinkedList<Proyecto> listAll;

    public ProyectoDao() {
        super(Proyecto.class);
    }

    public Proyecto getProyecto() {
        if (this.proyecto == null) {
            this.proyecto = new Proyecto();
        }
        return this.proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public LinkedList<Proyecto> getListAll() throws Exception {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public boolean save() throws Exception {
        Integer id = Contador.obtenerValorActual(Proyecto.class);
        try {
            this.proyecto.setId(id);
            this.persist(this.proyecto);
            Contador.actualizarContador(Proyecto.class);
            this.listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al guardar el proyecto: " + e.getMessage());
        }
    }

    public Boolean update() throws Exception {
        if (this.proyecto == null || this.proyecto.getId() == null) {
            throw new Exception("No se ha seleccionado un proyecto para actualizar.");
        }
        if (listAll == null) {
            listAll = listAll();
        }
        Integer index = getProyectoIndex("id", this.proyecto.getId());
        if (index == -1) {
            throw new Exception("Proyecto no encontrado.");
        }
        try {
            this.merge(this.proyecto, index);
            listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al actualizar el proyecto: " + e.getMessage());
        }
    }

    public Boolean delete() throws Exception {
        if (this.proyecto == null || this.proyecto.getId() == null) {
            throw new Exception("No se ha seleccionado un proyecto para eliminar.");
        }
        if (listAll == null) {
            listAll = listAll();
        }
        Integer index = getProyectoIndex("id", this.proyecto.getId());
        if (index == -1) {
            throw new Exception("Proyecto no encontrado.");
        }
        try {
            this.delete(index);
            listAll = listAll();
            return true;
        } catch (Exception e) {
            throw new Exception("Error al eliminar el proyecto: " + e.getMessage());
        }
    }

    private LinkedList<Proyecto> linearBinarySearch(String attribute, Object value) throws Exception {
        LinkedList<Proyecto> lista = this.listAll().quickSort(attribute, 1);
        LinkedList<Proyecto> proyectos = new LinkedList<>();
        if (!lista.isEmpty()) {
            Proyecto[] aux = lista.toArray();
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
                return proyectos;
            }

            Integer i = index;
            while (i < aux.length
                    && obtenerAttributeValue(aux[i], attribute).toString().toLowerCase().startsWith(searchValue)) {
                proyectos.add(aux[i]);
                System.out.println("Agregando: " + aux[i].getNombre());
                i++;
            }
        }
        return proyectos;
    }

    public LinkedList<Proyecto> buscarLista(String attribute, Object value) throws Exception {
        return linearBinarySearch(attribute, value);
    }

    public Proyecto buscarObjeto(String attribute, Object value) throws Exception {
        return binarySearch(attribute, value);
    }

    private Proyecto binarySearch(String attribute, Object value) throws Exception {
        LinkedList<Proyecto> lista = listAll().quickSort(attribute, 1);

        if (!lista.isEmpty()) {
            Proyecto[] proyectos = lista.toArray();
            Integer inicio = 0;
            Integer fin = proyectos.length - 1;
            Integer medio;
            while (inicio <= fin) {
                medio = (inicio + fin) / 2;
                String midValue = obtenerAttributeValue(proyectos[medio], attribute).toString().toLowerCase();
                if (midValue.equals(value.toString().toLowerCase())) {
                    proyecto = proyectos[medio];
                    break;
                } else if (midValue.compareToIgnoreCase(value.toString().toLowerCase()) < 0) {
                    inicio = medio + 1;
                } else {
                    fin = medio - 1;
                }
            }
            return proyecto;
        }
        return proyecto;
    }

    private Integer getProyectoIndex(String attribute, Object value) throws Exception {
        if (this.listAll == null) {
            this.listAll = listAll();
        }
        Integer index = -1;
        if (!this.listAll.isEmpty()) {
            Proyecto[] proyectos = this.listAll.toArray();
            for (int i = 0; i < proyectos.length; i++) {
                if (obtenerAttributeValue(proyectos[i], attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public String[] getProyectoAttributeLists() {
        LinkedList<String> attributes = new LinkedList<>();
        for (Method m : Proyecto.class.getDeclaredMethods()) {
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

    public LinkedList<Proyecto> selectOrder(String attribute, Integer order, String method) throws Exception {
        LinkedList<Proyecto> lista = listAll();
        if (!lista.isEmpty()) {
            switch (method) {
                case "merge":
                    return lista.mergeSort(attribute, order);
                case "quick":
                    return lista.quickSort(attribute, order);
                case "shell":
                    return lista.shellSort(attribute, order);
                default:
                    return lista;
            }
        }
        return lista;
    }

    public Proyecto getProyectoById(Integer id) throws Exception {
        return get(id);
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

    public String getProyectoJson(Integer Index) throws Exception {
        Gson g = new Gson();
        return g.toJson(get(Index));
    }
}
