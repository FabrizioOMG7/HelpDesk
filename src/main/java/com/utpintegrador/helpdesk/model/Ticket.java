package com.utpintegrador.helpdesk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TICKETS")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo_Ticket")
    private Integer codigoTicket;

    @Column(name = "Titulo", nullable = false, length = 250)
    private String titulo;

    @Column(name = "Fecha_Creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "Fecha_Cierre") // Permite nulos
    private LocalDateTime fechaCierre;

    @Column(name = "Estado", nullable = false)
    private boolean estado;

    //* RELACIONES
    // Muchos tickets pueden ser creados por UN usuario
    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Evidencia evidencia;

    @ManyToOne
    @JoinColumn(name = "Codigo_Usuario", nullable = false)
    private Usuario usuario;

    // Muchos tickets pueden pertenecer a UNA subcategoría
    @ManyToOne
    @JoinColumn(name = "Codigo_Sub_Categoria", nullable = false)
    private SubCategoria subCategoria;

    // Muchos tickets pueden tener UNA prioridad
    @ManyToOne
    @JoinColumn(name = "Codigo_Prioridad")
    private Prioridad prioridad;

    //* Constructor
    public Ticket() {
    }

    //* Getters - Setters
    public Integer getCodigoTicket() {
        return codigoTicket;
    }

    public void setCodigoTicket(Integer codigoTicket) {
        this.codigoTicket = codigoTicket;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }
}
