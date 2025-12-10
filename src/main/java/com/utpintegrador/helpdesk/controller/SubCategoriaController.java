package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.SubCategoria;
import com.utpintegrador.helpdesk.service.SubCategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subcategorias")
public class SubCategoriaController {

    private final SubCategoriaService subCategoriaService;

    @Autowired
    public SubCategoriaController(SubCategoriaService subCategoriaService) {
        this.subCategoriaService = subCategoriaService;
    }

    // 1. LISTAR TABLA
    @GetMapping
    public ResponseEntity<Map<String, List<SubCategoria>>> obtenerTodas() {
        List<SubCategoria> todas = subCategoriaService.obtenerTodasLasSubCategorias();
        return ResponseEntity.ok(Map.of("data", todas));
    }

    // 2. COMBOBOX (Nuevo Ticket) - Trae SOLO ACTIVAS filtradas por Categoría
    @GetMapping("/por-categoria")
    public ResponseEntity<List<SubCategoria>> getPorCategoria(@RequestParam("cat_id") Integer catId) {
        List<SubCategoria> subCategorias = subCategoriaService.obtenerPorCategoria(catId);

        List<SubCategoria> activas = subCategorias.stream()
                .filter(SubCategoria::isEstado)
                .collect(Collectors.toList());

        return ResponseEntity.ok(activas);
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<SubCategoria> obtenerPorId(@PathVariable Integer id) {
        return subCategoriaService.obtenerSubCategoriaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // CREAR
    @PostMapping
    public ResponseEntity<?> crearSubCategoria(@RequestBody SubCategoria subCategoria) {
        try {
            SubCategoria nueva = subCategoriaService.guardarSubCategoria(subCategoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarSubCategoria(@PathVariable Integer id, @RequestBody SubCategoria subCategoria) {
        if (!subCategoriaService.obtenerSubCategoriaPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        subCategoria.setCodigoSubCategoria(id);
        try {
            SubCategoria actualizada = subCategoriaService.guardarSubCategoria(subCategoria);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarSubCategoria(@PathVariable Integer id) {
        try {
            subCategoriaService.eliminarSubCategoria(id);
            return ResponseEntity.ok(Map.of("message", "SubCategoría desactivada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al eliminar."));
        }
    }

    // ACTIVAR
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activarSubCategoria(@PathVariable Integer id) {
        try {
            subCategoriaService.activarSubCategoria(id);
            return ResponseEntity.ok(Map.of("message", "SubCategoría reactivada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al activar."));
        }
    }
}