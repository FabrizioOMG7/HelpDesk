package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Evidencia; // <-- NUEVO: Necesitamos el objeto Evidencia
import com.utpintegrador.helpdesk.model.Ticket;
import com.utpintegrador.helpdesk.service.DetalleEvidenciaService; // <-- NUEVO
import com.utpintegrador.helpdesk.service.DetalleTicketService;
import com.utpintegrador.helpdesk.service.EvidenciaService;
import com.utpintegrador.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final DetalleTicketService detalleTicketService;
    private final EvidenciaService evidenciaService;
    private final DetalleEvidenciaService detalleEvidenciaService;

    @Value("${app.upload.dir}")
    private String uploadRootDir;

    @Autowired
    public TicketController(TicketService ticketService,
                            DetalleTicketService detalleTicketService,
                            EvidenciaService evidenciaService,
                            DetalleEvidenciaService detalleEvidenciaService) {
        this.ticketService = ticketService;
        this.detalleTicketService = detalleTicketService;
        this.evidenciaService = evidenciaService;
        this.detalleEvidenciaService = detalleEvidenciaService;
    }


    @GetMapping
    public List<Ticket> obtenerTickets() {
        return ticketService.obtenerTodosLosTickets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> obtenerPorId(@PathVariable Integer id) {
        try {
            Ticket ticket = ticketService.obtenerTicketPorId(id);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/insertar")
    public ResponseEntity<?> guardarTicket(
            @RequestParam("usu_id") Integer usuId,
            @RequestParam("cats_id") Integer subCatId,
            @RequestParam("prio_id") Integer prioId,
            @RequestParam("tick_titulo") String titulo,
            @RequestParam("tick_descrip") String descripcion,
            @RequestParam(value = "files[]", required = false) MultipartFile[] files
    ) {
        Evidencia evidenciaCreada = null;

        try {
            Ticket nuevoTicket = ticketService.crearTicket(usuId, subCatId, prioId, titulo);

            final Integer ESTADO_SIN_ASIGNAR_ID = 100;

            detalleTicketService.crearDetalleTicket(
                    descripcion,
                    nuevoTicket.getCodigoTicket(),
                    null,
                    ESTADO_SIN_ASIGNAR_ID
            );

            if (files != null && files.length > 0 && !files[0].isEmpty()) {

                String nombreCarpetaTicket = "Ticket_" + nuevoTicket.getCodigoTicket();
                Path ticketUploadPath = Paths.get(uploadRootDir).resolve(nombreCarpetaTicket);

                evidenciaCreada = evidenciaService.guardarEvidencia(
                        nombreCarpetaTicket,
                        nuevoTicket.getCodigoTicket()
                );

                if (!Files.exists(ticketUploadPath)) {
                    Files.createDirectories(ticketUploadPath);
                }

                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        try {
                            String originalFileName = file.getOriginalFilename();
                            String fileExtension = "";
                            if (originalFileName != null && originalFileName.contains(".")) {
                                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                            }
                            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

                            Path filePath = ticketUploadPath.resolve(uniqueFileName);

                            Files.copy(file.getInputStream(), filePath);

                            detalleEvidenciaService.guardarDetalleArchivo(
                                    uniqueFileName,
                                    evidenciaCreada.getCodigoEvidencia()
                            );

                            System.out.println("Archivo guardado: " + filePath.toString());

                        } catch (IOException e) {
                            System.err.println("Error al guardar el archivo " + file.getOriginalFilename() + ": " + e.getMessage());
                        }
                    }
                }
            }

            Map<String, Integer> response = Map.of("tick_id", nuevoTicket.getCodigoTicket());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al crear el ticket: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor al procesar la solicitud.");
        }

    }

}