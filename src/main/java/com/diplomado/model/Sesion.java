package com.diplomado.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;
import java.sql.Time;
import java.util.List;

@Data
@Entity
@Table(name = "sesion")
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSesion;

    private String lugar;
    private Date fecha;
    private Time horaInicio;
    private Time horaFinal;
    private String presidente;
    private String secretario;
    private String contenido;

    // Relación con asistencia de invitados
    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL)
    private List<AsistenciaInvitado> asistenciaInvitados;

    // Relación con asistencia de miembros
    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL)
    private List<AsistenciaMiembro> asistenciaMiembros;

    // Relación con actas
    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL)
    private List<Acta> actas;
}

