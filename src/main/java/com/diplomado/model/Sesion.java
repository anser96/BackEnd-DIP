package com.diplomado.model;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Time;
import java.util.Date;
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

    @OneToMany(mappedBy = "sesion")
    private List<Acta> actas;

    @OneToMany(mappedBy = "sesion")
    private List<AsistenciaInvitado> asistenciaInvitados;

    @OneToMany(mappedBy = "sesion")
    private List<AsistenciaMiembro> asistenciaMiembros;
}
