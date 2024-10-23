package com.diplomado.service;

import com.diplomado.model.*;
import com.diplomado.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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

    public Sesion save(Sesion sesion) {
        return sesionRepository.save(sesion);
    }

    public Sesion findById(int idSesion) {
        return sesionRepository.findById(idSesion).orElseThrow();
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
        }
    }


    public void citarMiembros(int sesionId, List<Miembro> miembros) {
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow();

        for (Miembro miembro : miembros) {
            Miembro existingMiembro = miembroRepository.findById(miembro.getIdMiembro()).orElse(miembro);
            miembroRepository.save(existingMiembro);

            // Crea la clave compuesta
            AsistenciaMiembroId asistenciaMiembroId = new AsistenciaMiembroId(sesionId, existingMiembro.getIdMiembro());

            // Crea la relación de asistencia
            AsistenciaMiembro asistenciaMiembro = new AsistenciaMiembro();
            asistenciaMiembro.setId(asistenciaMiembroId);
            asistenciaMiembro.setSesion(sesion);
            asistenciaMiembro.setMiembro(existingMiembro);
            asistenciaMiembro.setEstadoAsistencia("PENDIENTE");

            asistenciaMiembroRepository.save(asistenciaMiembro);
        }
    }



    public Sesion definirContenido(int sesionId, String contenido) {
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        sesion.setContenido(contenido);  // Actualiza solo el campo "contenido"
        return sesionRepository.save(sesion);
    }


    public void verificarQuorum(int sesionId) {
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow();
        // Lógica para verificar quorum
    }
}

