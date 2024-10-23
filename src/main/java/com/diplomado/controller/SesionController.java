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

        // Actualizar el contenido de la sesión
        Sesion sesion = sesionService.definirContenido(sesionId, contenido);

        // Convertir la entidad Sesion en DTO con todos los datos asociados
        return sesionService.convertToDTO(sesion);
    }

    @GetMapping
    public List<SesionDTO> getSesiones() {
        List<Sesion> sesiones = sesionService.findAll();

        // Convertir cada Sesion en un SesionDTO con toda la información relacionada
        return sesiones.stream()
                .map(sesion -> sesionService.convertToDTO(sesion))
                .collect(Collectors.toList());
    }
}

