package com.diplomado.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SesionDTO {
    private int idSesion;
    private String lugar;
    private Date fecha;
    private String contenido;

    // Constructor
    public SesionDTO(int idSesion, String lugar, Date fecha, String contenido) {
        this.idSesion = idSesion;
        this.lugar = lugar;
        this.fecha = fecha;
        this.contenido = contenido;
    }

    // Getters y Setters (puedes generar con Lombok si prefieres)
}

