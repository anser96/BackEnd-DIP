package com.diplomado.controller;

import com.diplomado.model.ApiResponse;
import com.diplomado.service.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/asistencia")
public class AsistenciaController {
    @Autowired
    private SesionService asistenciaService;

    @PutMapping("/miembro")
    public ResponseEntity<ApiResponse<Void>> actualizarEstadoMiembro(
            @RequestParam int sesionId,
            @RequestParam int miembroId,
            @RequestParam String nuevoEstado) {

        boolean actualizado = asistenciaService.actualizarEstadoAsistenciaMiembro(sesionId, miembroId, nuevoEstado);
        if (actualizado) {
            return ResponseEntity.ok(new ApiResponse<>("success", "Estado de asistencia del miembro actualizado", null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("error", "No se encontró la asistencia del miembro", null));
        }
    }

    @PutMapping("/invitado")
    public ResponseEntity<ApiResponse<Void>> actualizarEstadoInvitado(
            @RequestParam int sesionId,
            @RequestParam int invitadoId,
            @RequestParam String nuevoEstado) {

        boolean actualizado = asistenciaService.actualizarEstadoAsistenciaInvitado(sesionId, invitadoId, nuevoEstado);
        if (actualizado) {
            return ResponseEntity.ok(new ApiResponse<>("success", "Estado de asistencia del invitado actualizado", null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("error", "No se encontró la asistencia del invitado", null));
        }
    }
}
