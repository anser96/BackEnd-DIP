package com.diplomado.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActaDTO {
    private int idActa;
    private String estado;
    private int idSesion;
}
