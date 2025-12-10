package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Rol;
import com.utpintegrador.helpdesk.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    private final RolRepository rolRepository;

    @Autowired
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> obtenerRoles() {
        return rolRepository.findAll(); // Llama al repositorio
    }

    public Optional<Rol> obtenerRolPorId(Integer id) {
        return rolRepository.findById(id);
    }

    public Rol guardarRol(Rol rol) {
        return rolRepository.save(rol);
    }

    public void eliminarRol(Integer id) {
        rolRepository.deleteById(id);
    }
}