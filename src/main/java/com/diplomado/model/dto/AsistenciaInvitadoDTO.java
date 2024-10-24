package com.diplomado.model.dto;

import lombok.Data;

@Data
public class AsistenciaInvitadoDTO {
    private int idInvitado;
    private String nombre;
    private String dependencia;
    private String estadoAsistencia;
}
