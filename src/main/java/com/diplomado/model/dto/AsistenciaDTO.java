package com.diplomado.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaDTO {
    private int idPersona;           // ID del miembro o invitado
    private String tipo;             // Tipo de persona ("miembro" o "invitado")
    private String estadoAsistencia; // Estado de asistencia ("ASISTIÓ", "NO ASISTIÓ", "EXCUSA")
    private String excusa;
}
