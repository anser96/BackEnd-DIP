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
        return solicitudService.crearSolicitud(solicitud);
    }

    @PostMapping("/{solicitudId}/responder")
    public Solicitud responderSolicitud(@PathVariable int solicitudId, @RequestBody String respuesta) {
        return solicitudService.responder(solicitudId, respuesta);
    }


    @GetMapping
    public List<Solicitud> getAll(){
        return solicitudService.findAll();
    }
}

