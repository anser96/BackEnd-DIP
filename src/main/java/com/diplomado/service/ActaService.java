package com.diplomado.service;

import com.diplomado.model.Acta;
import com.diplomado.model.dto.ActaDTO;
import com.diplomado.repository.ActaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActaService {

    @Autowired
    private ActaRepository actaRepository;

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
                        .idSesion(acta.getSesion().getIdSesion()) // Incluyendo el idSesion
                        .build())
                .collect(Collectors.toList());
    }
}
