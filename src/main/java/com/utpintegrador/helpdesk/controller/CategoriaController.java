package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Categoria;
import com.utpintegrador.helpdesk.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<Categoria>>> obtenerTodasParaTabla() {
        List<Categoria> todas = categoriaService.obtenerCategorias();
        return ResponseEntity.ok(Map.of("data", todas));
    }

    @GetMapping("/combo")
    public ResponseEntity<List<Categoria>> obtenerActivasParaCombo() {
        List<Categoria> todas = categoriaService.obtenerCategorias();

        // Filtramos aquí para devolver solo las que tienen estado = true
        List<Categoria> activas = todas.stream()
                .filter(Categoria::isEstado)
                .collect(Collectors.toList());

        return ResponseEntity.ok(activas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Integer id) {
        Optional<Categoria> categoria = categoriaService.obtenerCategoriaPorId(id);
        return categoria.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria categoria) {
        try {
            Categoria nuevaCategoria = categoriaService.guardarCategoria(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Error al guardar la categoría"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoria) {
        Optional<Categoria> existente = categoriaService.obtenerCategoriaPorId(id);
        if (!existente.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        categoria.setCodigoCategoria(id);
        try {
            Categoria categoriaActualizada = categoriaService.guardarCategoria(categoria);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Error al actualizar la categoría"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Integer id) {
        try {
            categoriaService.eliminarCategoria(id);
            return ResponseEntity.ok(Map.of("message", "Categoría desactivada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al eliminar categoría"));
        }
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activarCategoria(@PathVariable Integer id) {
        try {
            categoriaService.activarCategoria(id);
            return ResponseEntity.ok(Map.of("message", "Categoría reactivada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al activar categoría"));
        }
    }
}