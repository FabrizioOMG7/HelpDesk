package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.DiccionarioSolucion;
import com.utpintegrador.helpdesk.service.DiccionarioSolucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diccionario-soluciones") // URL base
public class DiccionarioSolucionController {

    private final DiccionarioSolucionService diccionarioSolucionService;

    @Autowired
    public DiccionarioSolucionController(DiccionarioSolucionService diccionarioSolucionService) {
        this.diccionarioSolucionService = diccionarioSolucionService;
    }

    @GetMapping
    public List<DiccionarioSolucion> obtenerTodas() {
        return diccionarioSolucionService.obtenerSoluciones();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiccionarioSolucion> obtenerPorId(@PathVariable Integer id) {
        return diccionarioSolucionService.obtenerSolucionPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<DiccionarioSolucion> crearSolucion(
            @RequestBody DiccionarioSolucion solucion,
            @RequestParam Integer usuarioId,
            @RequestParam Integer subCategoriaId
    ) {
        try {

            DiccionarioSolucion nuevaSolucion = diccionarioSolucionService.crearSolucion(solucion, usuarioId, subCategoriaId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolucion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolucion(@PathVariable Integer id) {
        diccionarioSolucionService.eliminarSolucion(id);
        return ResponseEntity.noContent().build();
    }
}