package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.EstadoTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoTicketRepository extends JpaRepository<EstadoTicket, Integer> {
}