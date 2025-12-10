package com.utpintegrador.helpdesk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DETALLE_DE_EVIDENCIAS")
public class DetalleEvidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo_Detalle_De_Evidencia")
    private Integer codigoDetalleDeEvidencia;

    @Column(name = "Nombre", nullable = false, length = 150)
    private String nombre;

    //* RELACION
    // Muchos detalles pueden pertenecer a UNA evidencia
    @ManyToOne
    @JoinColumn(name = "Codigo_Evidencia", nullable = false)
    private Evidencia evidencia;

    //* Constructor
    public DetalleEvidencia() {
    }

    //* Getters - Setters
    public Integer getCodigoDetalleDeEvidencia() {
        return codigoDetalleDeEvidencia;
    }

    public void setCodigoDetalleDeEvidencia(Integer codigoDetalleDeEvidencia) {
        this.codigoDetalleDeEvidencia = codigoDetalleDeEvidencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Evidencia getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(Evidencia evidencia) {
        this.evidencia = evidencia;
    }
}