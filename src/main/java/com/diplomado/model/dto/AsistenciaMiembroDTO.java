package com.diplomado.model.dto;

import lombok.Data;

@Data
public class AsistenciaMiembroDTO {
    private int idMiembro;
    private String nombre;
    private String cargo;
    private String email;
    private String estadoAsistencia;
}
