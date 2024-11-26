package com.sistema_energia.controller.dao.services;

import com.sistema_energia.controller.dao.ProyectoDao;
import com.sistema_energia.controller.model.Estado;
import com.sistema_energia.controller.model.Provincia;
import com.sistema_energia.controller.model.Proyecto;
import com.sistema_energia.controller.model.TipoEnergia;
import com.sistema_energia.controller.tda.list.LinkedList;

public class ProyectoServices {

    @SuppressWarnings("FieldMayBeFinal")
    private ProyectoDao obj;

    public ProyectoServices() {
        obj = new ProyectoDao();
    }

    public Proyecto getProyecto() {
        return obj.getProyecto();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public LinkedList<Proyecto> listAll() throws Exception {
        return obj.getListAll();
    }

    public void setProyecto(Proyecto proyecto) {
        obj.setProyecto(proyecto);
    }

    public Proyecto getProyectoByIndex(Integer index) throws Exception {
        return obj.getProyectoByIndex(index);

    }

    /*
     * public Boolean saveInversionistaProyecto(Inversionista inversionista, int
     * proyectoId) throws Exception{
     * return obj.saveInversionistaProyecto(inversionista, proyectoId);
     * }
     * public LinkedList<Inversionista> getInversionistasByProyectoId(Integer
     * proyectoId) throws Exception{
     * return obj.getInversionistasByProyectoId(proyectoId);
     * }
     */

    public String toJson() throws Exception {
        return obj.toJson();

    }

    // Busquedas por atributos
    public LinkedList<Proyecto> getProyectosBy(String atributo, Object valor) throws Exception {
        return obj.buscar(atributo, valor);
    }

    public LinkedList<Proyecto> orderListBy(String atributo, Integer orden) throws Exception {
        return obj.orderList(atributo, orden);
    }

    public Proyecto obtenerProyectoPor(String atributo, Object valor) throws Exception {
        return obj.buscarPor(atributo, valor);
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public TipoEnergia getTipoEnergia(String tipo) {
        return obj.getTipoEnergia(tipo);
    }

    public TipoEnergia[] getTipoEnergia() throws Exception {
        return obj.getTipoEnergia();
    }

    public Provincia getProvincia(String provincia) throws Exception {
        return obj.getProvincia(provincia);
    }

    public Provincia[] getProvincia() throws Exception {
        return obj.getProvincia();
    }

    public Estado getEstado(String estado) throws Exception {
        return obj.getEstado(estado);
    }

    public Estado[] getEstado() throws Exception {
        return obj.getEstado();
    }

    public String[] getProyectoAttributeLists() {
        return obj.getProyectoAttributeLists();
    }
}
