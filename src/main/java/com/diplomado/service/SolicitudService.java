package com.diplomado.service;

import com.diplomado.model.*;
import com.diplomado.model.dto.SesionDTO;
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

    public  List<Solicitud> findAll(){
        return solicitudRepository.findAll();
    }
}

