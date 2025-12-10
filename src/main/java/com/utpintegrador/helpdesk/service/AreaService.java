package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Area;
import com.utpintegrador.helpdesk.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AreaService {

    private final AreaRepository areaRepository;

    @Autowired
    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    public List<Area> obtenerAreas() {
        return areaRepository.findAll();
    }

    public Optional<Area> obtenerAreaPorId(Integer id) {
        return areaRepository.findById(id);
    }

    @Transactional
    public Area guardarArea(Area area) {
        return areaRepository.save(area);
    }
}