package com.diplomado.controller;


import com.diplomado.model.Descripcion;
import com.diplomado.model.Invitado;
import com.diplomado.service.InvitadoService;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invitados")
public class InvitadoController {

    @Autowired
    private InvitadoService invitadoService;

    // Crear una nueva descripción
    @PostMapping
    public Invitado crearInvitados(@RequestBody Invitado invitado) {
        return invitadoService.save(invitado);
    }

    // Obtener todas las descripciones
    @GetMapping
    public List<Invitado> obtenerInvitados() {
        return invitadoService.findAllInitados();
    }

    // Obtener una descripción por ID
    @GetMapping("/{id}")
    public Invitado obtenerInvitadoPorId(@PathVariable int id) {
        return invitadoService.findById(id);
    }

    // Eliminar una descripción
    @DeleteMapping("/{id}")
    public void eliminarInvitado(@PathVariable int id) {
        invitadoService.deleteById(id);
    }
}
