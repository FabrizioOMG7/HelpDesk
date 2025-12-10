package com.utpintegrador.helpdesk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "SUB_CATEGORIAS")
public class SubCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo_Sub_Categoria")
    private Integer codigoSubCategoria;

    @Column(name = "Nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "Estado", nullable = false)
    private boolean estado;

    //* RELACION
    @ManyToOne
    @JoinColumn(name = "Codigo_Categoria", nullable = false)
    private Categoria categoria;

    //* Constructor vacío
    public SubCategoria() {
    }

    //* Getters - Setters
    public Integer getCodigoSubCategoria() {
        return codigoSubCategoria;
    }

    public void setCodigoSubCategoria(Integer codigoSubCategoria) {
        this.codigoSubCategoria = codigoSubCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}