package com.utpintegrador.helpdesk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIOS")
public class  Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo_Usuario")
    private Integer codigoUsuario;

    @Column(name = "Nombres", nullable = false, length = 50)
    private String nombres;

    @Column(name = "Apellido_Paterno", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "Apellido_Materno", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "Correo", nullable = false, length = 50)
    private String correo;

    @Column(name = "Passwoord", nullable = false, length = 100)
    private String password;

    @Column(name = "Fecha_Creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "Fecha_Modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "Fecha_Eliminacion")
    private LocalDateTime fechaEliminacion;

    @Column(name = "Estado", nullable = false)
    private boolean estado;

    // -RELACIONES
    @ManyToOne
    @JoinColumn(name = "Codigo_Area", nullable = false)
    private Area area;

    @ManyToOne
    @JoinColumn(name = "Codigo_Rol", nullable = false)
    private Rol rol;


    //* Constructor vacío
    public Usuario() {
    }

    //* Getters - Setters
    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(LocalDateTime fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getPasswoord() {
        return password;
    }

    public void setPasswoord(String passwoord) {
        this.password = passwoord;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Integer getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Integer codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }
}