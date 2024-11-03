package com.diplomado.model.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class ActaDTO {
    private int idActa;
    private String numeroActa;
    private String estado;
    private int sesionId;

    private SesionDTO sesion;
}
