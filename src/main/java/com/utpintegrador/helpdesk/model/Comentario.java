package com.utpintegrador.helpdesk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMENTARIOS")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo_Comentario")
    private Integer codigoComentario;

    @Column(name = "Contenido", nullable = false, columnDefinition = "VARCHAR(MAX)")
    private String contenido;

    @Column(name = "Fecha_Creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    //* RELACIONES
    // Muchos comentarios pueden ser escritos por UN usuario
    @ManyToOne
    @JoinColumn(name = "Codigo_Usuario", nullable = false)
    private Usuario usuario;

    // Muchos comentarios pueden pertenecer a UN detalle de ticket
    @ManyToOne
    @JoinColumn(name = "Codigo_Detalle_De_Ticket", nullable = false)
    private DetalleTicket detalleTicket;

    //* Constructor
    public Comentario() {
    }

    //* Getters - Setters

    public Integer getCodigoComentario() {
        return codigoComentario;
    }

    public void setCodigoComentario(Integer codigoComentario) {
        this.codigoComentario = codigoComentario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public DetalleTicket getDetalleTicket() {
        return detalleTicket;
    }

    public void setDetalleTicket(DetalleTicket detalleTicket) {
        this.detalleTicket = detalleTicket;
    }
}