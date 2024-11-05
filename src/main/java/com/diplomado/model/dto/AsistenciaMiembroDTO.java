package com.diplomado.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AsistenciaMiembroDTO {
    private int idMiembro;
    private String nombre;
    private String cargo;
    private String email;
    private String estadoAsistencia;
    private String excusa;
}
