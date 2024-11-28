package com.sistema_energia.controller.dao.services;

import com.sistema_energia.controller.dao.InversionistaDao;
import com.sistema_energia.controller.model.Inversionista;
import com.sistema_energia.controller.model.Provincia;
import com.sistema_energia.controller.model.Sector;
import com.sistema_energia.controller.tda.list.LinkedList;

public class InversionistaServices {

    @SuppressWarnings("FieldMayBeFinal")
    private InversionistaDao obj;

    public InversionistaServices() {
        obj = new InversionistaDao();
    }

    public Inversionista getInversionista() {
        return obj.getInversionista();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public LinkedList<Inversionista> listAll() throws Exception {
        return obj.getListAll();
    }

    public void setInversionista(Inversionista inversionista) {
        obj.setInversionista(inversionista);
    }

    public Inversionista getInversionistaById(Integer id) throws Exception {
        return obj.getInversionistaById(id);

    }

    /*
     * public Boolean saveInversionistaInversionista(Inversionista inversionista,
     * int
     * inversionistaId) throws Exception{
     * return obj.saveInversionistaInversionista(inversionista, inversionistaId);
     * }
     * public LinkedList<Inversionista> getInversionistasByInversionistaId(Integer
     * inversionistaId) throws Exception{
     * return obj.getInversionistasByInversionistaId(inversionistaId);
     * }
     */

    public String toJson() throws Exception {
        return obj.toJson();

    }

    // Busquedas por atributos
    public LinkedList<Inversionista> getInversionistasBy(String atributo, Object valor) throws Exception {
        return obj.buscar(atributo, valor);
    }

    public LinkedList<Inversionista> orderListBy(String atributo, Integer orden) throws Exception {
        return obj.orderList(atributo, orden);
    }

    public Inversionista obtenerInversionistaPor(String atributo, Object valor) throws Exception {
        return obj.buscarPor(atributo, valor);
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public Sector getSector(String sector) {
        return Sector.valueOf(sector);
    }

    public Sector[] getSector() {
        return Sector.values();
    }

    public Provincia getProvincia(String provincia) {
        return Provincia.valueOf(provincia);
    }

    public Provincia[] getProvincia() {
        return Provincia.values();
    }

}
