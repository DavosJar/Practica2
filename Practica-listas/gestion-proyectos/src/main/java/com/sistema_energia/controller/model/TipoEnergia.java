package com.sistema_energia.controller.model;

public enum TipoEnergia {
    SOLAR("Solar"),
    EOLICA("Eólica"),
    HIDRAULICA("Hidráulica"),
    GEOTERMICA("Geotérmica"),
    BIOMASA("Biomasa"),
    BIOCOMBUSTIBLES("Biocombustibles"),
    NUCLEAR("Nuclear"),
    GAS("Gas"),
    PETROLEO("Petróleo"),
    CARBON("Carbón"),
    HIDROGENO("Hidrógeno"),
    OTRAS("Otras");

    private final String nombre;

    TipoEnergia(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
