package com.diplomado.service;

import com.diplomado.model.AsistenciaInvitado;
import com.diplomado.model.AsistenciaInvitadoId;
import com.diplomado.repository.AsistenciaInvitadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsistenciaInvitadoService {

    @Autowired
    private AsistenciaInvitadoRepository asistenciaInvitadoRepository;

    public AsistenciaInvitado save(AsistenciaInvitado asistenciaInvitado) {
        return asistenciaInvitadoRepository.save(asistenciaInvitado);
    }

    public AsistenciaInvitado findById(AsistenciaInvitadoId id) {
        return asistenciaInvitadoRepository.findById(id).orElseThrow();
    }

    public void registrarAsistencia(int sesionId, int invitadoId, String estadoAsistencia) {
        AsistenciaInvitadoId asistenciaId = new AsistenciaInvitadoId(sesionId, invitadoId);
        AsistenciaInvitado asistencia = asistenciaInvitadoRepository.findById(asistenciaId)
                .orElse(new AsistenciaInvitado());
        asistencia.setEstadoAsistencia(estadoAsistencia);
        asistenciaInvitadoRepository.save(asistencia);
    }
}

