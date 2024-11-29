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

    private Gson g = new Gson();

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

    public void actualizarMontoInversion(Integer id, Double monto) throws Exception {
        Proyecto p = getProyectoById(id);
        Double inversionAcumulada = p.getInversion();
        inversionAcumulada += monto;
        p.setInversion(inversionAcumulada);
        Integer index = getProyectoIndex("id", id);
        merge(p, index);
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

    public LinkedList<Proyecto> buscar(String attribute, Object value) throws Exception {
        LinkedList<Proyecto> lista = listAll();
        LinkedList<Proyecto> proyectos = new LinkedList<>();

        if (!lista.isEmpty()) {
            Proyecto[] proyectosArray = lista.toArray();
            for (Proyecto p : proyectosArray) {
                if (obtenerAttributeValue(p, attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    proyectos.add(p);
                }
            }
        }
        return proyectos;
    }

    public Proyecto buscarPor(String attribute, Object value) throws Exception {
        LinkedList<Proyecto> lista = listAll();
        Proyecto p = null;

        if (!lista.isEmpty()) {
            Proyecto[] proyectos = lista.toArray();
            for (int i = 0; i < proyectos.length; i++) {
                if (obtenerAttributeValue(proyectos[i], attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    p = proyectos[i];
                    break;
                }
            }
        }
        return p;
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
                attributes.add(m.getName().substring(3).toLowerCase());
            }
        }
        return attributes.toArray();
    }

    public LinkedList<Proyecto> orderList(String attribute, Integer order) throws Exception {
        LinkedList<Proyecto> lista = listAll();

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

    public String toJson() throws Exception {
        return g.toJson(this.proyecto);
    }

    public Proyecto getProyectoById(Integer id) throws Exception {
        return get(id);
    }

    public String getProyectoJasonByIndex(Integer index) throws Exception {
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

    public String getProyectoJson(Integer Index) throws Exception {
        return g.toJson(get(Index));
    }
}
