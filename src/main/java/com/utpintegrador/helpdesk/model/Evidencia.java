package com.utpintegrador.helpdesk.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "EVIDENCIAS")
public class Evidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo_Evidencia")
    private Integer codigoEvidencia;

    @Column(name = "Ruta_Evidencia", nullable = false, length = 250)
    private String rutaEvidencia;

    @Column(name = "Fecha_Subida", nullable = false)
    private LocalDate fechaSubida;

    //* RELACIÓN
    // Muchas evidencias pueden pertenecer a UN ticket
    @OneToOne
    @JoinColumn(name = "Codigo_Ticket", nullable = false)
    private Ticket ticket;

    //* Constructor vacío
    public Evidencia() {
    }

    //* Getters y Setters
    public Integer getCodigoEvidencia() {
        return codigoEvidencia;
    }

    public void setCodigoEvidencia(Integer codigoEvidencia) {
        this.codigoEvidencia = codigoEvidencia;
    }

    public String getRutaEvidencia() {
        return rutaEvidencia;
    }

    public void setRutaEvidencia(String rutaEvidencia) {
        this.rutaEvidencia = rutaEvidencia;
    }

    public LocalDate getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDate fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}