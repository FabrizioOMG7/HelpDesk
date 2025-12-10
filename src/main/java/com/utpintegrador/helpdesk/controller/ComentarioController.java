package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Comentario;
import com.utpintegrador.helpdesk.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    @Autowired
    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }
    @PostMapping
    public ResponseEntity<Comentario> crearComentario(
            @RequestParam String contenido,
            @RequestParam Integer usuarioId,
            @RequestParam Integer detalleTicketId
    ) {
        try {
            Comentario nuevoComentario = comentarioService.crearComentario(
                    contenido, usuarioId, detalleTicketId
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoComentario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Integer id) {
        comentarioService.eliminarComentario(id);
        return ResponseEntity.noContent().build();
    }

}