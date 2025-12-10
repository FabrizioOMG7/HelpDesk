package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Prioridad;
import com.utpintegrador.helpdesk.repository.PrioridadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PrioridadService {

    private final PrioridadRepository prioridadRepository;

    @Autowired
    public PrioridadService(PrioridadRepository prioridadRepository) {
        this.prioridadRepository = prioridadRepository;
    }

    public List<Prioridad> obtenerPrioridades() {
        return prioridadRepository.findAll();
    }

    public Optional<Prioridad> obtenerPrioridadPorId(Integer id) {
        return prioridadRepository.findById(id);
    }

    @Transactional
    public Prioridad guardarPrioridad(Prioridad prioridad) {
        // Si es nuevo, forzamos estado activo
        if (prioridad.getCodigoPrioridad() == null) {
            prioridad.setEstado(true);
        }
        return prioridadRepository.save(prioridad);
    }

    @Transactional
    public Prioridad eliminarPrioridad(Integer id) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prioridad no encontrada con id: " + id));

        prioridad.setEstado(false);
        return prioridadRepository.save(prioridad);
    }

    @Transactional
    public Prioridad activarPrioridad(Integer id) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prioridad no encontrada con id: " + id));

        if (!prioridad.isEstado()) {
            prioridad.setEstado(true);
            return prioridadRepository.save(prioridad);
        }
        return prioridad;
    }
}