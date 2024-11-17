package com.diplomado.controller;

import com.diplomado.model.dto.PermisosRolDTO;
import com.diplomado.service.PermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class PermisoController {

    @Autowired
    private PermisoService permisoService;

    @GetMapping("/permisos")
    public ResponseEntity<PermisosRolDTO> obtenerPermisosPorRol(@RequestParam String rol) {
        PermisosRolDTO permisos = permisoService.obtenerPermisosPorRol(rol);
        return ResponseEntity.ok(permisos);
    }
}
