package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model. DetalleTicket;
import com.utpintegrador.helpdesk.model.EstadoTicket;
import com.utpintegrador.helpdesk.model.Ticket;
import com.utpintegrador. helpdesk.model.Usuario;
import com.utpintegrador.helpdesk.repository.DetalleTicketRepository;
import com.utpintegrador.helpdesk.repository.EstadoTicketRepository;
import com.utpintegrador.helpdesk.repository.TicketRepository;
import com.utpintegrador.helpdesk.repository.UsuarioRepository;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DetalleTicketServiceTest {

    @Mock
    private DetalleTicketRepository detalleTicketRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private EstadoTicketRepository estadoTicketRepository;

    @InjectMocks
    private DetalleTicketService detalleTicketService;

    private Ticket ticket;
    private Usuario usuario;
    private EstadoTicket estadoTicket;

    @BeforeEach
    void setUp() {
        // Configurar Ticket
        ticket = new Ticket();
        ticket.setCodigoTicket(1);
        ticket.setTitulo("Problema con impresora");

        // Configurar Usuario (técnico)
        usuario = new Usuario();
        usuario.setCodigoUsuario(1);
        usuario.setNombres("Juan Técnico");

        // Configurar EstadoTicket
        estadoTicket = new EstadoTicket();
        estadoTicket.setCodigoEstadoDeTicket(1);
        estadoTicket.setNombreEstado("En Proceso");
    }

    
    // DET_01: Listar detalles
   
    @Test
    @DisplayName("DET_01: Listar detalles retorna lista")
    void obtenerTodosLosDetalles_RetornaLista() {
        // Arrange
        List<DetalleTicket> listaEsperada = Arrays.asList(
                new DetalleTicket(),
                new DetalleTicket()
        );
        when(detalleTicketRepository.findAll()).thenReturn(listaEsperada);

        // Act
        List<DetalleTicket> resultado = detalleTicketService.obtenerTodosLosDetalles();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(listaEsperada, resultado);
        verify(detalleTicketRepository, times(1)).findAll();
    }

    
    // DET_02: Crear detalle con usuario asignado
   
    @Test
    @DisplayName("DET_02: Crear detalle con usuario asignado guarda exitosamente")
    void crearDetalleTicket_ConUsuarioAsignado_GuardaExitosamente() {
        // Arrange
        String descripcion = "Se está revisando el problema";
        Integer ticketId = 1;
        Integer usuarioId = 1;
        Integer estadoId = 1;

        when(ticketRepository.findById(ticketId)).thenReturn(Optional. of(ticket));
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(estadoTicketRepository.findById(estadoId)).thenReturn(Optional.of(estadoTicket));
        when(detalleTicketRepository.save(any(DetalleTicket.class))).thenAnswer(invocation -> {
            DetalleTicket dt = invocation.getArgument(0);
            dt.setCodigoDetalleDeTicket(1);
            return dt;
        });

        // Act
        DetalleTicket resultado = detalleTicketService.crearDetalleTicket(descripcion, ticketId, usuarioId, estadoId);

        // Assert
        assertNotNull(resultado);
        assertEquals(descripcion, resultado.getDescripcion());
        assertEquals(ticket, resultado.getTicket());
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(estadoTicket, resultado.getEstadoTicket());
        assertNotNull(resultado.getFechaAsignacion()); // Debe tener fecha porque hay usuario
        verify(detalleTicketRepository, times(1)).save(any(DetalleTicket. class));
    }

   
    // DET_03: Crear detalle sin usuario (sin asignación)
  
    @Test
    @DisplayName("DET_03: Crear detalle sin usuario mantiene usuario y fecha null")
    void crearDetalleTicket_SinUsuario_FechaAsignacionNull() {
        // Arrange
        String descripcion = "Ticket sin asignar";
        Integer ticketId = 1;
        Integer usuarioId = null; // Sin usuario
        Integer estadoId = 1;

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(estadoTicketRepository.findById(estadoId)).thenReturn(Optional.of(estadoTicket));
        when(detalleTicketRepository.save(any(DetalleTicket. class))).thenAnswer(invocation -> {
            DetalleTicket dt = invocation.getArgument(0);
            dt.setCodigoDetalleDeTicket(1);
            return dt;
        });

        // Act
        DetalleTicket resultado = detalleTicketService.crearDetalleTicket(descripcion, ticketId, usuarioId, estadoId);

        // Assert
        assertNotNull(resultado);
        assertEquals(descripcion, resultado.getDescripcion());
        assertNull(resultado.getUsuario()); // Usuario debe ser null
        assertNull(resultado.getFechaAsignacion()); // Fecha debe ser null
        verify(usuarioRepository, never()).findById(any()); // No se debe buscar usuario
        verify(detalleTicketRepository, times(1)).save(any(DetalleTicket.class));
    }

  
    // DET_04: Crear detalle con ticket inexistente
 
    @Test
    @DisplayName("DET_04: Crear detalle con ticket inexistente lanza excepción")
    void crearDetalleTicket_TicketInexistente_LanzaExcepcion() {
        // Arrange
        String descripcion = "Descripción";
        Integer ticketId = 999;
        Integer usuarioId = 1;
        Integer estadoId = 1;

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException. class, () -> {
            detalleTicketService.crearDetalleTicket(descripcion, ticketId, usuarioId, estadoId);
        });

        assertTrue(exception.getMessage().contains("Ticket no encontrado"));
        verify(detalleTicketRepository, never()).save(any(DetalleTicket.class));
    }

   
    // DET_05: Crear detalle con estado inexistente
   
    @Test
    @DisplayName("DET_05: Crear detalle con estado inexistente lanza excepción")
    void crearDetalleTicket_EstadoInexistente_LanzaExcepcion() {
        // Arrange
        String descripcion = "Descripción";
        Integer ticketId = 1;
        Integer usuarioId = 1;
        Integer estadoId = 999;

        when(ticketRepository. findById(ticketId)).thenReturn(Optional.of(ticket));
        when(estadoTicketRepository.findById(estadoId)).thenReturn(Optional. empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            detalleTicketService.crearDetalleTicket(descripcion, ticketId, usuarioId, estadoId);
        });

        assertTrue(exception.getMessage().contains("Estado de Ticket no encontrado"));
        verify(detalleTicketRepository, never()).save(any(DetalleTicket.class));
    }
}