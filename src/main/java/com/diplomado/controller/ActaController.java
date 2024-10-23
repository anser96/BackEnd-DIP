package com.diplomado.controller;

import com.diplomado.model.Acta;
import com.diplomado.service.ActaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/actas")
public class ActaController {

    @Autowired
    private ActaService actaService;

    @PostMapping
    public Acta crearActa(@RequestBody Acta acta) {
        return actaService.save(acta);
    }

    @PostMapping("/{actaId}/aprobar")
    public Acta aprobarActa(@PathVariable int actaId) {
        return actaService.aprobar(actaId);
    }
}

