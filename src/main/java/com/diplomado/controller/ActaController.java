package com.diplomado.controller;

import com.diplomado.model.Acta;
import com.diplomado.model.ApiResponse;
import com.diplomado.model.dto.ActaDTO;
import com.diplomado.service.ActaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actas")
public class ActaController {

    @Autowired
    private ActaService actaService;


    @PostMapping
    public ResponseEntity<ApiResponse<Acta>> crearActa(@RequestBody Acta acta) {
        Acta nuevaActa = actaService.save(acta);
        ApiResponse<Acta> response = new ApiResponse<>("success", "Acta creada exitosamente", nuevaActa);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{actaId}/aprobar")
    public ResponseEntity<ApiResponse<Acta>> aprobarActa(@PathVariable int actaId) {
        try {
            Acta actaAprobada = actaService.aprobar(actaId);
            ApiResponse<Acta> response = new ApiResponse<>("success", "Acta aprobada exitosamente", actaAprobada);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<Acta> errorResponse = new ApiResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ActaDTO>>> obtenerActas() {
        List<ActaDTO> actas = actaService.listarActas();
        ApiResponse<List<ActaDTO>> response = new ApiResponse<>("success", "Lista de actas obtenida exitosamente", actas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActaDTO>> obtenerActaPorId(@PathVariable int id) {
        return actaService.obtenerActaPorId(id)
                .map(actaDTO -> {
                    ApiResponse<ActaDTO> response = new ApiResponse<>("success", "Acta obtenida con éxito", actaDTO);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponse<ActaDTO> response = new ApiResponse<>("error", "Acta no encontrada", null);
                    return ResponseEntity.status(404).body(response);
                });
    }
}
