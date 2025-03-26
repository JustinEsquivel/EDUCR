package com.Educr.domain;

public enum Rol {
    ADMIN("ADMIN"), 
    ESTUDIANTE("ESTUDIANTE");
    
    private final String nombre;
    
    Rol(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() {
        return nombre;
    }
}