package com.diplomado.service;

import com.diplomado.configuration.exception.FechaNoValidaException;
import com.diplomado.model.*;
import com.diplomado.model.dto.ActaDTO;
import com.diplomado.model.dto.AsistenciaInvitadoDTO;
import com.diplomado.model.dto.AsistenciaMiembroDTO;
import com.diplomado.model.dto.SesionDTO;
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
                .map(am -> {
                    AsistenciaMiembroDTO amDTO = new AsistenciaMiembroDTO();
                    amDTO.setIdMiembro(am.getMiembro().getIdMiembro());
                    amDTO.setNombre(am.getMiembro().getNombre());
                    amDTO.setCargo(am.getMiembro().getCargo());
                    amDTO.setEstadoAsistencia(am.getEstadoAsistencia());
                    return amDTO;
                })
                .collect(Collectors.toList());
        dto.setAsistenciaMiembros(asistenciaMiembros);

        // Obtenemos la asistencia de invitados
        List<AsistenciaInvitadoDTO> asistenciaInvitados = asistenciaInvitadoRepository.findBySesion(sesion)
                .stream()
                .map(ai -> {
                    AsistenciaInvitadoDTO aiDTO = new AsistenciaInvitadoDTO();
                    aiDTO.setIdInvitado(ai.getInvitado().getIdInvitados());
                    aiDTO.setNombre(ai.getInvitado().getNombre());
                    aiDTO.setDependencia(ai.getInvitado().getDependencia());
                    aiDTO.setEstadoAsistencia(ai.getEstadoAsistencia());
                    return aiDTO;
                })
                .collect(Collectors.toList());
        dto.setAsistenciaInvitados(asistenciaInvitados);

        // Obtenemos las actas relacionadas con la sesión
        List<ActaDTO> actas = actaRepository.findBySesion(sesion)
                .stream()
                .map(acta -> {
                    ActaDTO actaDTO = new ActaDTO();
                    actaDTO.setIdActa(acta.getIdActa());
                    actaDTO.setEstado(acta.getEstado());
                    return actaDTO;
                })
                .collect(Collectors.toList());
        dto.setActas(actas);

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
}

