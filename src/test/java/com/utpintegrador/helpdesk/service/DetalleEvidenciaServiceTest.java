package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.DetalleEvidencia;
import com.utpintegrador.helpdesk. model.Evidencia;
import com.utpintegrador.helpdesk.repository.DetalleEvidenciaRepository;
import com.utpintegrador.helpdesk.repository.EvidenciaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api. Test;
import org.junit. jupiter.api.DisplayName;
import org.junit. jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit. jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit. jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DetalleEvidenciaServiceTest {

    @Mock
    private DetalleEvidenciaRepository detalleEvidenciaRepository;
    @Mock
    private EvidenciaRepository evidenciaRepository;

    @InjectMocks
    private DetalleEvidenciaService detalleEvidenciaService;

    private Evidencia evidencia;

    @BeforeEach
    void setUp() {
        // Configurar Evidencia
        evidencia = new Evidencia();
        evidencia. setCodigoEvidencia(1);
        evidencia.setRutaEvidencia("C:/uploads/Ticket_1/");
    }

    
    // DETEVI_01: Listar detalles de evidencia
    
    @Test
    @DisplayName("DETEVI_01: Listar detalles de evidencia retorna lista")
    void obtenerDetalles_RetornaLista() {
        // Arrange
        List<DetalleEvidencia> listaEsperada = Arrays.asList(
                new DetalleEvidencia(),
                new DetalleEvidencia()
        );
        when(detalleEvidenciaRepository.findAll()).thenReturn(listaEsperada);

        // Act
        List<DetalleEvidencia> resultado = detalleEvidenciaService.obtenerDetalles();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(listaEsperada, resultado);
        verify(detalleEvidenciaRepository, times(1)).findAll();
    }

    
    // DETEVI_02: Obtener detalle por id
    
    @Test
    @DisplayName("DETEVI_02: Obtener detalle por id existente retorna Optional")
    void obtenerDetallePorId_IdExistente_RetornaOptional() {
        // Arrange
        Integer detalleId = 1;
        DetalleEvidencia detalleEsperado = new DetalleEvidencia();
        detalleEsperado.setCodigoDetalleDeEvidencia(detalleId);
        detalleEsperado.setNombre("archivo_12345.pdf");

        when(detalleEvidenciaRepository.findById(detalleId)).thenReturn(Optional.of(detalleEsperado));

        // Act
        Optional<DetalleEvidencia> resultado = detalleEvidenciaService.obtenerDetallePorId(detalleId);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(detalleEsperado, resultado.get());
        assertEquals("archivo_12345.pdf", resultado.get().getNombre());
        verify(detalleEvidenciaRepository, times(1)).findById(detalleId);
    }

   
    // DETEVI_03: Guardar detalle de evidencia válido
   
    @Test
    @DisplayName("DETEVI_03: Guardar detalle de evidencia válido guarda exitosamente")
    void guardarDetalleArchivo_DatosValidos_GuardaExitosamente() {
        // Arrange
        String nombreArchivoUnico = "documento_abc123.pdf";
        Integer evidenciaId = 1;

        when(evidenciaRepository.findById(evidenciaId)).thenReturn(Optional.of(evidencia));
        when(detalleEvidenciaRepository.save(any(DetalleEvidencia.class))).thenAnswer(invocation -> {
            DetalleEvidencia de = invocation.getArgument(0);
            de.setCodigoDetalleDeEvidencia(1);
            return de;
        });

        // Act
        DetalleEvidencia resultado = detalleEvidenciaService.guardarDetalleArchivo(nombreArchivoUnico, evidenciaId);

        // Assert
        assertNotNull(resultado);
        assertEquals(nombreArchivoUnico, resultado. getNombre());
        assertEquals(evidencia, resultado.getEvidencia());
        verify(detalleEvidenciaRepository, times(1)).save(any(DetalleEvidencia.class));
    }

   
    // DETEVI_04: Guardar detalle con evidencia inexistente
    
    @Test
    @DisplayName("DETEVI_04: Guardar detalle con evidencia inexistente lanza excepción")
    void guardarDetalleArchivo_EvidenciaInexistente_LanzaExcepcion() {
        // Arrange
        String nombreArchivoUnico = "archivo. pdf";
        Integer evidenciaId = 999;

        when(evidenciaRepository.findById(evidenciaId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException. class, () -> {
            detalleEvidenciaService. guardarDetalleArchivo(nombreArchivoUnico, evidenciaId);
        });

        assertTrue(exception.getMessage().contains("Evidencia no encontrada"));
        verify(detalleEvidenciaRepository, never()).save(any(DetalleEvidencia.class));
    }

    
    // DETEVI_05: Eliminar detalle
    
    @Test
    @DisplayName("DETEVI_05: Eliminar detalle invoca deleteById")
    void eliminarDetalleEvidencia_IdValido_InvocaDeleteById() {
        // Arrange
        Integer detalleId = 1;
        doNothing().when(detalleEvidenciaRepository).deleteById(detalleId);

        // Act
        detalleEvidenciaService.eliminarDetalleEvidencia(detalleId);

        // Assert
        verify(detalleEvidenciaRepository, times(1)).deleteById(detalleId);
    }
}