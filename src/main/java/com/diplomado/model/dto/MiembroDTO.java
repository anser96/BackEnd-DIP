package com.diplomado.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MiembroDTO {
    private int idMiembro;
    private String nombre;
    private String cargo;
    private String email;
}
