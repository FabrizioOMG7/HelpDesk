package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.DetalleTicket;
import com.utpintegrador.helpdesk.service.DetalleTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/detalle-tickets")
public class DetalleTicketController {

    private final DetalleTicketService detalleTicketService;


    @Autowired
    public DetalleTicketController(DetalleTicketService detalleTicketService) {
        this.detalleTicketService = detalleTicketService;
    }

    @GetMapping
    public List<DetalleTicket> obtenerTodos() {
        return detalleTicketService.obtenerTodosLosDetalles();
    }
    @PostMapping
    public ResponseEntity<DetalleTicket> crearDetalleTicket(
            @RequestParam String descripcion,
            @RequestParam Integer ticketId,
            @RequestParam Integer usuarioId,
            @RequestParam Integer estadoId
    ) {
        try {
            DetalleTicket nuevoDetalle = detalleTicketService.crearDetalleTicket(
                    descripcion, ticketId, usuarioId, estadoId
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDetalle);
        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().build();
        }
    }

}