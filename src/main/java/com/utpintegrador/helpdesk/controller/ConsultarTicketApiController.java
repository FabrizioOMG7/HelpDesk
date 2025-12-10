package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.dto.TicketConsultarDTO;
import com.utpintegrador.helpdesk.model.*;
import com.utpintegrador.helpdesk.repository.TicketRepository;
import com.utpintegrador.helpdesk.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/consultarticket")
public class ConsultarTicketApiController {

    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
    private final DetalleTicketService detalleTicketService;
    private final UsuarioService usuarioService;

    @Autowired
    public ConsultarTicketApiController(TicketService ticketService,
                                        TicketRepository ticketRepository,
                                        DetalleTicketService detalleTicketService,
                                        UsuarioService usuarioService) {
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
        this.detalleTicketService = detalleTicketService;
        this.usuarioService = usuarioService;
    }

    // 1. LISTAR CON FILTROS
    @GetMapping
    public ResponseEntity<Map<String, List<TicketConsultarDTO>>> listarTickets(
            @RequestParam(value = "tick_titulo", required = false) String titulo,
            @RequestParam(value = "cat_id", required = false) Integer catId,
            @RequestParam(value = "prio_id", required = false) Integer prioId
    ) {
        List<Ticket> tickets;

        if ((titulo == null || titulo.isEmpty()) && catId == null && prioId == null) {
            tickets = ticketService.obtenerTodosLosTickets();
        } else {
            String tituloQuery = (titulo != null && !titulo.isEmpty()) ? titulo : null;
            tickets = ticketRepository.filtrarTickets(tituloQuery, catId, prioId);
        }

        List<DetalleTicket> detalles = detalleTicketService.obtenerTodosLosDetalles();
        List<TicketConsultarDTO> listaDto = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Ticket t : tickets) {
            TicketConsultarDTO dto = new TicketConsultarDTO();
            dto.setCodigoTicket(t.getCodigoTicket());
            dto.setTitulo(t.getTitulo());

            // --- Categoria ---
            if (t.getSubCategoria() != null && t.getSubCategoria().getCategoria() != null) {
                dto.setNombreCategoria(t.getSubCategoria().getCategoria().getNombreCategoria());
            } else {
                dto.setNombreCategoria("---");
            }

            // --- Prioridad ---
            if (t.getPrioridad() != null) {
                dto.setNombrePrioridad(t.getPrioridad().getNombrePrioridad());
            } else {
                dto.setNombrePrioridad("---");
            }

            // --- Fechas ---
            dto.setFechaCreacion(t.getFechaCreacion() != null ? t.getFechaCreacion().format(formatter) : "");
            dto.setFechaCierre(t.getFechaCierre() != null ? t.getFechaCierre().format(formatter) : "");

            // --- Lógica del Último Detalle ---
            DetalleTicket ultimoDetalle = detalles.stream()
                    .filter(d -> d.getTicket().getCodigoTicket().equals(t.getCodigoTicket()))
                    .max(Comparator.comparing(DetalleTicket::getCodigoDetalleDeTicket))
                    .orElse(null);

            if (ultimoDetalle != null) {
                dto.setFechaAsignacion(ultimoDetalle.getFechaAsignacion() != null ? ultimoDetalle.getFechaAsignacion().toString() : "");

                // Estado Nombre (Esto ya no dará error con el DTO actualizado)
                if(ultimoDetalle.getEstadoTicket() != null){
                    dto.setEstadoNombre(ultimoDetalle.getEstadoTicket().getNombreEstado());
                } else {
                    dto.setEstadoNombre("Desconocido");
                }

                if (ultimoDetalle.getUsuario() != null) {
                    dto.setNombreSoporte(ultimoDetalle.getUsuario().getNombres() + " " + ultimoDetalle.getUsuario().getApellidoPaterno());
                    dto.setSoporteId(ultimoDetalle.getUsuario().getCodigoUsuario());
                } else {
                    dto.setNombreSoporte("Sin Asignar");
                    dto.setSoporteId(null);
                }
            } else {
                dto.setFechaAsignacion("");
                dto.setEstadoNombre("Abierto");
                dto.setNombreSoporte("Sin Asignar");
                dto.setSoporteId(null);
            }

            listaDto.add(dto);
        }

        return ResponseEntity.ok(Map.of("data", listaDto));
    }

    // 2. ASIGNAR TÉCNICO
    @PostMapping("/asignar")
    public ResponseEntity<?> asignarTecnico(@RequestParam("tick_id") Integer ticketId,
                                            @RequestParam("usu_id_soporte") Integer tecnicoId) {
        try {
            // ID 101 = "Asignado" (Según tu BD)
            Integer ESTADO_ASIGNADO = 101;

            detalleTicketService.crearDetalleTicket("Ticket Asignado al Técnico", ticketId, tecnicoId, ESTADO_ASIGNADO);

            return ResponseEntity.ok(Map.of("message", "Ticket asignado correctamente"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Error al asignar ticket"));
        }
    }

    // 3. OBTENER TÉCNICOS (SOLUCIÓN AL COMBO VACÍO)
    @GetMapping("/tecnicos")
    public ResponseEntity<List<Usuario>> obtenerTecnicos() {

        List<Usuario> tecnicos = usuarioService.obtenerUsuarios().stream()
                .filter(u -> {
                    // Obtenemos el nombre del rol en minúsculas
                    String nombreRol = u.getRol().getNombreRol().toLowerCase();

                    // Aceptamos si el rol contiene "soporte", "tecnico" o "admin"
                    // Esto evita problemas si el ID cambia.
                    boolean esTecnico = nombreRol.contains("empleado") ||
                            nombreRol.contains("tecnico") ||
                            nombreRol.contains("admin");

                    return esTecnico && u.isEstado();
                })
                .collect(Collectors.toList());

        tecnicos.forEach(t -> t.setPasswoord(null));
        return ResponseEntity.ok(tecnicos);
    }
}