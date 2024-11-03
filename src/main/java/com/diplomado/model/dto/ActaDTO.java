package com.diplomado.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActaDTO {
    private int idActa;
    private String numeroActa;
    private String estado;
    private int sesionId;

    private SesionDTO sesion;
}
