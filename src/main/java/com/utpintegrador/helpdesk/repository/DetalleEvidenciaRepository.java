package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.DetalleEvidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleEvidenciaRepository extends JpaRepository<DetalleEvidencia, Integer> {
}