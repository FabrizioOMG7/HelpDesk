package com.utpintegrador.helpdesk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "AREAS")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo_Area")
    private Integer codigoArea;

    @Column(name = "Nombre_Area", nullable = false, length = 150)
    private String nombreArea;

    //* Constructor
    public Area() {
    }

    //* Getters - Setters
    public Integer getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(Integer codigoArea) {
        this.codigoArea = codigoArea;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }
}