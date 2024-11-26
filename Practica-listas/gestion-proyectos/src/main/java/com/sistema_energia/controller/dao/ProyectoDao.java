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
    /*
     * private void actualizarInversionProyecto(Double nuevoMontoInvertido) throws
     * Exception {
     * if (this.proyecto == null) {
     * throw new
     * Exception("No hay un proyecto actual para actualizar la inversión.");
     * }
     * this.proyecto.setInversion(this.proyecto.getInversion() +
     * nuevoMontoInvertido);
     * this.merge(this.proyecto, this.proyecto.getId() - 1);
     * }
     * 
     * // Metodo que guarda un inversionista en un proyecto
     * public boolean saveInversionistaProyecto(Inversionista inversionista, Integer
     * proyectoId) throws Exception {
     * Proyecto proyectoExistente = getProyectoByIndex(proyectoId);
     * if (proyectoExistente == null) {
     * throw new Exception("Proyecto con ID " + proyectoId + " no existe.");
     * }
     * 
     * this.proyecto = proyectoExistente;
     * InversionistaServices is = new InversionistaServices();
     * inversionista.setProyectoId(proyectoId);
     * actualizarInversionProyecto(inversionista.getMontoInvertido());
     * 
     * is.setInversionista(inversionista);
     * is.save();
     * 
     * return true;
     * }
     * 
     * // Metodo que actualiza un inversionista en un proyecto
     * private LinkedList<Inversionista> getAllInversionistas() throws Exception {
     * if (listInversionistas == null) {
     * InversionistaServices is = new InversionistaServices();
     * listInversionistas = is.getAllInversionistas();
     * }
     * return listInversionistas;
     * }
     * 
     * // Metodo que obtiene los inversionistas de un proyecto
     * public LinkedList getInversionistasByProyectoId(Integer proyectoId) throws
     * Exception {
     * Inversionista[] allInversionistas = getAllInversionistas().toArray();
     * 
     * LinkedList inversionistasAsociados = new LinkedList<>();
     * for (Inversionista inversionista : allInversionistas) {
     * if (inversionista.getProyectoId() == proyectoId) {
     * inversionistasAsociados.add(inversionista);
     * }
     * }
     * return inversionistasAsociados;
     * }
     */

    public Boolean update() throws Exception {
        if (this.proyecto == null || this.proyecto.getId() == null) {
            throw new Exception("No se ha seleccionado un proyecto para actualizar.");
        }
        if (listAll == null) {
            listAll = listAll();
        }
        Integer index = getPersonaIndex("id", this.proyecto.getId());
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
        Integer index = getPersonaIndex("id", this.proyecto.getId());
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
        LinkedList<Proyecto> listAll = listAll();
        LinkedList<Proyecto> proyectos = new LinkedList<>();

        if (!listAll.isEmpty()) {
            Proyecto[] proyectosArray = listAll.toArray();
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
        LinkedList<Proyecto> listAll = listAll();
        Proyecto proyecto = null;

        if (!listAll.isEmpty()) {
            Proyecto[] proyectos = listAll.toArray();
            for (int i = 0; i < proyectos.length; i++) {
                if (obtenerAttributeValue(proyectos[i], attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    proyecto = proyectos[i];
                    break;
                }
            }
        }
        return proyecto;
    }

    private Integer getPersonaIndex(String attribute, Object value) throws Exception {
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
        LinkedList<Proyecto> listAll = listAll();

        if (!listAll.isEmpty()) {
            listAll.order(attribute, order);
        }
        return listAll;
    }

    private Object obtenerAttributeValue(Object object, String attribute) throws Exception {
        Method method = object.getClass()
                .getMethod("get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1));
        return method.invoke(object);
    }

    public String toJson() throws Exception {
        return g.toJson(this.proyecto);
    }

    public Proyecto getProyectoByIndex(Integer index) throws Exception {
        return get(index);
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

    public Proyecto getProyectoB(Integer Index) throws Exception {
        return get(Index);
    }

    public String getProyectoJson(Integer Index) throws Exception {
        return g.toJson(get(Index));
    }
}
