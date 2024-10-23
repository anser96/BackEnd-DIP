package com.diplomado.controller;

import com.diplomado.model.Solicitud;
import com.diplomado.model.dto.SolicitudDTO;
import com.diplomado.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    @PostMapping
    public Solicitud crearSolicitud(@RequestBody Solicitud solicitud) {
        return solicitudService.save(solicitud);
    }

    @PostMapping("/{solicitudId}/responder")
    public Solicitud responderSolicitud(@PathVariable int solicitudId, @RequestBody String respuesta) {
        return solicitudService.responder(solicitudId, respuesta);
    }

    @GetMapping("/pendientes")
    public List<SolicitudDTO> getSolicitudesPendientes() {
        List<Solicitud> solicitudes = solicitudService.findByEstado("PENDIENTE");
        return solicitudes.stream()
                .map(s -> new SolicitudDTO(s.getIdSolicitud(), s.getDependencia(), s.getAsunto(), s.getEstado(), s.getFechaDeSolicitud(), s.getSolicitante().getNombre()))
                .collect(Collectors.toList());
    }
}

