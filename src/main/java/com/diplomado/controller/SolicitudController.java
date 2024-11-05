package com.diplomado.controller;

import com.diplomado.model.ApiResponse;
import com.diplomado.model.Solicitud;
import com.diplomado.model.dto.SolicitudDTO;
import com.diplomado.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public List<SolicitudDTO> getAll(){
        return solicitudService.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> obtenerSolicitudPorId(@PathVariable int id) {
        SolicitudDTO solicitudDTO = solicitudService.findSolicitudById(id);
        return ResponseEntity.ok(solicitudDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudDTO> editarSolicitud(@PathVariable int id, @RequestBody SolicitudDTO solicitudDTO) {
        SolicitudDTO solicitudActualizada = solicitudService.editarSolicitud(id, solicitudDTO);
        return ResponseEntity.ok(solicitudActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSolicitud(@PathVariable int id){
         solicitudService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Solicitud eliminada con éxito", null));
    }
}

