package com.diplomado.service;

import com.diplomado.configuration.exception.FechaNoValidaException;
import com.diplomado.model.*;
import com.diplomado.model.dto.*;
import com.diplomado.repository.*;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
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

    @Autowired
    private SolicitudRepository solicitudRepository;

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

        Sesion nuevaSesion = sesionRepository.save(sesion);
        // Crear el acta y asociarla a la sesión
        Acta acta = new Acta();
        acta.setSesion(nuevaSesion); // Asignar la sesión al acta
        acta.setEstado("Pendiente"); // Estado inicial del acta
        acta.setNumeroActa(generarNumeroActa()); // Generar número de acta alfanumérico

        // Guardar el acta en la base de datos
        actaRepository.save(acta);

        return nuevaSesion;
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

    private String generarNumeroActa() {
        String numeroActa;

        do {
            numeroActa = RandomStringUtils.randomAlphanumeric(3).toUpperCase();
        } while (actaRepository.existsByNumeroActa(numeroActa)); // Asegurarse de que el número es único

        return numeroActa;
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

        // Solicitudes asociadas a la sesión
        List<SolicitudDTO> solicitudes = solicitudRepository.findBySesion(sesion)
                .stream()
                .map(solicitud -> SolicitudDTO.builder()
                        .idSolicitud(solicitud.getIdSolicitud())
                        .dependencia(solicitud.getDependencia())
                        .asunto(solicitud.getAsunto())
                        .descripcion(solicitud.getDescripcion())
                        .fechaDeSolicitud(solicitud.getFechaDeSolicitud())
                        .respuesta(solicitud.getRespuesta())
                        .estado(solicitud.getEstado())
                        .tipoSolicitante(solicitud.getTipoSolicitante())
                        .idSolicitante(solicitud.getIdSolicitante())
                        .nombreSolicitante(solicitud.getNombreSolicitante())
                        .build())
                .collect(Collectors.toList());
        dto.setSolicitudes(solicitudes);

        return dto;
    }


    public List<Sesion> findAll() {
        return sesionRepository.findAll();
    }

    public void agregarInvitados(int sesionId, List<Invitado> invitados) {
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow();

        for (Invitado invitado : invitados) {
            // Buscar el invitado por email en lugar de ID
            Optional<Invitado> optionalInvitado = invitadoRepository.findByEmail(invitado.getEmail());
            Invitado existingInvitado;

            if (optionalInvitado.isPresent()) {
                // Si el invitado ya existe, utilizar el registro existente
                existingInvitado = optionalInvitado.get();
            } else {
                // Si el invitado no existe, guardar el nuevo invitado
                existingInvitado = invitadoRepository.save(invitado);
            }

            // Crear el ID compuesto para AsistenciaInvitado
            AsistenciaInvitadoId asistenciaInvitadoId = new AsistenciaInvitadoId(sesionId, existingInvitado.getIdInvitados());

            // Crear la instancia de AsistenciaInvitado
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
                throw new RuntimeException("Error al enviar la notificación al invitado", e);
            }
        }
    }






    @Transactional
    public void citarMiembros(int sesionId, List<Miembro> miembros) {
        // Buscamos la sesión, si no se encuentra lanzamos una excepción
        Sesion sesion = sesionRepository.findById(sesionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        for (Miembro miembro : miembros) {
            // Verificar si el miembro ya existe en la base de datos por email
            Optional<Miembro> optionalMiembro = miembroRepository.findByEmail(miembro.getEmail());
            Miembro existingMiembro;

            if (optionalMiembro.isPresent()) {
                // Si el miembro ya existe, utilizar el miembro encontrado
                existingMiembro = optionalMiembro.get();
            } else {
                // Si no existe, se guarda el nuevo miembro en la tabla
                existingMiembro = miembroRepository.save(miembro);
            }

            // Crear la clave compuesta (sesionId, miembroId)
            AsistenciaMiembroId asistenciaMiembroId = new AsistenciaMiembroId(sesionId, existingMiembro.getIdMiembro());

            // Crear la relación de asistencia
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
                throw new RuntimeException("Error al enviar la notificación al miembro", e);
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


    public void verificarQuorum(int sesionId, List<AsistenciaDTO> asistencias) {
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        for (AsistenciaDTO asistenciaDTO : asistencias) {
            // Si es un miembro
            if ("miembro".equalsIgnoreCase(asistenciaDTO.getTipo())) {
                AsistenciaMiembroId asistenciaMiembroId = new AsistenciaMiembroId(sesionId, asistenciaDTO.getIdPersona());
                AsistenciaMiembro asistenciaMiembro = asistenciaMiembroRepository.findById(asistenciaMiembroId)
                        .orElseThrow(() -> new RuntimeException("Asistencia de miembro no encontrada"));

                asistenciaMiembro.setEstadoAsistencia(asistenciaDTO.getEstadoAsistencia());
                asistenciaMiembroRepository.save(asistenciaMiembro);
            }

            // Si es un invitado
            else if ("invitado".equalsIgnoreCase(asistenciaDTO.getTipo())) {
                AsistenciaInvitadoId asistenciaInvitadoId = new AsistenciaInvitadoId(sesionId, asistenciaDTO.getIdPersona());
                AsistenciaInvitado asistenciaInvitado = asistenciaInvitadoRepository.findById(asistenciaInvitadoId)
                        .orElseThrow(() -> new RuntimeException("Asistencia de invitado no encontrada"));

                asistenciaInvitado.setEstadoAsistencia(asistenciaDTO.getEstadoAsistencia());
                asistenciaInvitadoRepository.save(asistenciaInvitado);
            }
        }
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

