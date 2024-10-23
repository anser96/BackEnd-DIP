package com.diplomado.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "invitados")
public class Invitado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idInvitados;

    private String nombre;
    private String dependencia;
    private String cargo;

    @OneToMany(mappedBy = "invitado")
    private List<AsistenciaInvitado> asistenciaInvitados;
}

