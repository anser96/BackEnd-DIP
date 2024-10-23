package com.diplomado.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SolicitudDTO {
    private int idSolicitud;
    private String dependencia;
    private String asunto;
    private String estado;
    private Date fechaDeSolicitud;
    private String solicitanteNombre;

    // Constructor
    public SolicitudDTO(int idSolicitud, String dependencia, String asunto, String estado, Date fechaDeSolicitud, String solicitanteNombre) {
        this.idSolicitud = idSolicitud;
        this.dependencia = dependencia;
        this.asunto = asunto;
        this.estado = estado;
        this.fechaDeSolicitud = fechaDeSolicitud;
        this.solicitanteNombre = solicitanteNombre;
    }

    // Getters y Setters (puedes generarlos con Lombok si prefieres)
}
