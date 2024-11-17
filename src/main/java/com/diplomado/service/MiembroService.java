package com.diplomado.service;

import com.diplomado.model.Miembro;
import com.diplomado.model.Sesion;
import com.diplomado.model.dto.MiembroDTO;
import com.diplomado.repository.MiembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MiembroService {
    @Autowired
    private MiembroRepository miembroRepository;

    public List<MiembroDTO> findAllMiembros() {
        return miembroRepository.findAll().stream()
                .map(miembro -> MiembroDTO.builder()
                        .idMiembro(miembro.getIdMiembro())
                        .nombre(miembro.getNombre())
                        .numCedula(miembro.getNumCedula())
                        .cargo(miembro.getCargo())
                        .email(miembro.getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    public Miembro findById(int id){
        return miembroRepository.findById(id).orElse(null);
    }

    public Miembro save(Miembro miembro){
        return miembroRepository.save(miembro);
    }

    public void deleteById(int id){
        miembroRepository.deleteById(id);
    }

    public Miembro actualizarMiembro(int id, Miembro miembroDetalles) {
        Optional<Miembro> miembroExistente = miembroRepository.findById(id);

        if (miembroExistente.isPresent()) {
            Miembro miembro = miembroExistente.get();
            miembro.setNombre(miembroDetalles.getNombre());
            miembro.setCargo(miembroDetalles.getCargo());
            return miembroRepository.save(miembro);
        } else {
            throw new RuntimeException("Miembro no encontrado con id: " + id);
        }
    }
}
