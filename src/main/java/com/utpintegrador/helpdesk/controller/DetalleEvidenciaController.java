package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.DetalleEvidencia;
import com.utpintegrador.helpdesk.service.DetalleEvidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-evidencias")
public class DetalleEvidenciaController {

    private final DetalleEvidenciaService detalleEvidenciaService;

    @Autowired
    public DetalleEvidenciaController(DetalleEvidenciaService detalleEvidenciaService) {
        this.detalleEvidenciaService = detalleEvidenciaService;
    }

    @GetMapping
    public List<DetalleEvidencia> obtenerTodos() {
        return detalleEvidenciaService.obtenerDetalles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleEvidencia> obtenerPorId(@PathVariable Integer id) {
        return detalleEvidenciaService.obtenerDetallePorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<DetalleEvidencia> crearDetalleEvidencia(
            @RequestParam String nombreDetalle,
            @RequestParam Integer evidenciaId
    ) {
        try {
            DetalleEvidencia nuevoDetalle = detalleEvidenciaService.guardarDetalleArchivo(nombreDetalle, evidenciaId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDetalle);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalle(@PathVariable Integer id) {
        detalleEvidenciaService.eliminarDetalleEvidencia(id);
        return ResponseEntity.noContent().build();
    }
}