package com.diplomado.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "solicitantes")
public class Solicitante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSolicitante;

    private String nombre;
    private String tipoDeSolicitante;
    private String email;
    private String celular;
}

