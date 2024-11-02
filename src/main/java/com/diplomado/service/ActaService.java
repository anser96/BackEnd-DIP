package com.diplomado.service;

import com.diplomado.model.*;
import com.diplomado.model.dto.*;
import com.diplomado.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActaService {

    @Autowired
    private SesionService sesionService;
    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private TareaService tareaService;

    @Autowired
    private ActaRepository actaRepository;

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private AsistenciaMiembroRepository asistenciaMiembroRepository;

    @Autowired
    private AsistenciaInvitadoRepository asistenciaInvitadoRepository;

    @Autowired
    private TareaRepository tareaRepository;

    public Acta save(Acta acta) {
        return actaRepository.save(acta);
    }

    public Acta aprobar(int actaId) {
        Acta acta = actaRepository.findById(actaId).orElseThrow();
        acta.setEstado("APROBADA");
        return actaRepository.save(acta);
    }

    public List<ActaDTO> listarActas() {
        return actaRepository.findAll()
                .stream()
                .map(acta -> ActaDTO.builder()
                        .idActa(acta.getIdActa())
                        .estado(acta.getEstado())
                        .sesionId(acta.getSesion().getIdSesion()) // Incluyendo el idSesion
                        .build())
                .collect(Collectors.toList());
    }

    public ActaDTO convertToDTO(Acta acta) {
        return ActaDTO.builder()
                .idActa(acta.getIdActa())
                .numeroActa(acta.getNumeroActa())
                .estado(acta.getEstado())
                .sesion(sesionService.convertToDTO(acta.getSesion()))
                .build();
    }


    public List<ActaDTO> obtenerTodasLasActas() {
        return actaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ActaDTO> obtenerActaPorId(int id) {
        return actaRepository.findById(id).map(this::convertToDTO);
    }
}
