package com.sistema_energia.controller.model;

public class Participacion {
    private Integer id;
    private Integer idProyecto;
    private Integer idInversionist;
    private Double montoInvertido;
    private String fechaRegistro;

    public Participacion() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Integer getIdInversionista() {
        return idInversionist;
    }

    public void setIdInversionist(Integer idInversionist) {
        this.idInversionist = idInversionist;
    }

    public Double getMontoInvertido() {
        return montoInvertido;
    }

    public void setMontoInvertido(Double montoInvertido) {
        this.montoInvertido = montoInvertido;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

}
