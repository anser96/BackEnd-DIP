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
    // Información del solicitante
    private String tipoSolicitante;
    private int idSolicitante;
    private String nombreSolicitante;

    // Getters y Setters (puedes generarlos con Lombok si prefieres)
}
