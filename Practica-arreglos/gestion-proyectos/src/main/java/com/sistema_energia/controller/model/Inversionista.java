package com.sistema_energia.controller.model;

public class Inversionista {
    private Integer id;
    private String nombre;
    private String registro;
    private Double montoInvertido;
    private Sector sector;
    private Provincia ubicacion;
    private Integer proyectoId;
    

    public Inversionista() {
    }

    public Inversionista(Integer id, String nombre, String registro, Double montoInvertido, Sector sector, Provincia ubicacion, Integer proyectoId) {
        this.id = id;
        this.nombre = nombre;
        this.registro = registro;
        this.montoInvertido = montoInvertido;
        this.sector = sector;
        this.ubicacion = ubicacion;
        this.proyectoId = proyectoId;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public Double getMontoInvertido() {
        return montoInvertido;
    }

    public void setMontoInvertido(Double montoInvertido) {
        this.montoInvertido = montoInvertido;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Provincia getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Provincia ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Integer getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Integer proyectoId) {
        this.proyectoId = proyectoId;
    }
    
}
