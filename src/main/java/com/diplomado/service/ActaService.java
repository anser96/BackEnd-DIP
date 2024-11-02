package com.diplomado.service;

import com.diplomado.model.Acta;
import com.diplomado.repository.ActaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Acta> findAll() {
        return actaRepository.findAll();
    }
}
