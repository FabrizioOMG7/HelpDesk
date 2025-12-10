package com.utpintegrador.helpdesk.dto;

import lombok.Data;

@Data
public class TicketConsultarDTO {
    private Integer codigoTicket;
    private String titulo;
    private String nombreCategoria;
    private String nombrePrioridad;
    private String fechaCreacion;
    private String fechaAsignacion;
    private String fechaCierre;
    private String nombreSoporte;
    private Integer soporteId;

    // ESTE ES EL CAMPO QUE FALTABA Y CAUSABA EL ERROR
    private String estadoNombre;
}