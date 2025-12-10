package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model. Categoria;
import com.utpintegrador.helpdesk.model.SubCategoria;
import com.utpintegrador.helpdesk.repository.CategoriaRepository;
import com.utpintegrador.helpdesk.repository. SubCategoriaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api. Test;
import org.junit.jupiter.api.DisplayName;
import org.junit. jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit. jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit. jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito. Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubCategoriaServiceTest {

    @Mock
    private SubCategoriaRepository subCategoriaRepository;
    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private SubCategoriaService subCategoriaService;

    private Categoria categoria;
    private SubCategoria subCategoria;

    @BeforeEach
    void setUp() {
        // Configurar Categoria
        categoria = new Categoria();
        categoria.setCodigoCategoria(1);
        categoria.setNombreCategoria("Hardware");
        categoria.setEstado(true);

        // Configurar SubCategoria
        subCategoria = new SubCategoria();
        subCategoria.setNombre("Impresoras");  // ← Corregido: setNombre
        subCategoria.setCategoria(categoria);
    }

   
    // SUBCAT_01: Obtener subcategorías por categoría
  
    @Test
    @DisplayName("SUBCAT_01: Obtener subcategorías por categoría retorna lista")
    void obtenerPorCategoria_CategoriaExistente_RetornaLista() {
        // Arrange
        Integer categoriaId = 1;
        SubCategoria sub1 = new SubCategoria();
        sub1.setNombre("Impresoras");  // ← Corregido
        SubCategoria sub2 = new SubCategoria();
        sub2.setNombre("Monitores");   // ← Corregido
        List<SubCategoria> listaEsperada = Arrays. asList(sub1, sub2);

        when(subCategoriaRepository.findByCategoria_CodigoCategoria(categoriaId)).thenReturn(listaEsperada);

        // Act
        List<SubCategoria> resultado = subCategoriaService.obtenerPorCategoria(categoriaId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado. size());
        assertEquals(listaEsperada, resultado);
        verify(subCategoriaRepository, times(1)).findByCategoria_CodigoCategoria(categoriaId);
    }

    
    // SUBCAT_02: Obtener todas las subcategorías
    
    @Test
    @DisplayName("SUBCAT_02: Obtener todas las subcategorías retorna lista")
    void obtenerTodasLasSubCategorias_RetornaLista() {
        // Arrange
        List<SubCategoria> listaEsperada = Arrays.asList(new SubCategoria(), new SubCategoria(), new SubCategoria());
        when(subCategoriaRepository.findAll()).thenReturn(listaEsperada);

        // Act
        List<SubCategoria> resultado = subCategoriaService.obtenerTodasLasSubCategorias();

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado. size());
        verify(subCategoriaRepository, times(1)).findAll();
    }

   
    // SUBCAT_03: Obtener subcategoría existente
   
    @Test
    @DisplayName("SUBCAT_03: Obtener subcategoría por id existente retorna Optional")
    void obtenerSubCategoriaPorId_IdExistente_RetornaOptional() {
        // Arrange
        Integer subCategoriaId = 1;
        SubCategoria subCategoriaEsperada = new SubCategoria();
        subCategoriaEsperada.setCodigoSubCategoria(subCategoriaId);
        subCategoriaEsperada. setNombre("Impresoras");  // ← Corregido

        when(subCategoriaRepository.findById(subCategoriaId)).thenReturn(Optional. of(subCategoriaEsperada));

        // Act
        Optional<SubCategoria> resultado = subCategoriaService.obtenerSubCategoriaPorId(subCategoriaId);

        // Assert
        assertTrue(resultado. isPresent());
        assertEquals(subCategoriaEsperada, resultado.get());
        assertEquals("Impresoras", resultado. get().getNombre());  // ← Corregido:  getNombre
        verify(subCategoriaRepository, times(1)).findById(subCategoriaId);
    }

 
    // SUBCAT_04: Guardar subcategoría válida
   
    @Test
    @DisplayName("SUBCAT_04: Guardar subcategoría válida asocia categoría y guarda")
    void guardarSubCategoria_DatosValidos_GuardaExitosamente() {
        // Arrange
        when(categoriaRepository.findById(1)).thenReturn(Optional. of(categoria));
        when(subCategoriaRepository.save(any(SubCategoria.class))).thenAnswer(invocation -> {
            SubCategoria sc = invocation.getArgument(0);
            sc.setCodigoSubCategoria(1);
            return sc;
        });

        // Act
        SubCategoria resultado = subCategoriaService.guardarSubCategoria(subCategoria);

        // Assert
        assertNotNull(resultado);
        assertEquals("Impresoras", resultado.getNombre());  // ← Corregido:  getNombre
        assertEquals(categoria, resultado.getCategoria());
        assertTrue(resultado.isEstado());
        verify(categoriaRepository, times(1)).findById(1);
        verify(subCategoriaRepository, times(1)).save(any(SubCategoria.class));
    }

    
    // SUBCAT_05: Guardar subcategoría con categoría inexistente
   
    @Test
    @DisplayName("SUBCAT_05: Guardar subcategoría con categoría inexistente lanza excepción")
    void guardarSubCategoria_CategoriaInexistente_LanzaExcepcion() {
        // Arrange
        when(categoriaRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException. class, () -> {
            subCategoriaService.guardarSubCategoria(subCategoria);
        });

        assertEquals("Categoría no encontrada", exception.getMessage());
        verify(subCategoriaRepository, never()).save(any(SubCategoria.class));
    }
}