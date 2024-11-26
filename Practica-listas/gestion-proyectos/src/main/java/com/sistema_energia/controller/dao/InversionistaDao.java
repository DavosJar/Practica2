package com.sistema_energia.controller.dao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sistema_energia.controller.dao.implement.AdapterDao;
import com.sistema_energia.controller.model.Inversionista;
import com.sistema_energia.controller.model.Provincia;
import com.sistema_energia.controller.model.Sector;
import com.sistema_energia.controller.tda.list.LinkedList;

@SuppressWarnings("unchecked")

public class InversionistaDao extends AdapterDao<Inversionista> {
    private Inversionista inversionista;
    private LinkedList<Inversionista> listAll;
    private Gson g = new Gson();

    public InversionistaDao() {
        super(Inversionista.class);
    }

    public Inversionista getInversionista() {
        if (this.inversionista == null) {
            this.inversionista = new Inversionista();
        }
        return this.inversionista;
    }

    public void setInversionista(Inversionista inversionista) {
        this.inversionista = inversionista;
    }

    public LinkedList<Inversionista> getAllInversionistas() throws Exception {
        try {
            listAll = listAll();
            if (listAll.getSize() <= 0) {
                return new LinkedList<>();
            }
            return listAll;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }

    public Boolean save() throws Exception {
        Integer id = getAllInversionistas().getSize() + 1;
        getInversionista().setId(id);
        this.persist(this.inversionista);
        listAll = null;
        return true;
    }

    public Boolean update() throws Exception {
        this.merge(this.inversionista, this.inversionista.getId());
        listAll = null;
        return true;
    }

    public Boolean deleteByIndex(Integer id) throws Exception {
        this.delete(id);
        listAll = null;
        reindex();
        return true;
    }

    private void reindex() throws Exception {
        LinkedList<Inversionista> inversionistas = getAllInversionistas();
        for (int i = 0; i < inversionistas.getSize(); i++) {
            inversionistas.get(i).setId(i + 1);
            this.merge(inversionistas.get(i), i);
        }
    }

    public LinkedList<Inversionista> obtenerInversionistasProyecto(Integer proyectoId) throws Exception {
        LinkedList<Inversionista> inversionistas = getAllInversionistas();
        LinkedList<Inversionista> inversionistasProyecto = new LinkedList<>();
        if (!inversionistas.isEmpty()) {
            Inversionista[] inversionistasArray = inversionistas.toArray();
            for (Inversionista inversionista : inversionistasArray) {
                if (inversionista.getProyectoId() == proyectoId) {
                    inversionistasProyecto.add(inversionista);
                }
            }
        } else {
            return new LinkedList<>();
        }
        return inversionistasProyecto;
    }

    public String toJson() throws Exception {
        return g.toJson(this.inversionista);
    }

    public Inversionista getInversionistaById(Integer id) throws Exception {
        return get(id);
    }

    public String getInversionistaJsonById(Integer id) throws Exception {
        return g.toJson(getInversionistaById(id));
    }

    public Provincia getProvincia(String provincia) {
        return Provincia.valueOf(provincia);
    }

    public Provincia[] getProvincia() {
        return Provincia.values();
    }

    public Sector getSector(String sector) {
        return Sector.valueOf(sector);
    }

    public Sector[] getSector() {
        return Sector.values();
    }

    public LinkedList<Inversionista> getListAll() {
        return listAll;
    }

    public void setListAll(LinkedList<Inversionista> listAll) {
        this.listAll = listAll;
    }
}
