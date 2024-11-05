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
        // Consulta el acta actual que se quiere aprobar
        Acta acta = actaRepository.findById(actaId).orElseThrow(() -> new RuntimeException("Acta no encontrada"));

        // Verifica si existe un acta anterior
        int idActaAnterior = actaId - 1;
        Optional<Acta> actaAnteriorOpt = actaRepository.findById(idActaAnterior);

        // Si existe el acta anterior, se verifica si está aprobada
        if (actaAnteriorOpt.isPresent() && !"APROBADA".equals(actaAnteriorOpt.get().getEstado())) {
            throw new RuntimeException("Debe aprobarse el acta anterior antes de aprobar esta.");
        }

        // Cambiar el estado del acta actual a "APROBADA"
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
                        .numeroActa(acta.getNumeroActa())
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
