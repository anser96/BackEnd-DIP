package com.diplomado.service;

import com.diplomado.model.Invitado;
import com.diplomado.model.Miembro;
import com.diplomado.model.Tarea;
import com.diplomado.model.dto.TareaDTO;
import com.diplomado.repository.InvitadoRepository;
import com.diplomado.repository.MiembroRepository;
import com.diplomado.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private MiembroRepository miembroRepository;

    @Autowired
    private InvitadoRepository invitadoRepository;

    // Método para guardar una tarea
    public Tarea save(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    // Método para obtener todas las tareas
    public List<Tarea> findAll() {
        return tareaRepository.findAll();
    }

    // Método para buscar una tarea por ID
    public Tarea findById(int idTarea) {
        return tareaRepository.findById(idTarea).orElseThrow(() ->
                new RuntimeException("Tarea no encontrada con el id: " + idTarea));
    }

    // Método para eliminar una tarea por ID
    public void deleteById(int idTarea) {
        tareaRepository.deleteById(idTarea);
    }

    public List<TareaDTO> obtenerTareasConResponsables() {
        return tareaRepository.findAll().stream().map(tarea -> {
            String responsableNombre = null;

            // Obtener solo el nombre del responsable según el tipo
            if ("miembro".equalsIgnoreCase(tarea.getTipoResponsable())) {
                responsableNombre = miembroRepository.findById(tarea.getResponsableId())
                        .map(Miembro::getNombre)
                        .orElse("Miembro no encontrado");
            } else if ("invitado".equalsIgnoreCase(tarea.getTipoResponsable())) {
                responsableNombre = invitadoRepository.findById(tarea.getResponsableId())
                        .map(Invitado::getNombre)
                        .orElse("Invitado no encontrado");
            }

            // Crear TareaDTO con solo los campos necesarios
            return TareaDTO.builder()
                    .idTarea(tarea.getIdTareas())
                    .descripcion(tarea.getDescripcion())
                    .estado(tarea.getEstado())
                    .fechaEntrega(tarea.getFechaEntrega())
                    .fechaVerificacion(tarea.getFechaVerificacion())
                    .tipoResponsable(tarea.getTipoResponsable())
                    .responsableId(tarea.getResponsableId())
                    .responsableNombre(responsableNombre)
                    .build();
        }).collect(Collectors.toList());
    }

    public Tarea asignarTarea(String descripcion, String tipoResponsable, int responsableId, LocalDate fechaEntrega, LocalDate fechaVerificacion) {
        Tarea tarea = new Tarea();
        tarea.setDescripcion(descripcion);
        tarea.setTipoResponsable(tipoResponsable);
        tarea.setResponsableId(responsableId);
        tarea.setFechaEntrega(fechaEntrega);
        tarea.setFechaVerificacion(fechaVerificacion);
        return tareaRepository.save(tarea);
    }

    public Optional<Tarea> actualizarEstado(int id, String estado) {
        Optional<Tarea> tareaOpt = tareaRepository.findById(id);
        tareaOpt.ifPresent(tarea -> {
            tarea.setEstado(estado);
            tareaRepository.save(tarea);
        });
        return tareaOpt;
    }
}

