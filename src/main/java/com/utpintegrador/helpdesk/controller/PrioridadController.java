package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Prioridad;
import com.utpintegrador.helpdesk.service.PrioridadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prioridades")
public class PrioridadController {

    private final PrioridadService prioridadService;

    @Autowired
    public PrioridadController(PrioridadService prioridadService) {
        this.prioridadService = prioridadService;
    }

    // 1. LISTAR TABLA MANTENIMIENTO
    @GetMapping
    public ResponseEntity<Map<String, List<Prioridad>>> obtenerPrioridades() {
        List<Prioridad> todas = prioridadService.obtenerPrioridades();
        return ResponseEntity.ok(Map.of("data", todas));
    }

    // 2. COMBOBOX (Nuevo Ticket / Filtros) - Trae SOLO ACTIVOS
    @GetMapping("/combo")
    public ResponseEntity<List<Prioridad>> obtenerPrioridadesActivas() {
        List<Prioridad> todas = prioridadService.obtenerPrioridades();

        List<Prioridad> activas = todas.stream()
                .filter(Prioridad::isEstado)
                .collect(Collectors.toList());

        return ResponseEntity.ok(activas);
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Prioridad> obtenerPorId(@PathVariable Integer id) {
        Optional<Prioridad> prioridad = prioridadService.obtenerPrioridadPorId(id);
        return prioridad.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // CREAR
    @PostMapping
    public ResponseEntity<?> crearPrioridad(@RequestBody Prioridad prioridad) {
        try {
            Prioridad nueva = prioridadService.guardarPrioridad(prioridad);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al guardar prioridad"));
        }
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPrioridad(@PathVariable Integer id, @RequestBody Prioridad prioridad) {
        if (!prioridadService.obtenerPrioridadPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        prioridad.setCodigoPrioridad(id);
        try {
            Prioridad actualizada = prioridadService.guardarPrioridad(prioridad);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al actualizar prioridad"));
        }
    }

    // ELIMINAR (Lógico)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPrioridad(@PathVariable Integer id) {
        try {
            prioridadService.eliminarPrioridad(id);
            return ResponseEntity.ok(Map.of("message", "Prioridad desactivada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al eliminar prioridad"));
        }
    }

    // ACTIVAR
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activarPrioridad(@PathVariable Integer id) {
        try {
            prioridadService.activarPrioridad(id);
            return ResponseEntity.ok(Map.of("message", "Prioridad reactivada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al activar prioridad"));
        }
    }
}