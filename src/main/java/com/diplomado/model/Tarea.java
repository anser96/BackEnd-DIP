package com.diplomado.model;

import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;


@Data
@Entity
@Table(name = "tareas")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTareas;
    private String descripcion;
    private LocalDate fechaEntrega;
    private LocalDate fechaVerificacion;
    private String tipoResponsable; // "miembro" o "invitado"
    private int responsableId; // ID del miembro o invitado responsable
    private String estado = "Pendiente"; // Estado inicial de la tarea
    @Transient // Campo transitorio para mostrar detalles del responsable sin relación persistente
    private Object responsable;
}

