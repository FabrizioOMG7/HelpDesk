package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.DiccionarioSolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiccionarioSolucionRepository extends JpaRepository<DiccionarioSolucion, Integer> {
}