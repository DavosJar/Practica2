package com.sistema_energia.controller.model;

public enum Provincia {
    AZUAY("Azuay"),
    BOLIVAR("Bolívar"),
    CAÑAR("Cañar"),
    CARCHI("Carchi"),
    CHIMBORAZO("Chimborazo"),
    COTOPAXI("Cotopaxi"),
    EL_ORO("El Oro"),
    ESMERALDAS("Esmeraldas"),
    GALAPAGOS("Galápagos"),
    GUAYAS("Guayas"),
    IMBABURA("Imbabura"),
    LOJA("Loja"),
    LOS_RIOS("Los Ríos"),
    MANABI("Manabí"),
    MORONA_SANTIAGO("Morona Santiago"),
    NAPO("Napo"),
    ORELLANA("Orellana"),
    PASTAZA("Pastaza"),
    PICHINCHA("Pichincha"),
    SANTA_ELENA("Santa Elena"),
    SANTO_DOMINGO("Santo Domingo"),
    SUCUMBIOS("Sucumbíos"),
    TUNGURAHUA("Tungurahua"),
    ZAMORA_CHINCHIPE("Zamora Chinchipe");

    private final String nombre;

    
    Provincia(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
