package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model. Ticket;
import com.utpintegrador.helpdesk. model.Usuario;
import com. utpintegrador.helpdesk.model.SubCategoria;
import com.utpintegrador. helpdesk.model.Prioridad;
import com.utpintegrador.helpdesk. repository.TicketRepository;
import com.utpintegrador.helpdesk.repository.UsuarioRepository;
import com.utpintegrador.helpdesk.repository.SubCategoriaRepository;
import com.utpintegrador. helpdesk.repository.PrioridadRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit. jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit. jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter. api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org. mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private PrioridadRepository prioridadRepository;
    @Mock
    private SubCategoriaRepository subCategoriaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TicketService ticketService;

    // TICKET_01: Crear ticket con parámetros válidos
  
    @Test
    @DisplayName("TICKET_01: Crear ticket con parámetros válidos")
    void crearTicket_ParametrosValidos_GuardaExitosamente() {
        // Arrange
        Integer idUsuario = 1;
        Integer idSubCategoria = 1;
        Integer idPrioridad = 1;
        String titulo = "Problema con mi PC";

        Usuario usuario = new Usuario();
        SubCategoria subCategoria = new SubCategoria();
        Prioridad prioridad = new Prioridad();
        Ticket ticketGuardado = new Ticket();
        ticketGuardado.setTitulo(titulo);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(subCategoriaRepository. findById(idSubCategoria)).thenReturn(Optional.of(subCategoria));
        when(prioridadRepository.findById(idPrioridad)).thenReturn(Optional.of(prioridad));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticketGuardado);

        // Act
        Ticket resultado = ticketService.crearTicket(idUsuario, idSubCategoria, idPrioridad, titulo);

        // Assert
        assertNotNull(resultado);
        assertEquals(titulo, resultado.getTitulo());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(subCategoriaRepository, times(1)).findById(idSubCategoria);
        verify(prioridadRepository, times(1)).findById(idPrioridad);
    }

    // TICKET_02: Crear ticket con usuario inexistente

    @Test
    @DisplayName("TICKET_02: Crear ticket con usuario inexistente lanza excepción")
    void crearTicket_UsuarioInexistente_LanzaExcepcion() {
        // Arrange
        Integer idUsuario = 999;
        Integer idSubCategoria = 1;
        Integer idPrioridad = 1;
        String titulo = "Test";

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException. class, () -> {
            ticketService.crearTicket(idUsuario, idSubCategoria, idPrioridad, titulo);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
        verify(ticketRepository, never()).save(any(Ticket.class));
        verify(subCategoriaRepository, never()).findById(any());
        verify(prioridadRepository, never()).findById(any());
    }


    // TICKET_03: Crear ticket con subcategoría inexistente

    @Test
    @DisplayName("TICKET_03: Crear ticket con subcategoría inexistente lanza excepción")
    void crearTicket_SubCategoriaInexistente_LanzaExcepcion() {
        // Arrange
        Integer idUsuario = 1;
        Integer idSubCategoria = 999;
        Integer idPrioridad = 1;
        String titulo = "Test";

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(new Usuario()));
        when(subCategoriaRepository.findById(idSubCategoria)).thenReturn(Optional. empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketService. crearTicket(idUsuario, idSubCategoria, idPrioridad, titulo);
        });

        assertTrue(exception.getMessage().contains("SubCategoría no encontrada"));
        verify(ticketRepository, never()).save(any(Ticket.class));
    }


    // TICKET_04: Obtener todos los tickets
  
    @Test
    @DisplayName("TICKET_04: Obtener todos los tickets retorna lista")
    void obtenerTodosLosTickets_RetornaLista() {
        // Arrange
        List<Ticket> listaEsperada = Arrays.asList(new Ticket(), new Ticket());
        when(ticketRepository.findAll()).thenReturn(listaEsperada);

        // Act
        List<Ticket> resultado = ticketService. obtenerTodosLosTickets();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(listaEsperada, resultado);
        verify(ticketRepository, times(1)).findAll();
    }


    // TICKET_05: Obtener ticket por id existente
   
    @Test
    @DisplayName("TICKET_05: Obtener ticket por id existente retorna ticket")
    void obtenerTicketPorId_IdExistente_RetornaTicket() {
        // Arrange
        Integer ticketId = 1;
        Ticket ticketEsperado = new Ticket();
        ticketEsperado.setTitulo("Ticket de prueba");
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticketEsperado));

        // Act
        Ticket resultado = ticketService. obtenerTicketPorId(ticketId);

        // Assert
        assertNotNull(resultado);
        assertEquals(ticketEsperado, resultado);
        verify(ticketRepository, times(1)).findById(ticketId);
    }

   
    // TICKET_06: Obtener ticket por id inexistente
   
    @Test
    @DisplayName("TICKET_06: Obtener ticket por id inexistente lanza excepción")
    void obtenerTicketPorId_IdInexistente_LanzaExcepcion() {
        // Arrange
        Integer ticketId = 999;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketService.obtenerTicketPorId(ticketId);
        });

        assertTrue(exception.getMessage().contains("Ticket no encontrado"));
        verify(ticketRepository, times(1)).findById(ticketId);
    }
}