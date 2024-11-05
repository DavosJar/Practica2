package com.sistema_energia.controller.model;

public class Proyecto {
    private int id;
    private String nombre;
    private double costoEstimadoInicial;  
    private double inversion;
    private String fechaInicio;
    private String fechaFin;
    private Integer tiempoDeVida;
    private Integer capacidad;
    private TipoEnergia tipoEnergia;
    private Provincia ubicacion;
    private String descripcion;
    private Estado estado;


    public Proyecto() {
    }

    public Proyecto(int id, String nombre, double inversion, String fechaInicio, String fechaFin, Integer tiempoDeVida, TipoEnergia tipoEnergia, Provincia ubicacion, String descripcion, Estado estado) {
        this.id = id;
        this.nombre = nombre;
        this.inversion = inversion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tiempoDeVida = tiempoDeVida;
        this.tipoEnergia = tipoEnergia;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getInversion() {
        return inversion;
    }

    public void setInversion(double inversion) {
        this.inversion = inversion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getTiempoDeVida() {
        return tiempoDeVida;
    }

    public void setTiempoDeVida(Integer tiempoDeVida) {
        this.tiempoDeVida = tiempoDeVida;
    }

    public TipoEnergia getTipoEnergia() {
        return tipoEnergia;
    }

    public void setTipoEnergia(TipoEnergia tipoEnergia) {
        this.tipoEnergia = tipoEnergia;
    }

    public Provincia getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Provincia ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public double getCostoEstimadoInicial() {
        return costoEstimadoInicial;
    }

    public void setCostoEstimadoInicial(double costoEstimadoInicial) {
        this.costoEstimadoInicial = costoEstimadoInicial;
    }
    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return "Proyecto{" + "id=" + id + ", nombre=" + nombre + ", inversion=" + inversion + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", tiempoDeVida=" + tiempoDeVida + ", tipoEnergia=" + tipoEnergia + ", ubicacion=" + ubicacion + ", descripcion=" + descripcion + ", estado=" + estado + '}';
    }
    
}
    