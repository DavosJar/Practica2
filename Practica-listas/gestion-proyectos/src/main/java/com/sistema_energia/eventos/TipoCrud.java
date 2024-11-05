package com.sistema_energia.eventos;

public enum TipoCrud {
    CREATE("Crear"),
    READ("Leer"),
    UPDATE("Actualizar"),
    DELETE("Eliminar"),
    LIST("Listar"); 

    private final String descripcion; 

    TipoCrud(String descripcion) {
        this.descripcion = descripcion;
    }
 
    public String getDescripcion() {
        return descripcion;
    }
}
