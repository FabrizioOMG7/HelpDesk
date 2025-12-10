package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

}