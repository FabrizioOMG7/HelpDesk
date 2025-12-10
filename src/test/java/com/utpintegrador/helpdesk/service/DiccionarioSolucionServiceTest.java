package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.DiccionarioSolucion;
import com. utpintegrador.helpdesk.model.SubCategoria;
import com.utpintegrador.helpdesk. model.Usuario;
import com. utpintegrador.helpdesk.repository.DiccionarioSolucionRepository;
import com. utpintegrador.helpdesk.repository.SubCategoriaRepository;
import com.utpintegrador.helpdesk.repository. UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api. Test;
import org.junit. jupiter.api.DisplayName;
import org.junit. jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit. jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiccionarioSolucionServiceTest {

    @Mock
    private DiccionarioSolucionRepository diccionarioSolucionRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private SubCategoriaRepository subCategoriaRepository;

    @InjectMocks
    private DiccionarioSolucionService diccionarioSolucionService;

    private Usuario usuario;
    private SubCategoria subCategoria;
    private DiccionarioSolucion solucion;

    @BeforeEach
    void setUp() {
        // Configurar Usuario
        usuario = new Usuario();
        usuario.setCodigoUsuario(1);
        usuario.setNombres("Juan");

        // Configurar SubCategoria
        subCategoria = new SubCategoria();
        subCategoria.setCodigoSubCategoria(1);
        subCategoria.setNombre("Impresoras");

        // Configurar DiccionarioSolucion
        solucion = new DiccionarioSolucion();
        solucion.setTitulo("Solución para impresora");
        solucion.setDescripcion("Reiniciar el servicio de cola de impresión");
    }

    
    // DIC_01: Listar soluciones
    
    @Test
    @DisplayName("DIC_01: Listar soluciones retorna lista")
    void obtenerSoluciones_RetornaLista() {
        // Arrange
        List<DiccionarioSolucion> listaEsperada = Arrays.asList(
                new DiccionarioSolucion(), 
                new DiccionarioSolucion()
        );
        when(diccionarioSolucionRepository.findAll()).thenReturn(listaEsperada);

        // Act
        List<DiccionarioSolucion> resultado = diccionarioSolucionService.obtenerSoluciones();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(listaEsperada, resultado);
        verify(diccionarioSolucionRepository, times(1)).findAll();
    }

   
    // DIC_02: Obtener solución por id
  
    @Test
    @DisplayName("DIC_02: Obtener solución por id existente retorna Optional")
    void obtenerSolucionPorId_IdExistente_RetornaOptional() {
        // Arrange
        Integer solucionId = 1;
        DiccionarioSolucion solucionEsperada = new DiccionarioSolucion();
        solucionEsperada. setCodigoDiccionarioDeSolucion(solucionId);
        solucionEsperada.setTitulo("Solución de prueba");

        when(diccionarioSolucionRepository.findById(solucionId)).thenReturn(Optional.of(solucionEsperada));

        // Act
        Optional<DiccionarioSolucion> resultado = diccionarioSolucionService.obtenerSolucionPorId(solucionId);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(solucionEsperada, resultado.get());
        assertEquals("Solución de prueba", resultado.get().getTitulo());
        verify(diccionarioSolucionRepository, times(1)).findById(solucionId);
    }

   
    // DIC_03: Crear solución válida
    
    @Test
    @DisplayName("DIC_03: Crear solución válida guarda exitosamente")
    void crearSolucion_DatosValidos_GuardaExitosamente() {
        // Arrange
        Integer usuarioId = 1;
        Integer subCategoriaId = 1;

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(subCategoriaRepository.findById(subCategoriaId)).thenReturn(Optional.of(subCategoria));
        when(diccionarioSolucionRepository.save(any(DiccionarioSolucion.class))).thenAnswer(invocation -> {
            DiccionarioSolucion ds = invocation.getArgument(0);
            ds.setCodigoDiccionarioDeSolucion(1);
            return ds;
        });

        // Act
        DiccionarioSolucion resultado = diccionarioSolucionService.crearSolucion(solucion, usuarioId, subCategoriaId);

        // Assert
        assertNotNull(resultado);
        assertEquals("Solución para impresora", resultado.getTitulo());
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(subCategoria, resultado.getSubCategoria());
        assertNotNull(resultado.getFecha());
        verify(diccionarioSolucionRepository, times(1)).save(any(DiccionarioSolucion.class));
    }

    
    // DIC_04: Crear solución con usuario inexistente
   
    @Test
    @DisplayName("DIC_04: Crear solución con usuario inexistente lanza excepción")
    void crearSolucion_UsuarioInexistente_LanzaExcepcion() {
        // Arrange
        Integer usuarioId = 999;
        Integer subCategoriaId = 1;

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            diccionarioSolucionService.crearSolucion(solucion, usuarioId, subCategoriaId);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
        verify(diccionarioSolucionRepository, never()).save(any(DiccionarioSolucion.class));
    }

    
    // DIC_05: Crear solución con subcategoría inexistente
    
    @Test
    @DisplayName("DIC_05: Crear solución con subcategoría inexistente lanza excepción")
    void crearSolucion_SubCategoriaInexistente_LanzaExcepcion() {
        // Arrange
        Integer usuarioId = 1;
        Integer subCategoriaId = 999;

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional. of(usuario));
        when(subCategoriaRepository.findById(subCategoriaId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            diccionarioSolucionService.crearSolucion(solucion, usuarioId, subCategoriaId);
        });

        assertTrue(exception.getMessage().contains("SubCategoría no encontrada"));
        verify(diccionarioSolucionRepository, never()).save(any(DiccionarioSolucion. class));
    }

    
    // DIC_06: Eliminar solución
    
    @Test
    @DisplayName("DIC_06: Eliminar solución invoca deleteById")
    void eliminarSolucion_IdValido_InvocaDeleteById() {
        // Arrange
        Integer solucionId = 1;
        doNothing().when(diccionarioSolucionRepository).deleteById(solucionId);

        // Act
        diccionarioSolucionService.eliminarSolucion(solucionId);

        // Assert
        verify(diccionarioSolucionRepository, times(1)).deleteById(solucionId);
    }
}