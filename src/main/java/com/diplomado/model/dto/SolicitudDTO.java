package com.diplomado.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SolicitudDTO {
    private int idSolicitud;
    private String dependencia;
    private String asunto;
    private String descripcion;
    private Date fechaDeSolicitud;
    private String respuesta;
    private String estado;
    private String solicitanteNombre;

    // Getters y Setters (puedes generarlos con Lombok si prefieres)
}
