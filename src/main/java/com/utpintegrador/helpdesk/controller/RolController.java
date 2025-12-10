package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Rol;
import com.utpintegrador.helpdesk.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    @Autowired
    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<Rol> obtenerRoles() {
        return rolService.obtenerRoles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerPorId(@PathVariable Integer id) {
        Optional<Rol> rol = rolService.obtenerRolPorId(id);

        if (rol.isPresent()) {
            return ResponseEntity.ok(rol.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol) {
        Rol nuevoRol = rolService.guardarRol(rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRol);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizarRol(@PathVariable Integer id, @RequestBody Rol rol) {
        Optional<Rol> rolExistente = rolService.obtenerRolPorId(id);
        if (!rolExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        rol.setCodigoRol(id);
        Rol rolActualizado = rolService.guardarRol(rol);

        return ResponseEntity.ok(rolActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Integer id) {
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }
}