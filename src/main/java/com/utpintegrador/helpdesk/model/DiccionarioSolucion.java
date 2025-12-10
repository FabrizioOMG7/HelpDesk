package com.utpintegrador.helpdesk.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DICCIONARIO_DE_SOLUCIONES")
public class DiccionarioSolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo_Diccionario_De_Solucion")
    private Integer codigoDiccionarioDeSolucion;

    @Column(name = "Titulo", nullable = false, length = 250)
    private String titulo;

    @Column(name = "Descripcion", nullable = false, columnDefinition = "VARCHAR(MAX)")
    private String descripcion;

    @Column(name = "Fecha", nullable = false)
    private LocalDate fecha;

    //* RELACIONES
    // Muchas soluciones pueden ser creadas por UN usuario
    @ManyToOne
    @JoinColumn(name = "Codigo_Usuario", nullable = false)
    private Usuario usuario;

    // Muchas soluciones pueden estar asociadas a UNA subcategoría
    @ManyToOne
    @JoinColumn(name = "Codigo_Sub_Categoria", nullable = false)
    private SubCategoria subCategoria;

    //* Constructor
    public DiccionarioSolucion() {
    }

    //* Getters - Setters
    public Integer getCodigoDiccionarioDeSolucion() {
        return codigoDiccionarioDeSolucion;
    }

    public void setCodigoDiccionarioDeSolucion(Integer codigoDiccionarioDeSolucion) {
        this.codigoDiccionarioDeSolucion = codigoDiccionarioDeSolucion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public SubCategoria getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(SubCategoria subCategoria) {
        this.subCategoria = subCategoria;
    }
}