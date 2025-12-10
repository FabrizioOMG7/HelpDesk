package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Categoria;
import com.utpintegrador. helpdesk.repository.CategoriaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit. jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito. Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit. jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito. Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    
    // CAT_01: Obtener categorías
    
    @Test
    @DisplayName("CAT_01: Obtener categorías retorna lista")
    void obtenerCategorias_RetornaLista() {
        // Arrange
        Categoria cat1 = new Categoria();
        cat1.setNombreCategoria("Hardware");
        Categoria cat2 = new Categoria();
        cat2.setNombreCategoria("Software");
        List<Categoria> listaEsperada = Arrays.asList(cat1, cat2);
        
        when(categoriaRepository.findAll()).thenReturn(listaEsperada);

        // Act
        List<Categoria> resultado = categoriaService. obtenerCategorias();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(listaEsperada, resultado);
        verify(categoriaRepository, times(1)).findAll();
    }

    
    // CAT_02: Obtener categoría existente
  
    @Test
    @DisplayName("CAT_02: Obtener categoría por id existente retorna Optional con categoría")
    void obtenerCategoriaPorId_IdExistente_RetornaOptionalConCategoria() {
        // Arrange
        Integer categoriaId = 1;
        Categoria categoriaEsperada = new Categoria();
        categoriaEsperada.setCodigoCategoria(categoriaId);
        categoriaEsperada.setNombreCategoria("Hardware");
        
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional. of(categoriaEsperada));

        // Act
        Optional<Categoria> resultado = categoriaService.obtenerCategoriaPorId(categoriaId);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(categoriaEsperada, resultado.get());
        assertEquals("Hardware", resultado.get().getNombreCategoria());
        verify(categoriaRepository, times(1)).findById(categoriaId);
    }

    
    // CAT_03: Guardar categoría
   
    @Test
    @DisplayName("CAT_03: Guardar categoría nueva asigna estado true y guarda")
    void guardarCategoria_CategoriaNueva_GuardaConEstadoTrue() {
        // Arrange
        Categoria categoriaNueva = new Categoria();
        categoriaNueva.setNombreCategoria("Redes");
        // codigoCategoria es null (nueva categoría)
        
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(invocation -> {
            Categoria c = invocation.getArgument(0);
            c.setCodigoCategoria(1);
            return c;
        });

        // Act
        Categoria resultado = categoriaService.guardarCategoria(categoriaNueva);

        // Assert
        assertNotNull(resultado);
        assertEquals("Redes", resultado.getNombreCategoria());
        assertTrue(resultado.isEstado());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

  
    // CAT_04: Eliminar categoría (desactivar)
  
    @Test
    @DisplayName("CAT_04: Eliminar categoría cambia estado a false")
    void eliminarCategoria_CategoriaExistente_DesactivaCategoria() {
        // Arrange
        Integer categoriaId = 1;
        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setCodigoCategoria(categoriaId);
        categoriaExistente.setNombreCategoria("Hardware");
        categoriaExistente.setEstado(true);
        
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoriaExistente));
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Categoria resultado = categoriaService.eliminarCategoria(categoriaId);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEstado());
        verify(categoriaRepository, times(1)).findById(categoriaId);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }
}