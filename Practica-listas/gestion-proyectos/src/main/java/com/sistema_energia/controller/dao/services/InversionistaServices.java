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
    
    public Boolean save() throws Exception {
        return obj.save();
    }

    public LinkedList getAllInversionistas() throws Exception {
        return obj.getAllInversionistas();
    }
    public void setInversionista(Inversionista inversionista){
        obj.setInversionista(inversionista);
    }
    public String toJson() throws Exception {
        return obj.toJson();
    }
    public Inversionista getInversionista(){
        return obj.getInversionista();
    }

    public Inversionista getInversionistaById(Integer id)throws  Exception{
        return obj.getInversionistaById(id);
    }
    public String getInversionistaJsonById(Integer id) throws Exception{
        return obj.getInversionistaJsonById(id);
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
