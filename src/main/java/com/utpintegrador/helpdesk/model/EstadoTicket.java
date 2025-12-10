package com.utpintegrador.helpdesk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ESTADOS_DE_TICKET")
public class EstadoTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo_Estado_De_Ticket")
    private Integer codigoEstadoDeTicket;

    @Column(name = "Nombre_Estado", nullable = false, length = 20)
    private String nombreEstado;

    //* Constructor
    public EstadoTicket() {
    }

    //* Getters - Setters
    public Integer getCodigoEstadoDeTicket() {
        return codigoEstadoDeTicket;
    }

    public void setCodigoEstadoDeTicket(Integer codigoEstadoDeTicket) {
        this.codigoEstadoDeTicket = codigoEstadoDeTicket;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
}