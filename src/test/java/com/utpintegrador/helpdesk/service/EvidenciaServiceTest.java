package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Evidencia;
import com.utpintegrador.helpdesk.model. Ticket;
import com.utpintegrador.helpdesk.repository.EvidenciaRepository;
import com.utpintegrador. helpdesk.repository.TicketRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api. Test;
import org.junit.jupiter.api.DisplayName;
import org.junit. jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org. mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit. jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito. Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EvidenciaServiceTest {

    @Mock
    private EvidenciaRepository evidenciaRepository;
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private EvidenciaService evidenciaService;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        // Configurar Ticket
        ticket = new Ticket();
        ticket.setCodigoTicket(1);
        ticket.setTitulo("Problema con impresora");

        // Inyectar valor de uploadRootDir usando ReflectionTestUtils
        ReflectionTestUtils.setField(evidenciaService, "uploadRootDir", "C:/uploads");
    }


    // EVI_01: Listar evidencias
   
    @Test
    @DisplayName("EVI_01: Listar evidencias retorna lista")
    void obtenerEvidencias_RetornaLista() {
        // Arrange
        List<Evidencia> listaEsperada = Arrays.asList(
                new Evidencia(),
                new Evidencia()
        );
        when(evidenciaRepository.findAll()).thenReturn(listaEsperada);

        // Act
        List<Evidencia> resultado = evidenciaService.obtenerEvidencias();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado. size());
        assertEquals(listaEsperada, resultado);
        verify(evidenciaRepository, times(1)).findAll();
    }

 
    // EVI_02: Obtener evidencia por id
   
    @Test
    @DisplayName("EVI_02: Obtener evidencia por id existente retorna Optional")
    void obtenerEvidenciaPorId_IdExistente_RetornaOptional() {
        // Arrange
        Integer evidenciaId = 1;
        Evidencia evidenciaEsperada = new Evidencia();
        evidenciaEsperada.setCodigoEvidencia(evidenciaId);
        evidenciaEsperada.setRutaEvidencia("C:/uploads/Ticket_1/");

        when(evidenciaRepository. findById(evidenciaId)).thenReturn(Optional.of(evidenciaEsperada));

        // Act
        Optional<Evidencia> resultado = evidenciaService.obtenerEvidenciaPorId(evidenciaId);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(evidenciaEsperada, resultado.get());
        verify(evidenciaRepository, times(1)).findById(evidenciaId);
    }

    
    // EVI_03: Guardar evidencia válida
   
    @Test
    @DisplayName("EVI_03: Guardar evidencia válida calcula ruta y guarda")
    void guardarEvidencia_DatosValidos_GuardaExitosamente() {
        // Arrange
        String nombreCarpeta = "Ticket_1";
        Integer ticketId = 1;

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(evidenciaRepository. save(any(Evidencia. class))).thenAnswer(invocation -> {
            Evidencia e = invocation.getArgument(0);
            e.setCodigoEvidencia(1);
            return e;
        });

        // Act
        Evidencia resultado = evidenciaService.guardarEvidencia(nombreCarpeta, ticketId);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getRutaEvidencia());
        assertTrue(resultado.getRutaEvidencia().contains("Ticket_1"));
        assertEquals(ticket, resultado.getTicket());
        assertNotNull(resultado.getFechaSubida());
        verify(evidenciaRepository, times(1)).save(any(Evidencia. class));
    }

    
    // EVI_04: Guardar evidencia con ticket inexistente
   
    @Test
    @DisplayName("EVI_04: Guardar evidencia con ticket inexistente lanza excepción")
    void guardarEvidencia_TicketInexistente_LanzaExcepcion() {
        // Arrange
        String nombreCarpeta = "Ticket_999";
        Integer ticketId = 999;

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            evidenciaService.guardarEvidencia(nombreCarpeta, ticketId);
        });

        assertTrue(exception.getMessage().contains("Ticket no encontrado"));
        verify(evidenciaRepository, never()).save(any(Evidencia.class));
    }
}