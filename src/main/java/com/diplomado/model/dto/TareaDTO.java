package com.diplomado.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TareaDTO {
    private int idTarea;
    private String descripcion;
    private LocalDate fechaEntrega;
    private LocalDate fechaVerificacion;
    private String tipoResponsable;
    private int responsableId;
    private String estado;
    private Object responsable;
}
