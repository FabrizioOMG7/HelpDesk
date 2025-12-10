package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Categoria;
import com.utpintegrador.helpdesk.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> obtenerCategoriaPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    @Transactional
    public Categoria guardarCategoria(Categoria categoria) {
        // Si es nuevo registro, aseguramos estado activo
        if (categoria.getCodigoCategoria() == null) {
            categoria.setEstado(true);
        }
        return categoriaRepository.save(categoria);
    }


    @Transactional
    public Categoria eliminarCategoria(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));

        categoria.setEstado(false);

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria activarCategoria(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));

        if (!categoria.isEstado()) {
            categoria.setEstado(true);
            return categoriaRepository.save(categoria);
        }
        return categoria;
    }
}