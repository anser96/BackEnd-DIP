package com.diplomado.service;

import com.diplomado.model.Tarea;
import com.diplomado.repository.InvitadoRepository;
import com.diplomado.repository.MiembroRepository;
import com.diplomado.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public Tarea asignarTarea(String descripcion, String tipoResponsable, int responsableId, LocalDate fechaEntrega, LocalDate fechaVerificacion) {
        Tarea tarea = new Tarea();
        tarea.setDescripcion(descripcion);
        tarea.setFechaEntrega(fechaEntrega);
        tarea.setFechaVerificacion(fechaVerificacion);
        tarea.setTipoResponsable(tipoResponsable);
        tarea.setResponsableId(responsableId);
        tarea.setEstado("Pendiente");

        return tareaRepository.save(tarea);
    }

    public List<Tarea> obtenerTareasConResponsables() {
        List<Tarea> tareas = tareaRepository.findAll();

        tareas.forEach(tarea -> {
            if ("miembro".equalsIgnoreCase(tarea.getTipoResponsable())) {
                tarea.setResponsable(miembroRepository.findById(tarea.getResponsableId()).orElse(null));
            } else if ("invitado".equalsIgnoreCase(tarea.getTipoResponsable())) {
                tarea.setResponsable(invitadoRepository.findById(tarea.getResponsableId()).orElse(null));
            }
        });

        return tareas;
    }

    public Optional<Tarea> actualizarEstado(int tareaId, String nuevoEstado) {
        Optional<Tarea> tareaOpt = tareaRepository.findById(tareaId);
        if (tareaOpt.isPresent()) {
            Tarea tarea = tareaOpt.get();
            tarea.setEstado(nuevoEstado);
            tareaRepository.save(tarea);
            return Optional.of(tarea);
        }
        return Optional.empty();
    }
}

