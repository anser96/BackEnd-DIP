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
                    .responsable(responsableNombre)
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

    @Autowired
    private SesionService sesionService; // Si es necesario obtener miembros/invitados por sesión

    public List<Tarea> obtenerTareasPorSesion(int sesionId) {
        // Obtener IDs de todos los miembros e invitados asociados a esta sesión
        List<Integer> miembrosIds = sesionService.obtenerMiembrosPorSesion(sesionId).stream()
                .map(miembro -> miembro.getIdMiembro())
                .collect(Collectors.toList());

        List<Integer> invitadosIds = sesionService.obtenerInvitadosPorSesion(sesionId).stream()
                .map(invitado -> invitado.getIdInvitados())
                .collect(Collectors.toList());

        // Buscar tareas que coincidan con los miembros o invitados de la sesión
        List<Tarea> tareasParaMiembros = tareaRepository.findByTipoResponsableAndResponsableIdIn("miembro", miembrosIds);
        List<Tarea> tareasParaInvitados = tareaRepository.findByTipoResponsableAndResponsableIdIn("invitado", invitadosIds);

        // Combinar ambas listas y devolver
        tareasParaMiembros.addAll(tareasParaInvitados);
        return tareasParaMiembros;
    }

    public Tarea asignarTareaExistente(int tareaId, int responsableId, String tipoResponsable) {
        // Buscar la tarea por ID
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + tareaId));

        // Asignar el responsable basado en el tipo
        if ("miembro".equalsIgnoreCase(tipoResponsable)) {
            Miembro miembro = miembroRepository.findById(responsableId)
                    .orElseThrow(() -> new RuntimeException("Miembro no encontrado con ID: " + responsableId));
            tarea.setTipoResponsable("miembro");
            tarea.setResponsableId(miembro.getIdMiembro());
            tarea.setResponsable(miembro); // Asigna el objeto completo para detalles en el DTO
        } else if ("invitado".equalsIgnoreCase(tipoResponsable)) {
            Invitado invitado = invitadoRepository.findById(responsableId)
                    .orElseThrow(() -> new RuntimeException("Invitado no encontrado con ID: " + responsableId));
            tarea.setTipoResponsable("invitado");
            tarea.setResponsableId(invitado.getIdInvitados());
            tarea.setResponsable(invitado); // Asigna el objeto completo para detalles en el DTO
        } else {
            throw new IllegalArgumentException("Tipo de responsable no válido: " + tipoResponsable);
        }

        // Guardar la tarea actualizada en la base de datos
        return tareaRepository.save(tarea);
    }

    public List<Tarea> obtenerTareasAsignadas(int responsableId, String tipoResponsable) {
        return tareaRepository.findByResponsableIdAndTipoResponsable(responsableId, tipoResponsable);
    }
}

