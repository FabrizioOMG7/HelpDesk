package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.*;
import com.utpintegrador.helpdesk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AreaRepository areaRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          AreaRepository areaRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.areaRepository = areaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }
    public Optional<Usuario> obtenerUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Usuario crearUsuario(Usuario usuarioRequest) {
        //* Validar y obtener IDs de Rol y Area
        Integer rolId = Optional.ofNullable(usuarioRequest.getRol())
                .map(Rol::getCodigoRol)
                .orElseThrow(() -> new IllegalArgumentException("El Rol es obligatorio"));
        Integer areaId = Optional.ofNullable(usuarioRequest.getArea())
                .map(Area::getCodigoArea)
                .orElseThrow(() -> new IllegalArgumentException("El Área es obligatoria"));

        //* Buscar Rol y Area
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + rolId));
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new RuntimeException("Área no encontrada con id: " + areaId));


        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombres(usuarioRequest.getNombres());
        nuevoUsuario.setApellidoPaterno(usuarioRequest.getApellidoPaterno());
        nuevoUsuario.setApellidoMaterno(usuarioRequest.getApellidoMaterno());
        nuevoUsuario.setCorreo(usuarioRequest.getCorreo());

        //* Validar y Encriptar contraseña
        if (!StringUtils.hasText(usuarioRequest.getPasswoord())) {
            throw new IllegalArgumentException("La contraseña es obligatoria al crear usuario");
        }
        nuevoUsuario.setPasswoord(passwordEncoder.encode(usuarioRequest.getPasswoord().trim()));
        nuevoUsuario.setRol(rol);
        nuevoUsuario.setArea(area);
        nuevoUsuario.setFechaCreacion(LocalDateTime.now());
        nuevoUsuario.setEstado(true);
        nuevoUsuario.setFechaModificacion(null);
        nuevoUsuario.setFechaEliminacion(null);

        return usuarioRepository.save(nuevoUsuario);
    }


    @Transactional
    public Usuario actualizarUsuario(Integer id, Usuario usuarioRequest) {
        //* Buscar el usuario
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuarioExistente.setNombres(usuarioRequest.getNombres());
        usuarioExistente.setApellidoPaterno(usuarioRequest.getApellidoPaterno());
        usuarioExistente.setApellidoMaterno(usuarioRequest.getApellidoMaterno());
        usuarioExistente.setCorreo(usuarioRequest.getCorreo());

        if (StringUtils.hasText(usuarioRequest.getPasswoord())) {
            usuarioExistente.setPasswoord(passwordEncoder.encode(usuarioRequest.getPasswoord().trim()));
        }

        //* Actualizar rol
        Integer rolId = Optional.ofNullable(usuarioRequest.getRol()).map(Rol::getCodigoRol).orElse(null);
        if (rolId != null) {
            if (!rolId.equals(usuarioExistente.getRol().getCodigoRol())) {
                Rol rol = rolRepository.findById(rolId)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + rolId));
                usuarioExistente.setRol(rol);
            }
        } else {
            throw new IllegalArgumentException("El Rol es obligatorio al actualizar");
        }

        //* Actualizar Area
        Integer areaId = Optional.ofNullable(usuarioRequest.getArea()).map(Area::getCodigoArea).orElse(null);
        if (areaId != null) {

            if (!areaId.equals(usuarioExistente.getArea().getCodigoArea())) {
                Area area = areaRepository.findById(areaId)
                        .orElseThrow(() -> new RuntimeException("Área no encontrada con id: " + areaId));
                usuarioExistente.setArea(area);
            }
        } else {
            throw new IllegalArgumentException("El Área es obligatoria al actualizar");
        }

        usuarioExistente.setFechaModificacion(LocalDateTime.now());

        return usuarioRepository.save(usuarioExistente);
    }



    @Transactional
    public Usuario eliminarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuario.setEstado(false);
        usuario.setFechaEliminacion(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }


    @Transactional
    public Usuario activarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        if (!usuario.isEstado()) {
            usuario.setEstado(true);
            usuario.setFechaEliminacion(null);
            usuario.setFechaModificacion(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        }
        return usuario;
    }
}