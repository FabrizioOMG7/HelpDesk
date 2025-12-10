package com.utpintegrador.helpdesk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORIAS")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo_Categoria")
    private Integer codigoCategoria;

    @Column(name = "Nombre_Categoria", nullable = false, length = 30)
    private String nombreCategoria;

    @Column(name = "Estado", nullable = false)
    private boolean estado;

    //* Constructor
    public Categoria() {
    }

    //* Getters - Setters
    public Integer getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(Integer codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}