package com.diplomado.service;

import com.diplomado.model.*;
import com.diplomado.model.dto.SolicitudDTO;
import com.diplomado.repository.InvitadoRepository;
import com.diplomado.repository.MiembroRepository;
import com.diplomado.repository.SolicitudRepository;
import com.diplomado.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private MiembroRepository miembroRepository;

    @Autowired
    private InvitadoRepository invitadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // Asumiendo que existe un repositorio para usuarios autenticados

    public Solicitud crearSolicitud(Solicitud solicitud) {
        String nombreSolicitante = obtenerNombreSolicitante(solicitud.getTipoSolicitante(), solicitud.getIdSolicitante());

        // Asigna el nombre del solicitante validado a la solicitud
        solicitud.setNombreSolicitante(nombreSolicitante);

        // Guarda la solicitud en la base de datos
        return solicitudRepository.save(solicitud);
    }

    private String obtenerNombreSolicitante(String tipoSolicitante, int solicitanteId) {
        switch (tipoSolicitante.toLowerCase()) {
            case "miembro":
                return miembroRepository.findById(solicitanteId)
                        .map(Miembro::getNombre)
                        .orElseThrow(() -> new IllegalArgumentException("El ID de miembro no existe: " + solicitanteId));
            case "invitado":
                return invitadoRepository.findById(solicitanteId)
                        .map(Invitado::getNombre)
                        .orElseThrow(() -> new IllegalArgumentException("El ID de invitado no existe: " + solicitanteId));
            case "usuario":
                return usuarioRepository.findById(solicitanteId)
                        .map(Usuario::getNombre)
                        .orElseThrow(() -> new IllegalArgumentException("El ID de usuario no existe: " + solicitanteId));
            default:
                throw new IllegalArgumentException("Tipo de solicitante no válido: " + tipoSolicitante);
        }
    }

    private int obtenerIdSolicitante(String tipoSolicitante, int solicitanteId) {
        switch (tipoSolicitante.toLowerCase()) {
            case "miembro":
                return miembroRepository.findById(solicitanteId)
                        .map(Miembro::getIdMiembro)
                        .orElseThrow(() -> new IllegalArgumentException("El ID de miembro no existe: " + solicitanteId));
            case "invitado":
                return invitadoRepository.findById(solicitanteId)
                        .map(Invitado::getIdInvitados)
                        .orElseThrow(() -> new IllegalArgumentException("El ID de invitado no existe: " + solicitanteId));
            case "usuario":
                return usuarioRepository.findById(solicitanteId)
                        .map(Usuario::getIdUsuario)
                        .orElseThrow(() -> new IllegalArgumentException("El ID de usuario no existe: " + solicitanteId));
            default:
                throw new IllegalArgumentException("Tipo de solicitante no válido: " + tipoSolicitante);
        }
    }

    public Solicitud findById(int idSolicitud) {
        return solicitudRepository.findById(idSolicitud).orElseThrow();
    }

    public List<Solicitud> getPendientes() {
        return solicitudRepository.findByEstado("PENDIENTE");
    }

    public Solicitud responder(int solicitudId, String respuesta) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId).orElseThrow();
        solicitud.setRespuesta(respuesta);
        return solicitudRepository.save(solicitud);
    }

    public List<Solicitud> findByEstado(String estado) {
        return solicitudRepository.findByEstado(estado);
    }

    public List<SolicitudDTO> findAll() {
        return solicitudRepository.findAll().stream()
                .map(solicitud -> {
                    int idSolicitante = solicitud.getIdSolicitante();
                    String tipoSolicitante = solicitud.getTipoSolicitante();
                    String nombreSolicitante = obtenerNombreSolicitante(tipoSolicitante, idSolicitante);

                    return SolicitudDTO.builder()
                            .idSolicitud(solicitud.getIdSolicitud())
                            .dependencia(solicitud.getDependencia())
                            .asunto(solicitud.getAsunto())
                            .descripcion(solicitud.getDescripcion())
                            .fechaDeSolicitud(solicitud.getFechaDeSolicitud())
                            .estado(solicitud.getEstado())
                            .respuesta(solicitud.getRespuesta())
                            .idSolicitante(idSolicitante)
                            .nombreSolicitante(nombreSolicitante)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public SolicitudDTO findSolicitudById(int idSolicitud) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud con ID " + idSolicitud + " no existe"));

        // Obtenemos el nombre y el ID del solicitante basado en el tipo
        String nombreSolicitante = obtenerNombreSolicitante(solicitud.getTipoSolicitante(), solicitud.getIdSolicitante());
        int idSolicitante = obtenerIdSolicitante(solicitud.getTipoSolicitante(), solicitud.getIdSolicitante());

        // Convertimos la solicitud a SolicitudDTO
        return SolicitudDTO.builder()
                .idSolicitud(solicitud.getIdSolicitud())
                .dependencia(solicitud.getDependencia())
                .asunto(solicitud.getAsunto())
                .descripcion(solicitud.getDescripcion())
                .fechaDeSolicitud(solicitud.getFechaDeSolicitud())
                .estado(solicitud.getEstado())
                .respuesta(solicitud.getRespuesta())
                .tipoSolicitante(solicitud.getTipoSolicitante())
                .idSolicitante(idSolicitante)
                .nombreSolicitante(nombreSolicitante)
                .build();
    }

    public SolicitudDTO editarSolicitud(int idSolicitud, SolicitudDTO solicitudDTO) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + idSolicitud));


        // Obtenemos el nombre y el ID del solicitante basado en el tipo
        String nombreSolicitante = obtenerNombreSolicitante(solicitud.getTipoSolicitante(), solicitud.getIdSolicitante());
        int idSolicitante = obtenerIdSolicitante(solicitud.getTipoSolicitante(), solicitud.getIdSolicitante());

        // Actualizamos solo los campos necesarios
        solicitud.setDependencia(solicitudDTO.getDependencia());
        solicitud.setAsunto(solicitudDTO.getAsunto());
        solicitud.setDescripcion(solicitudDTO.getDescripcion());
        solicitud.setEstado(solicitudDTO.getEstado());
        solicitud.setRespuesta(solicitudDTO.getRespuesta());

        Solicitud solicitudActualizada = solicitudRepository.save(solicitud);

        // Convertimos a DTO y devolvemos
        return SolicitudDTO.builder()
                .idSolicitud(solicitudActualizada.getIdSolicitud())
                .dependencia(solicitudActualizada.getDependencia())
                .asunto(solicitudActualizada.getAsunto())
                .descripcion(solicitudActualizada.getDescripcion())
                .fechaDeSolicitud(solicitudActualizada.getFechaDeSolicitud())
                .estado(solicitudActualizada.getEstado())
                .tipoSolicitante(solicitudActualizada.getTipoSolicitante())
                .respuesta(solicitudActualizada.getRespuesta())
                .nombreSolicitante(nombreSolicitante)
                .idSolicitante(idSolicitante)
                .build();
    }

}

