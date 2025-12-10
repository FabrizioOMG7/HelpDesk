package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Evidencia;
import com.utpintegrador.helpdesk.service.EvidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evidencias")
public class EvidenciaController {
    private final EvidenciaService evidenciaService;

    @Autowired
    public EvidenciaController(EvidenciaService evidenciaService) {
        this.evidenciaService = evidenciaService;
    }

    @GetMapping
    public List<Evidencia> obtenerEvidencias() {
        return evidenciaService.obtenerEvidencias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evidencia> obtenerPorId(@PathVariable Integer id) {
        return evidenciaService.obtenerEvidenciaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT: Crear (guardar) una nueva evidencia
    // Petición: POST http://localhost:8080/api/evidencias?rutaEvidencia=C:/uploads/img.png&ticketId=101
    // (NOTA: Más adelante, esto se hará con carga de archivos (MultipartFile), pero la lógica es la misma)
    @PostMapping
    public ResponseEntity<Evidencia> crearEvidencia(
            @RequestParam String rutaEvidencia, // 1. La ruta (o nombre) del archivo
            @RequestParam Integer ticketId        // 2. El ID del ticket
    ) {
        try {
            // 3. Pasamos todo al servicio
            Evidencia nuevaEvidencia = evidenciaService.guardarEvidencia(rutaEvidencia, ticketId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEvidencia);
        } catch (RuntimeException e) {
            // Error si el ticket no existe
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

}