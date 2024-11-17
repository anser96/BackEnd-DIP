package com.diplomado.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class PermisosRolDTO {
    private String rol; // Ejemplo: ESTUDIANTE, INVITADO, etc.
    private Map<String, Map<String, Boolean>> permisos;
    // Estructura: {"solicitud": {"consultar": true, "crear": true, "actualizar": false, "eliminar": false}, ...}
}