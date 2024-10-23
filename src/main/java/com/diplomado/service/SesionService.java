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
        // Busca la sesión por su ID
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow();

        // Procesa cada invitado y lo agrega a la lista de asistencia de invitados
        for (Invitado invitado : invitados) {
            // Verifica si el invitado ya existe, si no, lo guarda
            Invitado existingInvitado = invitadoRepository.findById(invitado.getIdInvitados()).orElse(invitado);
            invitadoRepository.save(existingInvitado);

            // Crea un nuevo registro de asistencia para el invitado
            AsistenciaInvitado asistenciaInvitado = new AsistenciaInvitado();
            asistenciaInvitado.setSesion(sesion);
            asistenciaInvitado.setInvitado(existingInvitado);
            asistenciaInvitado.setEstadoAsistencia("PENDIENTE"); // Puedes ajustar el estado si es necesario

            asistenciaInvitadoRepository.save(asistenciaInvitado);
        }
    }


    public void citarMiembros(int sesionId, List<Miembro> miembros) {
        // Busca la sesión por su ID
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow();

        // Procesa cada miembro y lo agrega a la lista de asistencia de miembros
        for (Miembro miembro : miembros) {
            // Verifica si el miembro ya existe, si no, lo guarda
            Miembro existingMiembro = miembroRepository.findById(miembro.getIdMiembro()).orElse(miembro);
            miembroRepository.save(existingMiembro);

            // Crea un nuevo registro de asistencia para el miembro
            AsistenciaMiembro asistenciaMiembro = new AsistenciaMiembro();
            asistenciaMiembro.setSesion(sesion);
            asistenciaMiembro.setMiembro(existingMiembro);
            asistenciaMiembro.setEstadoAsistencia("PENDIENTE"); // Puedes ajustar el estado si es necesario

            asistenciaMiembroRepository.save(asistenciaMiembro);
        }
    }


    public Sesion definirContenido(int sesionId, String contenido) {
        // Busca la sesión por su ID
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow();

        // Define el contenido de la sesión
        sesion.setContenido(contenido);

        // Guarda los cambios
        return sesionRepository.save(sesion);
    }


    public void verificarQuorum(int sesionId) {
        Sesion sesion = sesionRepository.findById(sesionId).orElseThrow();
        // Lógica para verificar quorum
    }
}

