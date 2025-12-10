package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.*;
import com.utpintegrador.helpdesk.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita Mockito
class TicketServiceTest {

    // 1. Simulamos las dependencias que usa TicketService
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private SubCategoriaRepository subCategoriaRepository;
    @Mock
    private PrioridadRepository prioridadRepository;

    // 2. Inyectamos los mocks en el servicio real
    @InjectMocks
    private TicketService ticketService;

    // AQUI IRÁN LOS CASOS DE PRUEBA (TICKET_01 a TICKET_06)
}