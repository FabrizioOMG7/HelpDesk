package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Usuario;
import com.utpintegrador.helpdesk.repository.TicketRepository;
import com.utpintegrador.helpdesk.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/admin/total")
    public Map<String, Long> getAdminTotal() {
        long total = ticketRepository.count();
        return Collections.singletonMap("TOTAL", total);
    }

    @PostMapping("/admin/totalabierto")
    public Map<String, Long> getAdminTotalAbierto() {
        long total = ticketRepository.countByEstado(true); // true = Abierto
        return Collections.singletonMap("TOTAL", total);
    }

    @PostMapping("/admin/totalcerrado")
    public Map<String, Long> getAdminTotalCerrado() {
        long total = ticketRepository.countByEstado(false); // false = Cerrado
        return Collections.singletonMap("TOTAL", total);
    }

    @PostMapping("/usuario/total")
    public Map<String, Long> getUsuarioTotal(@RequestParam("usu_id") Integer usuId) {
        Usuario usuario = usuarioRepository.findById(usuId).orElseThrow();
        long total = ticketRepository.countByUsuario(usuario);
        return Collections.singletonMap("TOTAL", total);
    }

    @PostMapping("/usuario/totalabierto")
    public Map<String, Long> getUsuarioTotalAbierto(@RequestParam("usu_id") Integer usuId) {
        Usuario usuario = usuarioRepository.findById(usuId).orElseThrow();
        long total = ticketRepository.countByUsuarioAndEstado(usuario, true);
        return Collections.singletonMap("TOTAL", total);
    }

    @PostMapping("/usuario/totalcerrado")
    public Map<String, Long> getUsuarioTotalCerrado(@RequestParam("usu_id") Integer usuId) {
        Usuario usuario = usuarioRepository.findById(usuId).orElseThrow();
        long total = ticketRepository.countByUsuarioAndEstado(usuario, false);
        return Collections.singletonMap("TOTAL", total);
    }
}