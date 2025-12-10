package com.utpintegrador.helpdesk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PRIORIDADES")
public class Prioridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo_Prioridad")
    private Integer codigoPrioridad;

    @Column(name = "Nombre_Prioridad", nullable = false, length = 20)
    private String nombrePrioridad;

    @Column(name = "Estado", nullable = false)
    private boolean estado;

    //* Constructor
    public Prioridad() {
    }

    //* Getters - Setters
    public Integer getCodigoPrioridad() {
        return codigoPrioridad;
    }

    public void setCodigoPrioridad(Integer codigoPrioridad) {
        this.codigoPrioridad = codigoPrioridad;
    }

    public String getNombrePrioridad() {
        return nombrePrioridad;
    }

    public void setNombrePrioridad(String nombrePrioridad) {
        this.nombrePrioridad = nombrePrioridad;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}