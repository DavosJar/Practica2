package com.sistema_energia.controller.dao;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sistema_energia.controller.dao.implement.AdapterDao;
import com.sistema_energia.controller.dao.services.InversionistaServices;
import com.sistema_energia.controller.model.Estado;
import com.sistema_energia.controller.model.Inversionista;
import com.sistema_energia.controller.model.Provincia;
import com.sistema_energia.controller.model.Proyecto;
import com.sistema_energia.controller.model.TipoEnergia;

public class ProyectoDao extends AdapterDao<Proyecto> {
    private Proyecto proyecto;
    private Proyecto[] listAll; 
    private Inversionista[] allInversionistas;
    private Gson g = new Gson();
    //Constructor de la clase ProyectoDao que hereda de AdapterDao con el tipo Proyecto
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
    
    public Proyecto[] getAllProyectos() {
        try {
            Proyecto[] listAll = listAll(); 
            if (listAll.length == 0) {
                throw new Exception("Error: No hay proyectos disponibles."); 
            }
            return listAll;
        } catch (IOException e) {
            e.printStackTrace();
            return new Proyecto[0]; 
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return new Proyecto[0]; 
        } catch (Exception e) {
            e.printStackTrace();
            return new Proyecto[0]; 
        }
    }

    public boolean save() throws Exception {
        Integer id = getAllProyectos().length + 1; 
        getProyecto().setId(id);
        this.persist(this.proyecto); 
        listAll = null; 
        return true; 
    }

    private void actualizarInversionProyecto(Double nuevoMontoInvertido) throws Exception {
        if (this.proyecto == null) {
            throw new Exception("No hay un proyecto actual para actualizar la inversión.");
        }
        this.proyecto.setInversion(this.proyecto.getInversion() + nuevoMontoInvertido);
        this.merge(this.proyecto, this.proyecto.getId() - 1);
    }

    public boolean saveInversionistaProyecto(Inversionista inversionista, Integer proyectoId) throws Exception {
        Proyecto proyectoExistente = getProyectoByIndex(proyectoId); 
        if (proyectoExistente == null) {
            throw new Exception("Proyecto con ID " + proyectoId + " no existe.");
        }
        
        this.proyecto = proyectoExistente; 
        InversionistaServices is = new InversionistaServices();
        inversionista.setProyectoId(proyectoId);
        actualizarInversionProyecto(inversionista.getMontoInvertido());

        is.setInversionista(inversionista);
        is.save();
        
        return true;
    }

    private Inversionista[] getAllInversionistas() throws Exception {
        if (allInversionistas == null) {
            InversionistaServices in = new InversionistaServices(); 
            allInversionistas = in.listAll(); 
        }
        return allInversionistas; 
    }
    
    public Inversionista[] getInversionistasByProyectoId(Integer proyectoId) throws Exception {
        Inversionista[] allInversionistas = getAllInversionistas();
        
        Inversionista[] inversionistasAsociados = new Inversionista[allInversionistas.length];
        int count = 0; 
    
        for (Inversionista inversionista : allInversionistas) {
            if (inversionista.getProyectoId() == proyectoId) { 
                inversionistasAsociados[count] = inversionista; 
                count++; 
            }
        }
        Inversionista[] result = new Inversionista[count];
        System.arraycopy(inversionistasAsociados, 0, result, 0, count);
        
        return result;
    }

    public Boolean existe(){
        Proyecto[] proyectos = getAllProyectos();
        for (Proyecto proyecto : proyectos) {
            if(proyecto.getNombre().equals(this.proyecto.getNombre())){
                return true;
            }
        }
        return false;
    }
    public Boolean existeEnProyecto() throws Exception{
        Inversionista[] inversionistas = getAllInversionistas();
        for (Inversionista inversionista : inversionistas) {
            if(inversionista.getProyectoId() == this.proyecto.getId()){
                return true;
            }
        }
        return false;
    }
    
    
    public Boolean update () throws Exception {
        this.merge(this.proyecto, this.proyecto.getId() - 1); 
        listAll = listAll(); 
        return true; 
    }

    public Boolean delete() throws Exception {
        if (listAll == null) {
            listAll = getAllProyectos(); 
        }
        int indexToDelete = this.proyecto.getId() - 1;
        if (indexToDelete < 0 || indexToDelete >= listAll.length) {
            throw new Exception("Indice fuera de trango"); 
        }
        this.delete(indexToDelete);
        reindex();
        return true;
    }

    private void reindex() throws Exception {
        Proyecto[] proyectos = getAllProyectos(); 
        for (int i = 0; i < proyectos.length; i++) {
            proyectos[i].setId(i + 1); 
            this.merge(proyectos[i], i); 
        }
    }
    public String toJson()throws Exception{
        return g.toJson(this.proyecto);
    }
    public Proyecto getProyectoByIndex(Integer index) throws Exception {
        return get(index); 
    }
    public String getProyectoJasonByIndex(Integer index)throws Exception{
        return g.toJson(get(index));
    }
    public TipoEnergia getTipoEnergia(String tipo){
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

    public Proyecto[] getListAll() {
        return listAll;
    }

    public void setListAll(Proyecto[] listAll) {
        this.listAll = listAll;
    }
}

