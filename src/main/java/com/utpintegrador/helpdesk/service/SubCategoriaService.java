package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Categoria;
import com.utpintegrador.helpdesk.model.SubCategoria;
import com.utpintegrador.helpdesk.repository.CategoriaRepository;
import com.utpintegrador.helpdesk.repository.SubCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SubCategoriaService {

    private final SubCategoriaRepository subCategoriaRepository;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public SubCategoriaService(SubCategoriaRepository subCategoriaRepository, CategoriaRepository categoriaRepository) {
        this.subCategoriaRepository = subCategoriaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<SubCategoria> obtenerTodasLasSubCategorias() {
        return subCategoriaRepository.findAll();
    }

    public Optional<SubCategoria> obtenerSubCategoriaPorId(Integer id) {
        return subCategoriaRepository.findById(id);
    }

    public List<SubCategoria> obtenerPorCategoria(Integer catId) {
        return subCategoriaRepository.findByCategoria_CodigoCategoria(catId);
    }

    @Transactional
    public SubCategoria guardarSubCategoria(SubCategoria subCategoria) {
        // Validar que venga la categoría
        if (subCategoria.getCategoria() == null || subCategoria.getCategoria().getCodigoCategoria() == null) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }

        // Verificar que la categoría exista en BD
        Categoria categoria = categoriaRepository.findById(subCategoria.getCategoria().getCodigoCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        subCategoria.setCategoria(categoria);

        // Si es nuevo, estado activo por defecto
        if (subCategoria.getCodigoSubCategoria() == null) {
            subCategoria.setEstado(true);
        }

        return subCategoriaRepository.save(subCategoria);
    }

    @Transactional
    public SubCategoria eliminarSubCategoria(Integer id) {
        SubCategoria subCategoria = subCategoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubCategoría no encontrada con id: " + id));

        subCategoria.setEstado(false);
        return subCategoriaRepository.save(subCategoria);
    }

    @Transactional
    public SubCategoria activarSubCategoria(Integer id) {
        SubCategoria subCategoria = subCategoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubCategoría no encontrada con id: " + id));

        if (!subCategoria.isEstado()) {
            subCategoria.setEstado(true);
            return subCategoriaRepository.save(subCategoria);
        }
        return subCategoria;
    }
}