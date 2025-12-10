package com.utpintegrador.HelpDesk.service;

import com.utpintegrador.helpdesk.model.*; // Importa todas las clases (Ticket, Usuario, Prioridad, etc.)
import com.utpintegrador.helpdesk.repository.*;
import com.utpintegrador.helpdesk.service.TicketService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    // --- TEST 1: CREAR TICKET (CORREGIDO PARA LOS 4 PARÁMETROS) ---
    @Test
    void crearTicket_ParametrosValidos_GuardaExitosamente() {
        // 1. Arrange (Preparamos los datos sueltos)
        Integer idUsuario = 1;
        Integer idSubCategoria = 1;
        Integer idPrioridad = 1;
        String descripcion = "Problema con mi PC";

        // Simulamos que EXISTEN en la base de datos
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(new Usuario()));
        when(subCategoriaRepository.findById(idSubCategoria)).thenReturn(Optional.of(new SubCategoria()));
        when(prioridadRepository.findById(idPrioridad)).thenReturn(Optional.of(new Prioridad()));

        // Simulamos el guardado final
        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());

        // 2. Act (Llamamos al método con los 4 INGREDIENTES, no con el pastel entero)
        ticketService.crearTicket(idUsuario, idSubCategoria, idPrioridad, descripcion);

        // 3. Assert (Verificamos que al final se intentó guardar un ticket)
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    // --- TEST 2: OBTENER POR ID ---
    @Test
    void obtenerTicketPorId_IdExistente_RetornaTicket() {
        // Arrange
        Ticket ticket = new Ticket();
        when(ticketRepository.findById(1)).thenReturn(Optional.of(ticket));

        // Act
        Ticket resultado = ticketService.obtenerTicketPorId(1);

        // Assert
        assertNotNull(resultado);
    }

    // --- TEST 3: OBTENER TODOS ---
    @Test
    void obtenerTodosLosTickets_RetornaLista() {
        // Arrange
        List<Ticket> lista = Arrays.asList(new Ticket(), new Ticket());
        when(ticketRepository.findAll()).thenReturn(lista);

        // Act
        List<Ticket> resultado = ticketService.obtenerTodosLosTickets();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }
}