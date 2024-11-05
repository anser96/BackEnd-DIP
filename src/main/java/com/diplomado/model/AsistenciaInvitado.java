package com.diplomado.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "asistencia_invitado")
public class AsistenciaInvitado {

    @EmbeddedId
    private AsistenciaInvitadoId id;

    @ManyToOne
    @MapsId("sesionId")
    @JoinColumn(name = "SESION_IDSESION")
    @JsonBackReference // Para romper ciclos de serialización
    private Sesion sesion;

    @ManyToOne
    @MapsId("invitadoId")
    @JoinColumn(name = "INVITADO_IDINVITADO")
    private Invitado invitado;

    private String estadoAsistencia;

    private String excusa;
}


