package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.EstadoTicket;
import com.utpintegrador.helpdesk.service.EstadoTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estados-ticket")
public class EstadoTicketController {

    private final EstadoTicketService estadoTicketService;

    @Autowired
    public EstadoTicketController(EstadoTicketService estadoTicketService) {
        this.estadoTicketService = estadoTicketService;
    }

    @GetMapping
    public List<EstadoTicket> obtenerTodos() {
        return estadoTicketService.obtenerTodosLosEstados();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoTicket> obtenerPorId(@PathVariable Integer id) {
        Optional<EstadoTicket> estado = estadoTicketService.obtenerEstadoPorId(id);

        return estado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EstadoTicket> crearEstado(@RequestBody EstadoTicket estadoTicket) {
        EstadoTicket nuevoEstado = estadoTicketService.guardarEstado(estadoTicket);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoTicket> actualizarEstado(@PathVariable Integer id, @RequestBody EstadoTicket estadoTicket) {
        // Verificamos si existe
        if (!estadoTicketService.obtenerEstadoPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        estadoTicket.setCodigoEstadoDeTicket(id);
        EstadoTicket estadoActualizado = estadoTicketService.guardarEstado(estadoTicket);

        return ResponseEntity.ok(estadoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstado(@PathVariable Integer id) {
        estadoTicketService.eliminarEstado(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}