package com.diplomado.service;

import com.diplomado.model.Descripcion;
import com.diplomado.repository.DescripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DescripcionService {

    @Autowired
    private DescripcionRepository descripcionRepository;

    public Descripcion save(Descripcion descripcion) {
        return descripcionRepository.save(descripcion);
    }

    public List<Descripcion> findAll() {
        return descripcionRepository.findAll();
    }

    public Descripcion findById(int id) {
        return descripcionRepository.findById(id).orElseThrow(() -> new RuntimeException("Descripcion no encontrada con el id: " + id));
    }

    public Descripcion updateDescripcion(int id, Descripcion descripcionActualizada) {
        Descripcion descripcion = descripcionRepository.findById(id).orElseThrow(() -> new RuntimeException("Descripcion no encontrada con el id: " + id));
        descripcion.setEstuImplicados(descripcionActualizada.getEstuImplicados());
        descripcion.setNumEstuImplicados(descripcionActualizada.getNumEstuImplicados());
        descripcion.setDocenImplicados(descripcionActualizada.getDocenImplicados());
        descripcion.setNumDocenImplicados(descripcionActualizada.getNumDocenImplicados());
        descripcion.setCuidadImplicada(descripcionActualizada.getCuidadImplicada());
        descripcion.setPaisImplicado(descripcionActualizada.getPaisImplicado());
        descripcion.setEvento(descripcionActualizada.getEvento());
        return descripcionRepository.save(descripcion);
    }

    public void deleteById(int id) {
        descripcionRepository.deleteById(id);
    }
}

