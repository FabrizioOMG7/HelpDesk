package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.*;
import com.utpintegrador.helpdesk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DiccionarioSolucionService {

    private final DiccionarioSolucionRepository diccionarioSolucionRepository;
    private final UsuarioRepository usuarioRepository;
    private final SubCategoriaRepository subCategoriaRepository;

    @Autowired
    public DiccionarioSolucionService(DiccionarioSolucionRepository diccionarioSolucionRepository,
                                      UsuarioRepository usuarioRepository,
                                      SubCategoriaRepository subCategoriaRepository) {
        this.diccionarioSolucionRepository = diccionarioSolucionRepository;
        this.usuarioRepository = usuarioRepository;
        this.subCategoriaRepository = subCategoriaRepository;
    }


    public List<DiccionarioSolucion> obtenerSoluciones() {
        return diccionarioSolucionRepository.findAll();
    }

    public Optional<DiccionarioSolucion> obtenerSolucionPorId(Integer id) {
        return diccionarioSolucionRepository.findById(id);
    }

    public DiccionarioSolucion crearSolucion(DiccionarioSolucion solucion, Integer usuarioId, Integer subCategoriaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        SubCategoria subCategoria = subCategoriaRepository.findById(subCategoriaId)
                .orElseThrow(() -> new RuntimeException("SubCategoría no encontrada con id: " + subCategoriaId));
        solucion.setUsuario(usuario);
        solucion.setSubCategoria(subCategoria);
        solucion.setFecha(LocalDate.now());
        return diccionarioSolucionRepository.save(solucion);
    }

    public void eliminarSolucion(Integer id) {
        diccionarioSolucionRepository.deleteById(id);
    }
}