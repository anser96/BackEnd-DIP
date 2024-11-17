package com.diplomado.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AsistenciaInvitadoDTO {
    private int idInvitado;
    private String nombre;
    private int numCedula;
    private String dependencia;
    private String email;
    private String estadoAsistencia;
    private String excusa;
}
