package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.Prioridad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrioridadRepository extends JpaRepository<Prioridad, Integer> {
}