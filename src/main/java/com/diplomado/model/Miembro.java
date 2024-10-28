package com.diplomado.model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "miembros")
public class Miembro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMiembro;
    private String nombre;
    private String cargo;
    private String email;
    @OneToMany(mappedBy = "miembro")
    private List<AsistenciaMiembro> asistenciaMiembros;
}
