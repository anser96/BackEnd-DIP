package com.diplomado.service;

import com.diplomado.model.dto.PermisosRolDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PermisoService {

    public PermisosRolDTO obtenerPermisosPorRol(String rol) {
        Map<String, Map<String, Boolean>> permisos = new HashMap<>();

        switch (rol.toUpperCase()) {
            case "ESTUDIANTE":
                permisos.put("solicitud", crearPermisos(true, true, false, false));
                permisos.put("actas", crearPermisos(true, false, false, false));
                permisos.put("sesion", crearPermisos(true, false, false, false));
                permisos.put("orden_sesion", crearPermisos(true, false, false, false));
                permisos.put("tareas", crearPermisos(true, false, false, false));
                permisos.put("invitados", crearPermisos(false, false, false, false));
                permisos.put("miembros", crearPermisos(false, false, false, false));
                permisos.put("descripcion", crearPermisos(false, false, false, false));
                break;

            case "INVITADO":
                permisos.put("solicitud", crearPermisos(true, false, false, false));
                permisos.put("asistencia_invitado", crearPermisos(true, false, true, false));
                permisos.put("actas", crearPermisos(true, false, false, false));
                permisos.put("sesion", crearPermisos(true, false, false, false));
                permisos.put("orden_sesion", crearPermisos(true, false, false, false));
                permisos.put("tareas", crearPermisos(false, false, false, false));
                permisos.put("miembros", crearPermisos(false, false, false, false));
                permisos.put("descripcion", crearPermisos(false, false, false, false));
                break;

            case "INTEGRANTECOMITE":
                permisos.put("solicitud", crearPermisos(true, true, true, true));
                permisos.put("actas", crearPermisos(true, true, true, true));
                permisos.put("sesion", crearPermisos(true, true, true, false));
                permisos.put("orden_sesion", crearPermisos(true, true, true, true));
                permisos.put("proposiciones", crearPermisos(true, true, true, true));
                permisos.put("tareas", crearPermisos(true, true, true, true));
                permisos.put("invitados", crearPermisos(true, false, true, false));
                permisos.put("miembros", crearPermisos(true, false, true, false));
                permisos.put("descripcion", crearPermisos(true, false, true, false));
                break;

            case "ADMINISTRADOR":
                permisos.put("todas_las_tablas", crearPermisos(true, true, true, true));
                break;

            case "SECRETARIO":
                permisos.put("actas", crearPermisos(true, true, true, true));
                permisos.put("sesion", crearPermisos(true, true, true, false));
                permisos.put("orden_sesion", crearPermisos(true, true, true, true));
                permisos.put("tareas", crearPermisos(true, true, true, true));
                break;

            case "DOCENTE":
                permisos.put("solicitud", crearPermisos(true, true, false, false));
                permisos.put("actas", crearPermisos(true, false, false, false));
                permisos.put("sesion", crearPermisos(true, false, false, false));
                permisos.put("orden_sesion", crearPermisos(true, false, false, false));
                permisos.put("proposiciones", crearPermisos(true, false, false, false));
                break;

            case "PRESIDENTE":
                permisos.put("solicitud", crearPermisos(true, true, true, true));
                permisos.put("actas", crearPermisos(true, true, true, true));
                permisos.put("sesion", crearPermisos(true, true, true, false));
                permisos.put("orden_sesion", crearPermisos(true, true, true, true));
                permisos.put("proposiciones", crearPermisos(true, true, true, true));
                permisos.put("tareas", crearPermisos(true, true, true, true));
                permisos.put("invitados", crearPermisos(true, false, true, false));
                permisos.put("miembros", crearPermisos(true, false, true, false));
                permisos.put("descripcion", crearPermisos(true, false, true, false));
                break;

            case "MODERADOR":
                permisos.put("sesion", crearPermisos(true, true, true, false));
                permisos.put("asistencia_miembros", crearPermisos(true, false, true, false));
                permisos.put("asistencia_invitado", crearPermisos(true, false, true, false));
                break;

            default:
                throw new IllegalArgumentException("Rol no reconocido: " + rol);
        }

        return PermisosRolDTO.builder()
                .rol(rol)
                .permisos(permisos)
                .build();
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
