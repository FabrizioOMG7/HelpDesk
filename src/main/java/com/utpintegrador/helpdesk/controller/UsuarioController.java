package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Usuario;
import com.utpintegrador.helpdesk.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    //*Mostrar datos en tabla
    @GetMapping
    public ResponseEntity<Map<String, List<Usuario>>> obtenerDatosUsuario() {
        List<Usuario> usuarios = usuarioService.obtenerUsuarios();
        //* Sin contraseña
        usuarios.forEach(u -> u.setPasswoord(null));
        Map<String, List<Usuario>> response = Map.of("data", usuarios);
        return ResponseEntity.ok(response);
    }

    //*Mostrar datos según idUsuario
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Integer id) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setPasswoord(null);
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuarioRequest) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuario(usuarioRequest);
            nuevoUsuario.setPasswoord(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al crear usuario."));
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioRequest) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioRequest);
            usuarioActualizado.setPasswoord(null);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().startsWith("Usuario no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al actualizar usuario."));
        }
    }


    //* Eliminar registro de usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok(Map.of("message", "Usuario eliminado lógicamente con ID: " + id));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al eliminar usuario."));
        }
    }

    //* Activar usuario
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activarUsuario(@PathVariable Integer id) {
        try {
            usuarioService.activarUsuario(id);
            return ResponseEntity.ok(Map.of("message", "Usuario reactivado con ID: " + id));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al activar usuario."));
        }
    }

}