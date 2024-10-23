package com.diplomado.service;

import com.diplomado.model.Tarea;
import com.diplomado.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

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

    // Método para actualizar una tarea
    public Tarea updateTarea(int idTarea, Tarea tareaActualizada) {
        Tarea tarea = tareaRepository.findById(idTarea).orElseThrow(() ->
                new RuntimeException("Tarea no encontrada con el id: " + idTarea));
        tarea.setDescripcion(tareaActualizada.getDescripcion());
        tarea.setResponsable(tareaActualizada.getResponsable());
        tarea.setFechaLimite(tareaActualizada.getFechaLimite());
        return tareaRepository.save(tarea);
    }
}

