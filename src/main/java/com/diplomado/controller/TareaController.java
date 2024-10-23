package com.diplomado.controller;

import com.diplomado.model.Tarea;
import com.diplomado.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @PostMapping
    public Tarea asignarTarea(@RequestBody Tarea tarea) {
        return tareaService.save(tarea);
    }

    @GetMapping
    public List<Tarea> getAllTareas() {
        return tareaService.findAll();
    }
}

