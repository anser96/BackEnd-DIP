package com.diplomado.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor  // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Table(name = "sesion")
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSesion;

    private String lugar;
    private LocalDate fecha;
    private String contenido; // El contenido que defines en el método
    private LocalTime horaInicio; // Campo para la hora de inicio
    private LocalTime horaFinal; // Campo para la hora de fin
    private String presidente;
    private String secretario;

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

