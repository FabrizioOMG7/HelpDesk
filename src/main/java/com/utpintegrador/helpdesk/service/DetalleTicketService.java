package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.*;
import com.utpintegrador.helpdesk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DetalleTicketService {

    private final DetalleTicketRepository detalleTicketRepository;
    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoTicketRepository estadoTicketRepository;

    @Autowired
    public DetalleTicketService(DetalleTicketRepository detalleTicketRepository,
                                TicketRepository ticketRepository,
                                UsuarioRepository usuarioRepository,
                                EstadoTicketRepository estadoTicketRepository) {
        this.detalleTicketRepository = detalleTicketRepository;
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoTicketRepository = estadoTicketRepository;
    }


    public List<DetalleTicket> obtenerTodosLosDetalles() {
        return detalleTicketRepository.findAll();
    }

    public DetalleTicket crearDetalleTicket(String descripcion, Integer ticketId, Integer usuarioId, Integer estadoId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con id: " + ticketId));
        EstadoTicket estado = estadoTicketRepository.findById(estadoId)
                .orElseThrow(() -> new RuntimeException("Estado de Ticket no encontrado con id: " + estadoId));

        Usuario usuarioAsignado = null;
        if (usuarioId != null) {
            usuarioAsignado = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Tecnico no encontrado con id: " + usuarioId));
        }

        DetalleTicket detalle = new DetalleTicket();
        detalle.setDescripcion(descripcion);
        detalle.setTicket(ticket);
        detalle.setEstadoTicket(estado);
        detalle.setUsuario(usuarioAsignado);

        //* Fecha Asignacion
        if (usuarioAsignado != null) {
            detalle.setFechaAsignacion(LocalDate.now());
        } else {
            detalle.setFechaAsignacion(null);
        }

        return detalleTicketRepository.save(detalle);
    }
}