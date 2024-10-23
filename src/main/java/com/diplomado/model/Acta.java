package com.diplomado.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "actas")
public class Acta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int numActas;

    private String estado;

    @ManyToOne
    @JoinColumn(name = "SESION_IDSESION")
    private Sesion sesion;
}

