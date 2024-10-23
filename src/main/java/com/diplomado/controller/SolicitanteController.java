package com.diplomado.controller;

import com.diplomado.model.Solicitante;
import com.diplomado.service.SolicitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitantes")
public class SolicitanteController {

    @Autowired
    private SolicitanteService solicitanteService;

    // Crear un nuevo solicitante
    @PostMapping
    public Solicitante crearSolicitante(@RequestBody Solicitante solicitante) {
        return solicitanteService.save(solicitante);
    }

    // Obtener todos los solicitantes
    @GetMapping
    public List<Solicitante> obtenerSolicitantes() {
        return solicitanteService.findAll();
    }

    // Obtener un solicitante por ID
    @GetMapping("/{id}")
    public Solicitante obtenerSolicitantePorId(@PathVariable int id) {
        return solicitanteService.findById(id);
    }

    // Actualizar un solicitante
    @PutMapping("/{id}")
    public Solicitante actualizarSolicitante(@PathVariable int id, @RequestBody Solicitante solicitanteActualizado) {
        return solicitanteService.updateSolicitante(id, solicitanteActualizado);
    }

    // Eliminar un solicitante
    @DeleteMapping("/{id}")
    public void eliminarSolicitante(@PathVariable int id) {
        solicitanteService.deleteById(id);
    }
}

