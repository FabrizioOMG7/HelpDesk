package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model. Comentario;
import com. utpintegrador.helpdesk.model.DetalleTicket;
import com.utpintegrador.helpdesk.model. Usuario;
import com.utpintegrador.helpdesk.repository.ComentarioRepository;
import com.utpintegrador.helpdesk.repository.DetalleTicketRepository;
import com. utpintegrador.helpdesk.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api. Test;
import org.junit.jupiter.api.DisplayName;
import org.junit. jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit. jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit. jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private DetalleTicketRepository detalleTicketRepository;

    @InjectMocks
    private ComentarioService comentarioService;

    private Usuario usuario;
    private DetalleTicket detalleTicket;

    @BeforeEach
    void setUp() {
        // Configurar Usuario
        usuario = new Usuario();
        usuario.setCodigoUsuario(1);
        usuario.setNombres("Juan");

        // Configurar DetalleTicket
        detalleTicket = new DetalleTicket();
        detalleTicket.setCodigoDetalleDeTicket(1);
        detalleTicket.setDescripcion("Descripción del detalle");
    }

    
    // COM_01: Crear comentario válido
    
    @Test
    @DisplayName("COM_01: Crear comentario válido guarda exitosamente")
    void crearComentario_DatosValidos_GuardaExitosamente() {
        // Arrange
        String contenido = "Este es un comentario de prueba";
        Integer usuarioId = 1;
        Integer detalleTicketId = 1;

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(detalleTicketRepository.findById(detalleTicketId)).thenReturn(Optional.of(detalleTicket));
        when(comentarioRepository.save(any(Comentario.class))).thenAnswer(invocation -> {
            Comentario c = invocation.getArgument(0);
            c.setCodigoComentario(1);
            return c;
        });

        // Act
        Comentario resultado = comentarioService.crearComentario(contenido, usuarioId, detalleTicketId);

        // Assert
        assertNotNull(resultado);
        assertEquals(contenido, resultado. getContenido());
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(detalleTicket, resultado.getDetalleTicket());
        assertNotNull(resultado.getFechaCreacion());
        verify(comentarioRepository, times(1)).save(any(Comentario.class));
    }

   
    // COM_02: Crear comentario con usuario inexistente
  
    @Test
    @DisplayName("COM_02: Crear comentario con usuario inexistente lanza excepción")
    void crearComentario_UsuarioInexistente_LanzaExcepcion() {
        // Arrange
        String contenido = "Comentario de prueba";
        Integer usuarioId = 999;
        Integer detalleTicketId = 1;

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            comentarioService.crearComentario(contenido, usuarioId, detalleTicketId);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
        verify(comentarioRepository, never()).save(any(Comentario.class));
    }

    
    // COM_03: Crear comentario con detalle inexistente
   
    @Test
    @DisplayName("COM_03: Crear comentario con detalle inexistente lanza excepción")
    void crearComentario_DetalleInexistente_LanzaExcepcion() {
        // Arrange
        String contenido = "Comentario de prueba";
        Integer usuarioId = 1;
        Integer detalleTicketId = 999;

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(detalleTicketRepository.findById(detalleTicketId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            comentarioService.crearComentario(contenido, usuarioId, detalleTicketId);
        });

        assertTrue(exception.getMessage().contains("Detalle de Ticket no encontrado"));
        verify(comentarioRepository, never()).save(any(Comentario.class));
    }

   
    // COM_04: Eliminar comentario
    
    @Test
    @DisplayName("COM_04: Eliminar comentario invoca deleteById")
    void eliminarComentario_IdValido_InvocaDeleteById() {
        // Arrange
        Integer comentarioId = 1;
        doNothing().when(comentarioRepository).deleteById(comentarioId);

        // Act
        comentarioService.eliminarComentario(comentarioId);

        // Assert
        verify(comentarioRepository, times(1)).deleteById(comentarioId);
    }
}