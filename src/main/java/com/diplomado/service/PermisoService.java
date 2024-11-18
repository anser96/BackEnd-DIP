package com.diplomado.service;

import com.diplomado.model.dto.PermisosRolDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermisoService {

    // Listado completo de tablas
    private final List<String> todasLasTablas = Arrays.asList(
            "solicitud", "actas", "sesion", "orden_sesion", "proposiciones",
            "tareas", "invitados", "miembros", "descripcion",
            "asistencia_miembros", "asistencia_invitado"
    );

    public PermisosRolDTO obtenerPermisosPorRol(String rol) {
        Map<String, Map<String, Boolean>> permisos = switch (rol.toUpperCase()) {
            case "ESTUDIANTE" -> estudiantePermisos();
            case "INVITADO" -> invitadoPermisos();
            case "INTEGRANTECOMITE" -> integranteComitePermisos();
            case "ADMINISTRADOR" -> administradorPermisos();
            case "SECRETARIO" -> secretarioPermisos();
            case "DOCENTE" -> docentePermisos();
            case "PRESIDENTE" -> presidentePermisos();
            case "MODERADOR" -> moderadorPermisos();
            default -> throw new IllegalArgumentException("Rol no reconocido: " + rol);
        };

        // Asegurarse de incluir todas las tablas con permisos false si no están presentes
        completarPermisosConFalsos(permisos);

        return PermisosRolDTO.builder()
                .rol(rol)
                .permisos(permisos)
                .build();
    }

    private void completarPermisosConFalsos(Map<String, Map<String, Boolean>> permisos) {
        for (String tabla : todasLasTablas) {
            permisos.putIfAbsent(tabla, crearPermisos(false, false, false, false));
        }
    }

    private Map<String, Map<String, Boolean>> estudiantePermisos() {
        Map<String, Map<String, Boolean>> permisos = new HashMap<>();
        permisos.put("solicitud", crearPermisos(true, true, false, false));
        permisos.put("actas", crearPermisos(true, false, false, false));
        permisos.put("sesion", crearPermisos(true, false, false, false));
        permisos.put("orden_sesion", crearPermisos(true, false, false, false));
        permisos.put("tareas", crearPermisos(true, false, false, false));
        return permisos;
    }

    private Map<String, Map<String, Boolean>> invitadoPermisos() {
        Map<String, Map<String, Boolean>> permisos = new HashMap<>();
        permisos.put("solicitud", crearPermisos(true, true, false, false));
        permisos.put("asistencia_invitado", crearPermisos(true, false, true, false));
        permisos.put("actas", crearPermisos(true, false, false, false));
        permisos.put("sesion", crearPermisos(true, false, false, false));
        permisos.put("orden_sesion", crearPermisos(true, false, false, false));
        return permisos;
    }

    private Map<String, Map<String, Boolean>> integranteComitePermisos() {
        Map<String, Map<String, Boolean>> permisos = new HashMap<>();
        permisos.put("solicitud", crearPermisos(true, true, true, true));
        permisos.put("actas", crearPermisos(true, true, true, true));
        permisos.put("sesion", crearPermisos(true, true, true, false));
        permisos.put("orden_sesion", crearPermisos(true, true, true, true));
        permisos.put("proposiciones", crearPermisos(true, true, true, true));
        permisos.put("tareas", crearPermisos(true, true, true, true));
        permisos.put("invitados", crearPermisos(true, false, true, false));
        permisos.put("miembros", crearPermisos(true, false, true, false));
        permisos.put("descripcion", crearPermisos(true, false, true, false));
        permisos.put("asistencia_miembros", crearPermisos(true,true,true,false));
        permisos.put("asistencia_invitado", crearPermisos(true,true,true,false));
        return permisos;
    }

    private Map<String, Map<String, Boolean>> administradorPermisos() {
        Map<String, Map<String, Boolean>> permisos = new HashMap<>();
        for (String tabla : todasLasTablas) {
            permisos.put(tabla, crearPermisos(true, true, true, true));
        }
        return permisos;
    }

    private Map<String, Map<String, Boolean>> secretarioPermisos() {
        Map<String, Map<String, Boolean>> permisos = new HashMap<>();
        permisos.put("actas", crearPermisos(true, true, true, true));
        permisos.put("sesion", crearPermisos(true, true, true, false));
        permisos.put("orden_sesion", crearPermisos(true, true, true, true));
        permisos.put("tareas", crearPermisos(true, true, true, true));
        return permisos;
    }

    private Map<String, Map<String, Boolean>> docentePermisos() {
        Map<String, Map<String, Boolean>> permisos = new HashMap<>();
        permisos.put("solicitud", crearPermisos(true, true, false, false));
        permisos.put("actas", crearPermisos(true, false, false, false));
        permisos.put("sesion", crearPermisos(true, false, false, false));
        permisos.put("orden_sesion", crearPermisos(true, false, false, false));
        permisos.put("proposiciones", crearPermisos(true, false, false, false));
        return permisos;
    }

    private Map<String, Map<String, Boolean>> presidentePermisos() {
        Map<String, Map<String, Boolean>> permisos = new HashMap<>();
        permisos.put("solicitud", crearPermisos(true, true, true, true));
        permisos.put("actas", crearPermisos(true, true, true, true));
        permisos.put("sesion", crearPermisos(true, true, true, false));
        permisos.put("orden_sesion", crearPermisos(true, true, true, true));
        permisos.put("proposiciones", crearPermisos(true, true, true, true));
        permisos.put("tareas", crearPermisos(true, true, true, true));
        permisos.put("invitados", crearPermisos(true, false, true, false));
        permisos.put("miembros", crearPermisos(true, false, true, false));
        permisos.put("descripcion", crearPermisos(true, false, true, false));
        permisos.put("asistencia_miembros", crearPermisos(true,true,true,false));
        permisos.put("asistencia_invitado", crearPermisos(true,true,true,false));
        return permisos;
    }

    private Map<String, Map<String, Boolean>> moderadorPermisos() {
        Map<String, Map<String, Boolean>> permisos = new HashMap<>();
        permisos.put("sesion", crearPermisos(true, true, true, false));
        permisos.put("asistencia_miembros", crearPermisos(true, false, true, false));
        permisos.put("asistencia_invitado", crearPermisos(true, false, true, false));
        return permisos;
    }

    private Map<String, Boolean> crearPermisos(boolean consultar, boolean crear, boolean actualizar, boolean eliminar) {
        Map<String, Boolean> permisos = new HashMap<>();
        permisos.put("consultar", consultar);
        permisos.put("crear", crear);
        permisos.put("actualizar", actualizar);
        permisos.put("eliminar", eliminar);
        return permisos;
    }
}
