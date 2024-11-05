

package com.sistema_energia.controller.dao;


import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sistema_energia.controller.dao.implement.AdapterDao;
import com.sistema_energia.controller.model.Inversionista;
import com.sistema_energia.controller.model.Provincia;
import com.sistema_energia.controller.model.Sector;

public class InversionistaDao extends AdapterDao<Inversionista> {
    private Inversionista inversionista;
    private Inversionista[] listAll;
    private Gson g = new Gson(); 

    public InversionistaDao() {
        super(Inversionista.class);
    }

    public Inversionista getInversionista(){
        if(this.inversionista == null){
            this.inversionista = new Inversionista();
        }
        return this.inversionista;
    }

    public void setInversionista(Inversionista inversionista){
        this.inversionista = inversionista;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public Inversionista[] getAllInversionistas() throws Exception{
        try {
            Inversionista[] listAll = listAll();
            if(listAll.length == 0){
                throw new Exception("Error: No hay inversionistas Registrados.");
            }
            return listAll; 
        } catch (IOException e) {
            e.printStackTrace();
            return new Inversionista[0];
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return new Inversionista[0];
        } catch (Exception e) {
            e.printStackTrace();
            return new Inversionista[0];
        }
    }

    public Boolean save() throws Exception{
        Integer id = getAllInversionistas().length + 1;
        getInversionista().setId(id);
        this.persist(this.inversionista);
        listAll = null;
        return true;
    }
    
    public String toJson() throws Exception{
        return g.toJson(this.inversionista);
    }
    public Inversionista getInversionistaById(Integer id) throws Exception{
        return get(id);
    }
    public String getInversionistaJsonById(Integer id) throws Exception{
        return g.toJson(getInversionistaById(id));
    } 
    
    public Provincia getProvincia(String provincia){
        return Provincia.valueOf(provincia);
    }
    public Provincia[] getProvincia(){
        return Provincia.values();
    }
    public Sector getSector(String sector){
        return Sector.valueOf(sector);
    }
    public Sector[] getSector(){
        return Sector.values();
    }

    public Inversionista[] getListAll() {
        return listAll;
    }
    public void setListAll(Inversionista[] listAll) {
        this.listAll = listAll;
    }

}
