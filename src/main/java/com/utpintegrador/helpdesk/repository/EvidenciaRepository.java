package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvidenciaRepository extends JpaRepository<Evidencia, Integer> {
}