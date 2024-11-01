package com.diplomado.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class SesionDTO {
    private int idSesion;
    private String lugar;
    private LocalDate fecha;
    private String contenido;
    private LocalTime horaInicio;
    private LocalTime horaFinal;
    private String presidente;
    private String secretario;

    // Listas de asistencia de miembros e invitados
    private List<AsistenciaMiembroDTO> asistenciaMiembros;
    private List<AsistenciaInvitadoDTO> asistenciaInvitados;

    // Lista de actas relacionadas a la sesión
    private List<ActaDTO> actas;
}

