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
    private String desicion;
    private Date fechaDeSolicitud;
    private String respuesta;

    @ManyToOne
    @JoinColumn(name = "SOLICITANTE_IDSOLICITANTE")
    private Solicitante solicitante;

    @ManyToOne
    @JoinColumn(name = "SESION_IDSESION")
    private Sesion sesion;

    @ManyToOne
    @JoinColumn(name = "DESCRIPCION_IDDESCRIPCION")
    private Descripcion descripcion;
}

