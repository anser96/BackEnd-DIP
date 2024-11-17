package com.diplomado.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class InvitadoDTO {
    private int idInvitado;
    private String nombre;
    private String dependencia;
    private String email;
    private int numCedula;

}
