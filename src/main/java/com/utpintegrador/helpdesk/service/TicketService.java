package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.*;
import com.utpintegrador.helpdesk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;
    private final SubCategoriaRepository subCategoriaRepository;
    private final PrioridadRepository prioridadRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository,
                         UsuarioRepository usuarioRepository,
                         SubCategoriaRepository subCategoriaRepository,
                         PrioridadRepository prioridadRepository) {
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.subCategoriaRepository = subCategoriaRepository;
        this.prioridadRepository = prioridadRepository;
    }

    public Ticket crearTicket(Integer usuId, Integer subCatId, Integer prioId, String titulo) {

        Usuario usuario = usuarioRepository.findById(usuId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuId));

        SubCategoria subCategoria = subCategoriaRepository.findById(subCatId)
                .orElseThrow(() -> new RuntimeException("SubCategoría no encontrada con id: " + subCatId));

        Prioridad prioridad = prioridadRepository.findById(prioId)
                .orElseThrow(() -> new RuntimeException("Prioridad no encontrada con id: " + prioId));

        Ticket nuevoTicket = new Ticket();
        nuevoTicket.setTitulo(titulo);
        nuevoTicket.setUsuario(usuario);
        nuevoTicket.setSubCategoria(subCategoria);
        nuevoTicket.setPrioridad(prioridad);
        nuevoTicket.setFechaCreacion(LocalDateTime.now());
        nuevoTicket.setEstado(true);

        return ticketRepository.save(nuevoTicket);
    }

    public List<Ticket> obtenerTodosLosTickets() {
        return ticketRepository.findAll();
    }

    public Ticket obtenerTicketPorId(Integer id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con id: " + id));
    }

}
