package com.diplomado.controller;

import com.diplomado.model.Invitado;
import com.diplomado.model.Miembro;
import com.diplomado.model.Sesion;
import com.diplomado.model.dto.SesionDTO;
import com.diplomado.service.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public SesionDTO definirContenido(@PathVariable int sesionId, @RequestBody Map<String, String> requestBody) {
        String contenido = requestBody.get("contenido");
        Sesion sesion = sesionService.definirContenido(sesionId, contenido);
        return new SesionDTO(sesion.getIdSesion(), sesion.getLugar(), sesion.getFecha(), sesion.getContenido());
    }

    @GetMapping
    public List<SesionDTO> getSesiones() {
        List<Sesion> sesiones = sesionService.findAll();
        return sesiones.stream()
                .map(s -> new SesionDTO(s.getIdSesion(), s.getLugar(), s.getFecha(), s.getContenido()))
                .collect(Collectors.toList());
    }
}

