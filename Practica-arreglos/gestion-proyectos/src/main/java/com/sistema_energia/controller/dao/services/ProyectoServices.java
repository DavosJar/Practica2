package com.sistema_energia.controller.dao.services;

import com.sistema_energia.controller.dao.ProyectoDao;
import com.sistema_energia.controller.model.Estado;
import com.sistema_energia.controller.model.Inversionista;
import com.sistema_energia.controller.model.Provincia;
import com.sistema_energia.controller.model.Proyecto;
import com.sistema_energia.controller.model.TipoEnergia;

public class ProyectoServices {

    @SuppressWarnings("FieldMayBeFinal")
    private ProyectoDao obj;    

    public ProyectoServices() {
        obj = new ProyectoDao();
    }
    
    public Proyecto getProyecto(){
        return obj.getProyecto();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }
    public Boolean delete() throws Exception{
        return obj.delete();
    }
    public Proyecto[] listAll() throws Exception {
        return obj.getAllProyectos();
    }
    public void setProyecto(Proyecto proyecto) {
        obj.setProyecto(proyecto);
    }
    public Proyecto getProyectoByIndex(Integer index)throws Exception{
        return obj.getProyectoByIndex(index);

    }
    public Boolean saveInversionistaProyecto(Inversionista inversionista, int proyectoId) throws Exception{
        return obj.saveInversionistaProyecto(inversionista, proyectoId);
    }
    public String toJson()throws Exception{
        return obj.toJson();
        
    }
    public Boolean existe(){
        return obj.existe();
    }
    
    public Boolean existeEnProyecto() throws Exception{
        return obj.existeEnProyecto();
    }
    public Boolean update() throws Exception{
        return obj.update();
    }
    public Inversionista[] getInversionistasByProyectoId(int proyectoId) throws Exception{
        return obj.getInversionistasByProyectoId(proyectoId);
    }
    public TipoEnergia getTipoEnergia(String tipo){
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
    
}
