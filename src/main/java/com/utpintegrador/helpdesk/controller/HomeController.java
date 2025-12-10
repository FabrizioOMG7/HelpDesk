package com.utpintegrador.helpdesk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String mostrarHome() {
        return "home";
    }

    @GetMapping("/nuevo-ticket")
    public String mostrarNuevoTicket() {
        return "nuevoticket";
    }

    @GetMapping("/mantenimiento/usuario")
    public String mostrarMntUsuario() {
        return "mntusuario";
    }

    @GetMapping("/mantenimiento/prioridad")
    public String mostrarMntPrioridad() {
        return "mntprioridad";
    }

    @GetMapping("/mantenimiento/categoria")
    public String mostrarMntCategoria() {
        return "mntcategoria";
    }

    @GetMapping("/mantenimiento/subcategoria")
    public String mostrarMntSubcategoria() {
        return "mntsubcategoria";
    }

    @GetMapping("/mantenimiento/area")
    public String mostrarMntArea() {
        return "mntarea";
    }

    @GetMapping("/consultartickect")
    public String mostrarConsultarTicket() {
        return "mntConsultarTicket";
    }
}