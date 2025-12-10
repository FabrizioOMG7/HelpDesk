package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.DetalleTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleTicketRepository extends JpaRepository<DetalleTicket, Integer> {
}