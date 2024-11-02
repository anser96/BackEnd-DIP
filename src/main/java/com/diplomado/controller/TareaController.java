package com.diplomado.controller;

import com.diplomado.model.ApiResponse;
import com.diplomado.model.Tarea;
import com.diplomado.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @PostMapping
    public ResponseEntity<ApiResponse<Tarea>> asignarTarea(@RequestBody Tarea tarea) {
        Tarea nuevaTarea = tareaService.save(tarea);

        // Crear una respuesta estándar con la tarea creada
        ApiResponse<Tarea> response = new ApiResponse<>("success", "Tarea asignada con éxito", nuevaTarea);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Tarea>> obtenerTareasConResponsables() {
        List<Tarea> tareas = tareaService.obtenerTareasConResponsables();
        return ResponseEntity.ok(tareas);
    }

    @PostMapping("/asignar")
    public ResponseEntity<Tarea> asignarTarea(
            @RequestParam String descripcion,
            @RequestParam String tipoResponsable,
            @RequestParam int responsableId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEntrega,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaVerificacion) {

        Tarea tarea = tareaService.asignarTarea(descripcion, tipoResponsable, responsableId, fechaEntrega, fechaVerificacion);
        return ResponseEntity.ok(tarea);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Tarea> actualizarEstado(
            @PathVariable int id,
            @RequestParam String estado) {

        Optional<Tarea> tareaOpt = tareaService.actualizarEstado(id, estado);
        return tareaOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
