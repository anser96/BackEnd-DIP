package com.diplomado.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "descricion")
public class Descripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDescripcion;

    private String estuImplicados;
    private int numEstuImplicados;
    private String docenImplicados;
    private int numDocenImplicados;
    private String cuidadImplicada;
    private String paisImplicado;
    private String evento;
}

