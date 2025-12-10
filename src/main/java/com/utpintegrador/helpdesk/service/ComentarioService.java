package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Comentario;
import com.utpintegrador.helpdesk.model.DetalleTicket;
import com.utpintegrador.helpdesk.model.Usuario;
import com.utpintegrador.helpdesk.repository.ComentarioRepository;
import com.utpintegrador.helpdesk.repository.DetalleTicketRepository;
import com.utpintegrador.helpdesk.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final DetalleTicketRepository detalleTicketRepository;

    @Autowired
    public ComentarioService(ComentarioRepository comentarioRepository,
                             UsuarioRepository usuarioRepository,
                             DetalleTicketRepository detalleTicketRepository) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.detalleTicketRepository = detalleTicketRepository;
    }

    public Comentario crearComentario(String contenido, Integer usuarioId, Integer detalleTicketId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        DetalleTicket detalleTicket = detalleTicketRepository.findById(detalleTicketId)
                .orElseThrow(() -> new RuntimeException("Detalle de Ticket no encontrado con id: " + detalleTicketId));

        Comentario comentario = new Comentario();
        comentario.setContenido(contenido);
        comentario.setFechaCreacion(LocalDateTime.now());
        comentario.setUsuario(usuario);
        comentario.setDetalleTicket(detalleTicket);

        return comentarioRepository.save(comentario);
    }

    public void eliminarComentario(Integer id) {
        comentarioRepository.deleteById(id);
    }
}