package com.diplomado.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "asistencia_miembros")
public class AsistenciaMiembro {
    @EmbeddedId
    private AsistenciaMiembroId id;

    @ManyToOne
    @MapsId("sesionId")
    @JoinColumn(name = "SESION_IDSESION")
    private Sesion sesion;

    @ManyToOne
    @MapsId("miembroId")
    @JoinColumn(name = "MIEMBROS_IDMIEMBRO")
    private Miembro miembro;

    private String estadoAsistencia;
}

