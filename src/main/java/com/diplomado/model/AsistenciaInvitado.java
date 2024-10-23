package com.diplomado.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "asistencia_invitado")
public class AsistenciaInvitado {
    @EmbeddedId
    private AsistenciaInvitadoId id;

    @ManyToOne
    @MapsId("sesionId")
    @JoinColumn(name = "SESION_IDSESION")
    private Sesion sesion;

    @ManyToOne
    @MapsId("invitadoId")
    @JoinColumn(name = "INVITADO_IDINVITADO")
    private Invitado invitado;

    private String estadoAsistencia;
}

