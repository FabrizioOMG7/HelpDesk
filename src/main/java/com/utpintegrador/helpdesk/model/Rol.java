package com.utpintegrador.helpdesk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ROLES")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo_Rol")
    private Integer codigoRol;

    @Column(name = "Nombre_Rol", nullable = false, length = 50)
    private String nombreRol;

    @Column(name = "Estado", nullable = false)
    private boolean estado;

    //*Constructor
    public Rol() {
    }

    //*Getters - Setters
    public Integer getCodigoRol() {
        return codigoRol;
    }

    public void setCodigoRol(Integer codigoRol) {
        this.codigoRol = codigoRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
