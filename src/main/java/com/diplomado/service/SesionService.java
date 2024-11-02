package com.diplomado.service;

import com.diplomado.configuration.exception.FechaNoValidaException;
import com.diplomado.model.*;
import com.diplomado.model.dto.*;
import com.diplomado.repository.*;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SesionService {

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private InvitadoRepository invitadoRepository;

    @Autowired
    private AsistenciaInvitadoRepository asistenciaInvitadoRepository;

    @Autowired
    private MiembroRepository miembroRepository;

    @Autowired
    private AsistenciaMiembroRepository asistenciaMiembroRepository;

    @Autowired
    private ActaRepository actaRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TareaRepository tareaRepository;

    public Sesion save(Sesion sesion) {
        Optional<Sesion> ultimaSesionOpt = sesionRepository.findTopByOrderByFechaDesc();

        if (ultimaSesionOpt.isPresent()) {
            Sesion ultimaSesion = ultimaSesionOpt.get();
            LocalDate fechaUltimaSesion = ultimaSesion.getFecha();

            if (!sesion.getFecha().isAfter(fechaUltimaSesion)) {
                throw new FechaNoValidaException("La fecha de la nueva reunión debe ser posterior a la última reunión registrada, que fue el "
                        + fechaUltimaSesion.toString());
            }
        }
        // Asegurarse de que Presidente y Secretario no estén vacíos antes de guardar
        if (sesion.getPresidente() == null || sesion.getSecretario() == null) {
            throw new IllegalArgumentException("Presidente y Secretario son campos obligatorios.");
        }

        return sesionRepository.save(sesion);
    }

    public Sesion findById(int idSesion) {
        return sesionRepository.findById(idSesion).orElseThrow();
    }

    public SesionDTO getSesionById(int id) {
        Sesion sesion = sesionRepository.findById(id).orElse(null);

        if (sesion != null) {
            // Convertir la entidad Sesion a DTO
            return convertToDTO(sesion);
        } else {
            return null; // Sesión no encontrada
        }
    }

    public List<SesionDTO> getSesiones() {
        List<Sesion> sesiones = sesionRepository.findAll();

        return sesiones.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public SesionDTO updateSesion(int id, SesionDTO sesionDTO) {
        Sesion sesion = sesionRepository.findById(id).orElse(null);

        if (sesion != null) {
            // Actualizamos los campos que han sido enviados en el DTO
            sesion.setLugar(sesionDTO.getLugar());
            sesion.setFecha(sesionDTO.getFecha());
            sesion.setHoraInicio(sesionDTO.getHoraInicio());
            sesion.setHoraFinal(sesionDTO.getHoraFinal());
            sesion.setContenido(sesionDTO.getContenido());

            Sesion updatedSesion = sesionRepository.save(sesion);
            return convertToDTO(updatedSesion);
        } else {
            return null;
        }
    }

    public SesionDTO convertToDTO(Sesion sesion) {
        SesionDTO dto = SesionDTO.builder().build();
        dto.setIdSesion(sesion.getIdSesion());
        dto.setLugar(sesion.getLugar());
        dto.setFecha(sesion.getFecha());
        dto.setHoraInicio(sesion.getHoraInicio());
        dto.setHoraFinal(sesion.getHoraFinal());
        dto.setContenido(sesion.getContenido());
        dto.setPresidente(sesion.getPresidente());
        dto.setSecretario(sesion.getSecretario());

        // Obtenemos la asistencia de miembros
        List<AsistenciaMiembroDTO> asistenciaMiembros = asistenciaMiembroRepository.findBySesion(sesion)
                .stream()
                .map(am -> AsistenciaMiembroDTO.builder()
                        .idMiembro(am.getMiembro().getIdMiembro())
                        .nombre(am.getMiembro().getNombre())
                        .cargo(am.getMiembro().getCargo())
                        .email(am.getMiembro().getEmail())
                        .estadoAsistencia(am.getEstadoAsistencia())
                        .build())
                .collect(Collectors.toList());
        dto.setAsistenciaMiembros(asistenciaMiembros);

// Obtenemos la asistencia de invitados
        List<AsistenciaInvitadoDTO> asistenciaInvitados = asistenciaInvitadoRepository.findBySesion(sesion)
                .stream()
                .map(ai -> AsistenciaInvitadoDTO.builder()
                        .idInvitado(ai.getInvitado().getIdInvitados())
                        .nombre(ai.getInvitado().getNombre())
                        .dependencia(ai.getInvitado().getDependencia())
                        .email(ai.getInvitado().getEmail())
                        .estadoAsistencia(ai.getEstadoAsistencia())
                        .build())
                .collect(Collectors.toList());
        dto.setAsistenciaInvitados(asistenciaInvitados);


        // Obtenemos las actas relacionadas con la sesión
        List<ActaDTO> actas = actaRepository.findBySesion(sesion)
                .stream()
                .map(acta -> ActaDTO.builder()
                        .idActa(acta.getIdActa())
                        .estado(acta.getEstado())
                        .sesionId(acta.getSesion().getIdSesion()) // Incluyendo el idSesion
                        .build())
                .collect(Collectors.toList());
        dto.setActas(actas);

        // Obtener las tareas asociadas con el responsable, según el tipo de responsable
        List<TareaDTO> tareas = tareaRepository.findAll().stream()
                .filter(tarea -> "miembro".equalsIgnoreCase(tarea.getTipoResponsable()) && asistenciaMiembros.stream().anyMatch(m -> m.getIdMiembro() == tarea.getResponsableId())
                        || "invitado".equalsIgnoreCase(tarea.getTipoResponsable()) && asistenciaInvitados.stream().anyMatch(i -> i.getIdInvitado() == tarea.getResponsableId()))
                .map(tarea -> {
                    String responsableNombre = "Desconocido";
                    if ("miembro".equalsIgnoreCase(tarea.getTipoResponsable())) {
                        responsableNombre = miembroRepository.findById(tarea.getResponsableId())
                                .map(Miembro::getNombre)
                                .orElse("Miembro no encontrado");
                    } else if ("invitado".equalsIgnoreCase(tarea.getTipoResponsable())) {
                        responsableNombre = invitadoRepository.findById(tarea.getResponsableId())
                                .map(Invitado::getNombre)
                                .orElse("Invitado no encontrado");
                    }
                    return TareaDTO.builder()
                            .idTarea(tarea.getIdTareas())
                            .descripcion(tarea.getDescripcion())
                            .estado(tarea.getEstado())
                            .fechaEntrega(tarea.getFechaEntrega())
                            .fechaVerificacion(tarea.getFechaVerificacion())
                            .tipoResponsable(tarea.getTipoResponsable())
                            .responsableId(tarea.getResponsableId())
                            .responsable(responsableNombre)
                            .build();
                })
                .collect(Collectors.toList());

        dto.setTareas(tareas);

        return dto;
    }


    public List<Sesion> findAll() {
        return sesionRepository.findAll();
    }

    public void agregarInvitados(int sesionId, List<Invitado> invitados) {
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow();

        for (Invitado invitado : invitados) {
            Invitado existingInvitado = invitadoRepository.findById(invitado.getIdInvitados()).orElse(invitado);
            invitadoRepository.save(existingInvitado);

            // Crea el ID compuesto
            AsistenciaInvitadoId asistenciaInvitadoId = new AsistenciaInvitadoId(sesionId, existingInvitado.getIdInvitados());

            // Crea la instancia de AsistenciaInvitado
            AsistenciaInvitado asistenciaInvitado = new AsistenciaInvitado();
            asistenciaInvitado.setId(asistenciaInvitadoId);
            asistenciaInvitado.setSesion(sesion);
            asistenciaInvitado.setInvitado(existingInvitado);
            asistenciaInvitado.setEstadoAsistencia("PENDIENTE");

            asistenciaInvitadoRepository.save(asistenciaInvitado);
            // Enviar notificación por correo
            String subject = "Invitación a la sesión";
            String text = "Usted ha sido agregado a una sesión. Detalles de la sesión: " + sesion.getLugar() + ", Fecha: " + sesion.getFecha();
            try {
                notificationService.sendEmail(
                        existingInvitado.getEmail(),
                        subject,
                        text
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Transactional
    public void citarMiembros(int sesionId, List<Miembro> miembros) {
        // Buscamos la sesión, si no se encuentra lanzamos una excepción
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        for (Miembro miembro : miembros) {
            // Verificamos si el miembro ya existe en la BD, si no existe lo creamos
            Miembro existingMiembro = miembroRepository.findById(miembro.getIdMiembro()).orElse(miembro);
            miembroRepository.save(existingMiembro);  // Guardamos el miembro en caso de que no exista

            // Creamos la clave compuesta (sesionId, miembroId)
            AsistenciaMiembroId asistenciaMiembroId = new AsistenciaMiembroId(sesionId, existingMiembro.getIdMiembro());

            // Creamos la relación de asistencia
            AsistenciaMiembro asistenciaMiembro = new AsistenciaMiembro();
            asistenciaMiembro.setId(asistenciaMiembroId);
            asistenciaMiembro.setSesion(sesion);
            asistenciaMiembro.setMiembro(existingMiembro);
            asistenciaMiembro.setEstadoAsistencia("PENDIENTE");  // Inicializamos con "PENDIENTE"

            // Guardamos la relación de asistencia
            asistenciaMiembroRepository.save(asistenciaMiembro);
            // Enviar notificación por correo
            String subject = "Invitación a la sesión";
            String text = "Usted ha sido agregado a una sesión. Detalles de la sesión: " + sesion.getLugar() + ", Fecha: " + sesion.getFecha();
            try {
                notificationService.sendEmail(
                        existingMiembro.getEmail(),
                        subject,
                        text
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    public Sesion definirContenido(int sesionId, String contenido) {
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        sesion.setContenido(contenido);  // Actualiza solo el campo "contenido"
        return sesionRepository.save(sesion);
    }

    public Sesion definirFechas(int sesionId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        sesion.setFecha(fecha);
        sesion.setHoraInicio(horaInicio);
        sesion.setHoraFinal(horaFin);
        return sesionRepository.save(sesion);
    }


    public void verificarQuorum(int sesionId) {
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow();
        // Lógica para verificar quorum
    }

    public void deleteSesion(int id) {
        sesionRepository.deleteById(id);
    }

    public boolean actualizarEstadoAsistenciaMiembro(int sesionId, int miembroId, String nuevoEstado) {
        AsistenciaMiembroId asistenciaId = new AsistenciaMiembroId(sesionId, miembroId);
        Optional<AsistenciaMiembro> asistencia = asistenciaMiembroRepository.findById(asistenciaId);
        if (asistencia.isPresent()) {
            asistencia.get().setEstadoAsistencia(nuevoEstado);
            asistenciaMiembroRepository.save(asistencia.get());
            return true;
        }
        return false;
    }

    public boolean actualizarEstadoAsistenciaInvitado(int sesionId, int invitadoId, String nuevoEstado) {
        AsistenciaInvitadoId asistenciaId = new AsistenciaInvitadoId(sesionId, invitadoId);
        Optional<AsistenciaInvitado> asistencia = asistenciaInvitadoRepository.findById(asistenciaId);
        if (asistencia.isPresent()) {
            asistencia.get().setEstadoAsistencia(nuevoEstado);
            asistenciaInvitadoRepository.save(asistencia.get());
            return true;
        }
        return false;
    }

    public List<Miembro> obtenerMiembrosPorSesion(int sesionId) {
        Sesion sesion = sesionRepository.findById(sesionId)
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada"));
        return sesion.getAsistenciaMiembros().stream()
                .map(asistencia -> asistencia.getMiembro())
                .collect(Collectors.toList());
    }

    public List<Invitado> obtenerInvitadosPorSesion(int sesionId) {
        Sesion sesion = sesionRepository.findById(sesionId)
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada"));
        return sesion.getAsistenciaInvitados().stream()
                .map(asistencia -> asistencia.getInvitado())
                .collect(Collectors.toList());
    }
}

