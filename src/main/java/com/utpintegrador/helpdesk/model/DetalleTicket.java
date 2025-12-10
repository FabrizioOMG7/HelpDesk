package com.utpintegrador.helpdesk.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DETALLE_DE_TICKETS")
public class DetalleTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo_Detalle_De_Ticket")
    private Integer codigoDetalleDeTicket;

    @Column(name = "Fecha_Asignacion")
    private LocalDate fechaAsignacion;

    @Column(name = "Descripcion", nullable = false, columnDefinition = "VARCHAR(MAX)")
    private String descripcion;

    //* RELACIONES
    // Muchos detalles de ticket pertenecen a UN solo ticket
    @ManyToOne
    @JoinColumn(name = "Codigo_Ticket", nullable = false)
    private Ticket ticket;

    // Muchos detalles (asignaciones) pueden ser hechos por UN usuario (el técnico)
    @ManyToOne
    @JoinColumn(name = "Codigo_Usuario")
    private Usuario usuario;

    // Muchos detalles pueden registrar UN estado
    @ManyToOne
    @JoinColumn(name = "Codigo_Estado_De_Ticket", nullable = false)
    private EstadoTicket estadoTicket;

    //* Constructor
    public DetalleTicket() {
    }

    //* Getters - Setters
    public Integer getCodigoDetalleDeTicket() {
        return codigoDetalleDeTicket;
    }

    public void setCodigoDetalleDeTicket(Integer codigoDetalleDeTicket) {
        this.codigoDetalleDeTicket = codigoDetalleDeTicket;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EstadoTicket getEstadoTicket() {
        return estadoTicket;
    }

    public void setEstadoTicket(EstadoTicket estadoTicket) {
        this.estadoTicket = estadoTicket;
    }
}