package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.EstadoTicket;
import com.utpintegrador.helpdesk.repository.EstadoTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoTicketService {

    private final EstadoTicketRepository estadoTicketRepository;

    @Autowired
    public EstadoTicketService(EstadoTicketRepository estadoTicketRepository) {
        this.estadoTicketRepository = estadoTicketRepository;
    }


    public List<EstadoTicket> obtenerTodosLosEstados() {
        return estadoTicketRepository.findAll();
    }

    public Optional<EstadoTicket> obtenerEstadoPorId(Integer id) {
        return estadoTicketRepository.findById(id);
    }

    public EstadoTicket guardarEstado(EstadoTicket estadoTicket) {
        return estadoTicketRepository.save(estadoTicket);
    }

    public void eliminarEstado(Integer id) {

        estadoTicketRepository.deleteById(id);
    }
}