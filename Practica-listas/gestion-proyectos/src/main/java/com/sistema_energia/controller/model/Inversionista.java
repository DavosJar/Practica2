package com.sistema_energia.controller.model;

public class Inversionista {
    private Integer id;
    private String nombre;
    private String registro;
    private Double montoInvertido;
    private Sector sector;
    private Provincia ubicacion;

    public Inversionista() {
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

}
