package com.diplomado.controller;

import com.diplomado.model.Invitado;
import com.diplomado.model.Miembro;
import com.diplomado.model.Sesion;
import com.diplomado.service.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
public class SesionController {

    @Autowired
    private SesionService sesionService;

    @PostMapping
    public Sesion crearSesion(@RequestBody Sesion sesion) {
        return sesionService.save(sesion);
    }

    @PostMapping("/{sesionId}/invitados")
    public void programarInvitados(@PathVariable int sesionId, @RequestBody List<Invitado> invitados) {
        sesionService.agregarInvitados(sesionId, invitados);
    }

    @PostMapping("/{sesionId}/miembros")
    public void citarMiembros(@PathVariable int sesionId, @RequestBody List<Miembro> miembros) {
        sesionService.citarMiembros(sesionId, miembros);
    }

    @PostMapping("/{sesionId}/definir-contenido")
    public Sesion definirContenido(@PathVariable int sesionId, @RequestBody String contenido) {
        return sesionService.definirContenido(sesionId, contenido);
    }

    @GetMapping
    public List<Sesion> getAllSesiones() {
        return sesionService.findAll();
    }
}

