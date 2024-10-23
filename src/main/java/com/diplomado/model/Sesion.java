package com.diplomado.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@Table(name = "sesion")
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSesion;

    private String lugar;
    private Date fecha;
    private String contenido; // El contenido que defines en el método

    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL)
    @JsonManagedReference // Para romper ciclos de serialización
    private List<AsistenciaMiembro> asistenciaMiembros;

    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL)
    @JsonManagedReference // Para romper ciclos de serialización
    private List<AsistenciaInvitado> asistenciaInvitados;

    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL)
    @JsonManagedReference // Para romper ciclos de serialización con Acta
    private List<Acta> actas;
}

