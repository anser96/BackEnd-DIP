package com.diplomado.controller;

import com.diplomado.model.Acta;
import com.diplomado.model.ApiResponse;
import com.diplomado.model.dto.ActaDTO;
import com.diplomado.service.ActaService;
import org.springframework.beans.factory.annotation.Autowired;
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
        Acta actaAprobada = actaService.aprobar(actaId);
        ApiResponse<Acta> response = new ApiResponse<>("success", "Acta aprobada exitosamente", actaAprobada);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ActaDTO>> obtenerActas() {
        List<ActaDTO> actas = actaService.listarActas();
        return ResponseEntity.ok(actas);
    }
}
