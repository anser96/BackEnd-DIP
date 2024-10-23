package com.diplomado.controller;

import com.diplomado.model.Descripcion;
import com.diplomado.service.DescripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/descripciones")
public class DescripcionController {

    @Autowired
    private DescripcionService descripcionService;

    // Crear una nueva descripción
    @PostMapping
    public Descripcion crearDescripcion(@RequestBody Descripcion descripcion) {
        return descripcionService.save(descripcion);
    }

    // Obtener todas las descripciones
    @GetMapping
    public List<Descripcion> obtenerDescripciones() {
        return descripcionService.findAll();
    }

    // Obtener una descripción por ID
    @GetMapping("/{id}")
    public Descripcion obtenerDescripcionPorId(@PathVariable int id) {
        return descripcionService.findById(id);
    }

    // Actualizar una descripción
    @PutMapping("/{id}")
    public Descripcion actualizarDescripcion(@PathVariable int id, @RequestBody Descripcion descripcionActualizada) {
        return descripcionService.updateDescripcion(id, descripcionActualizada);
    }

    // Eliminar una descripción
    @DeleteMapping("/{id}")
    public void eliminarDescripcion(@PathVariable int id) {
        descripcionService.deleteById(id);
    }
}

