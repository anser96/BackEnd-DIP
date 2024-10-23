package com.diplomado.service;

import com.diplomado.model.AsistenciaMiembro;
import com.diplomado.model.AsistenciaMiembroId;
import com.diplomado.repository.AsistenciaMiembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsistenciaMiembroService {

    @Autowired
    private AsistenciaMiembroRepository asistenciaMiembroRepository;

    public AsistenciaMiembro save(AsistenciaMiembro asistenciaMiembro) {
        return asistenciaMiembroRepository.save(asistenciaMiembro);
    }

    public AsistenciaMiembro findById(AsistenciaMiembroId id) {
        return asistenciaMiembroRepository.findById(id).orElseThrow();
    }

    public void registrarAsistencia(int sesionId, int miembroId, String estadoAsistencia) {
        AsistenciaMiembroId asistenciaId = new AsistenciaMiembroId(sesionId, miembroId);
        AsistenciaMiembro asistencia = asistenciaMiembroRepository.findById(asistenciaId)
                .orElse(new AsistenciaMiembro());
        asistencia.setEstadoAsistencia(estadoAsistencia);
        asistenciaMiembroRepository.save(asistencia);
    }
}

