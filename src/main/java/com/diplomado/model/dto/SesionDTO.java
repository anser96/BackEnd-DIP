package com.diplomado.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SesionDTO {
    private int idSesion;
    private String lugar;
    private Date fecha;
    private String contenido;

    // Listas de asistencia de miembros e invitados
    private List<AsistenciaMiembroDTO> asistenciaMiembros;
    private List<AsistenciaInvitadoDTO> asistenciaInvitados;

    // Lista de actas relacionadas a la sesión
    private List<ActaDTO> actas;
}

