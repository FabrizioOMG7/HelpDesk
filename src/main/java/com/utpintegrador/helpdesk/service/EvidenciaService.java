package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Evidencia;
import com.utpintegrador.helpdesk.model.Ticket;
import com.utpintegrador.helpdesk.repository.EvidenciaRepository;
import com.utpintegrador.helpdesk.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EvidenciaService {

    private final EvidenciaRepository evidenciaRepository;
    private final TicketRepository ticketRepository;

    @Value("${app.upload.dir}")
    private String uploadRootDir;

    @Autowired
    public EvidenciaService(EvidenciaRepository evidenciaRepository, TicketRepository ticketRepository) {
        this.evidenciaRepository = evidenciaRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<Evidencia> obtenerEvidencias() {
        return evidenciaRepository.findAll();
    }
    public Optional<Evidencia> obtenerEvidenciaPorId(Integer id) {
        return evidenciaRepository.findById(id);
    }


    public Evidencia guardarEvidencia(String nombreCarpeta, Integer ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con id: " + ticketId));

        Path rutaCompletaPath = Paths.get(uploadRootDir).resolve(nombreCarpeta);
        String rutaCompletaString = rutaCompletaPath.toString() + System.getProperty("file.separator");

        Evidencia evidencia = new Evidencia();
        evidencia.setRutaEvidencia(rutaCompletaString);
        evidencia.setTicket(ticket);
        evidencia.setFechaSubida(LocalDate.now());

        return evidenciaRepository.save(evidencia);
    }

}