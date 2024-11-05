package com.sistema_energia.controller.dao;

import com.google.gson.Gson;
import com.sistema_energia.controller.dao.implement.AdapterDao;
import com.sistema_energia.controller.dao.services.InversionistaServices;
import com.sistema_energia.controller.model.Estado;
import com.sistema_energia.controller.model.Inversionista;
import com.sistema_energia.controller.model.Provincia;
import com.sistema_energia.controller.model.Proyecto;
import com.sistema_energia.controller.model.TipoEnergia;
import com.sistema_energia.controller.tda.list.LinkedList;

public class ProyectoDao extends AdapterDao<Proyecto> {
    private Proyecto proyecto;
    private LinkedList listAll;
    private LinkedList listInversionistas;
    
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
    
    public LinkedList<Proyecto> getListAll() throws Exception {
        if (listAll == null) {
            listAll = listAll(); 
        }
        return listAll; 
    }

    public boolean save() throws Exception {
        Integer id = getListAll().getSize() + 1; 
        getProyecto().setId(id);
        this.persist(this.proyecto); 
        listAll = listAll();
        return true; 
    }

    private void actualizarInversionProyecto(Double nuevoMontoInvertido) throws Exception {
        if (this.proyecto == null) {
            throw new Exception("No hay un proyecto actual para actualizar la inversión.");
        }
        this.proyecto.setInversion(this.proyecto.getInversion() + nuevoMontoInvertido);
        this.merge(this.proyecto, this.proyecto.getId() - 1);
    }
    //Metodo que guarda un inversionista en un proyecto
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
    //Metodo que actualiza un inversionista en un proyecto
    private LinkedList<Inversionista> getAllInversionistas() throws Exception {
        if (listInversionistas == null) {
            InversionistaServices is = new InversionistaServices();
            listInversionistas = is.getAllInversionistas();
        }
        return listInversionistas;
    }
    //Metodo que obtiene los inversionistas de un proyecto
    public LinkedList<Inversionista> getInversionistasByProyectoId(Integer proyectoId) throws Exception {
        Inversionista[] allInversionistas = getAllInversionistas().toArray();
        
        LinkedList<Inversionista> inversionistasAsociados = new LinkedList<>();
        for (Inversionista inversionista : allInversionistas) {
            if (inversionista.getProyectoId() == proyectoId) {
                inversionistasAsociados.add(inversionista);
            }
        }
        return inversionistasAsociados;
    }

    public Boolean update () throws Exception {
        this.merge(this.proyecto, this.proyecto.getId()); 
        listAll = listAll(); 
        return true; 
    }

    public Boolean delete() throws Exception {
        if (listAll == null) {
            listAll = getListAll(); 
        }
        this.delete(proyecto.getId());
        reindex();
        return true;
    }

    private void reindex() throws Exception {
        LinkedList<Proyecto> listAll = listAll();
        for (int i = 0; i < listAll.getSize(); i++) {
            Proyecto p = listAll.get(i);
            p.setId(i + 1);
            this.merge(p, i+1);
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
    public Proyecto getProyectoB(Integer Index) throws Exception {
        return get(Index); 
    }

    public String getProyectoJson(Integer Index) throws Exception {
        return g.toJson(get(Index)); 
    }
}

