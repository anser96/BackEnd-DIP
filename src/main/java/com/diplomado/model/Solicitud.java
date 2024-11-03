package com.diplomado.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "solicitud")
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSolicitud;

    private String dependencia;
    private String asunto;
    private String descripcion;
    private Date fechaDeSolicitud;
    private String respuesta;
    private String estado;

    // Información del solicitante genérica
    private String tipoSolicitante; // "miembro", "invitado", o "usuario"
    private int idSolicitante;       // ID del solicitante (puede ser un miembro, invitado o usuario)
    private String nombreSolicitante; // Nombre del solicitante para evitar múltiples consultas

    // Relación opcional con una sesión
    @ManyToOne
    @JoinColumn(name = "SESION_IDSESION")
    private Sesion sesion;

    // Constructor vacío para JPA
    public Solicitud() {
    }

    // Constructor para inicializar con los campos genéricos de solicitante
    public Solicitud(String dependencia, String asunto, String descripcion, Date fechaDeSolicitud,
                     String tipoSolicitante, int idSolicitante, String nombreSolicitante) {
        this.dependencia = dependencia;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fechaDeSolicitud = fechaDeSolicitud;
        this.tipoSolicitante = tipoSolicitante;
        this.idSolicitante = idSolicitante;
        this.nombreSolicitante = nombreSolicitante;
        this.estado = "Pendiente"; // Estado inicial de la solicitud
    }
}

