package com.diplomado.model;

import jakarta.persistence.*;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "acta")
public class Acta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Esto asegura que el ID sea generado automáticamente
    @Column(name = "ID_ACTA")  // Mapea a la columna correcta en la base de datos
    private int idActa;

    private String estado;

    @ManyToOne
    @JoinColumn(name = "SESION_IDSESION")
    @JsonBackReference // Esta es la referencia inversa a Sesion
    private Sesion sesion;
}


