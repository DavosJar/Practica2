package com.sistema_energia.controller.model;

public enum Sector {
    PUBLICO("Sector Público"),
    PRIVADO("Sector Privado"),
    ACADEMICO("Sector Académico"),
    ONG("Organización No Gubernamental"),
    COOPERATIVO("Sector Cooperativo"),
    FONDOS_DE_INVERSION("Fondos de Inversión"),
    ORGANISMOS_INTERNACIONALES("Organismos Internacionales"),
    EMPRESA_MIXTA("Empresa Mixta");

    private final String descripcion;

    Sector(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
