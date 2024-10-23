package com.diplomado.model;

import lombok.Data;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "asistencia_miembros")
public class AsistenciaMiembro {

    @EmbeddedId
    private AsistenciaMiembroId id;

    @ManyToOne
    @MapsId("sesionId")
    @JoinColumn(name = "SESION_IDSESION")
    @JsonBackReference // Para romper ciclos de serialización
    private Sesion sesion;

    @ManyToOne
    @MapsId("miembroId")
    @JoinColumn(name = "MIEMBRO_IDMIEMBRO")
    private Miembro miembro;

    private String estadoAsistencia;
}
