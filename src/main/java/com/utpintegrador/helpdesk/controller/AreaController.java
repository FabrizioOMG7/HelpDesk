package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Area;
import com.utpintegrador.helpdesk.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/areas")
public class AreaController {

    private final AreaService areaService;

    @Autowired
    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    // 1. LISTAR TABLA (Formato DataTables { "data": ... })
    @GetMapping
    public ResponseEntity<Map<String, List<Area>>> obtenerTodas() {
        List<Area> todas = areaService.obtenerAreas();
        return ResponseEntity.ok(Map.of("data", todas));
    }

    // 2. COMBOBOX (Lista Simple [ ... ])
    // Este es el método nuevo que necesita tu JS de Usuario
    @GetMapping("/combo")
    public ResponseEntity<List<Area>> obtenerAreasCombo() {
        List<Area> todas = areaService.obtenerAreas();
        return ResponseEntity.ok(todas);
    }

    // 3. OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Area> obtenerPorId(@PathVariable Integer id) {
        Optional<Area> area = areaService.obtenerAreaPorId(id);
        return area.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. CREAR
    @PostMapping
    public ResponseEntity<?> crearArea(@RequestBody Area area) {
        try {
            Area nuevaArea = areaService.guardarArea(area);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaArea);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Error al guardar el área"));
        }
    }

    // 5. ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarArea(@PathVariable Integer id, @RequestBody Area area) {
        if (!areaService.obtenerAreaPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        area.setCodigoArea(id);
        try {
            Area areaActualizada = areaService.guardarArea(area);
            return ResponseEntity.ok(areaActualizada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Error al actualizar el área"));
        }
    }
}