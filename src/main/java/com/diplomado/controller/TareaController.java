package com.diplomado.controller;

import com.diplomado.model.ApiResponse;
import com.diplomado.model.Tarea;
import com.diplomado.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponse<List<Tarea>>> getAllTareas() {
        List<Tarea> tareas = tareaService.findAll();

        // Crear una respuesta estándar con la lista de tareas
        ApiResponse<List<Tarea>> response = new ApiResponse<>("success", "Lista de tareas obtenida con éxito", tareas);
        return ResponseEntity.ok(response);
    }
}
