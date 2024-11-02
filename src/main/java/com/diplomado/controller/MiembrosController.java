package com.diplomado.controller;

import com.diplomado.model.Invitado;
import com.diplomado.model.Miembro;
import com.diplomado.repository.MiembroRepository;
import com.diplomado.service.MiembroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/miembros")
public class MiembrosController {

    @Autowired
    private MiembroService miembroService;

    @PostMapping
    public Miembro crearMiembro(@RequestBody Miembro miembro) {
        return miembroService.save(miembro);
    }

    // Obtener todas las descripciones
    @GetMapping
    public List<Miembro> obtenerMiembro() {
        return miembroService.findAllMiembros();
    }

    // Obtener una descripción por ID
    @GetMapping("/{id}")
    public Miembro obtenerMiembroPorId(@PathVariable int id) {
        return miembroService.findById(id);
    }

    // Eliminar una descripción
    @DeleteMapping("/{id}")
    public void eliminarMiembro(@PathVariable int id) {
        miembroService.deleteById(id);
    }
}
