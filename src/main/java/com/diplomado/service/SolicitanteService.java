package com.diplomado.service;

import com.diplomado.model.Solicitante;
import com.diplomado.repository.SolicitanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SolicitanteService {

    @Autowired
    private SolicitanteRepository solicitanteRepository;

    public Solicitante save(Solicitante solicitante) {
        return solicitanteRepository.save(solicitante);
    }

    public List<Solicitante> findAll() {
        return solicitanteRepository.findAll();
    }

    public Solicitante findById(int id) {
        return solicitanteRepository.findById(id).orElseThrow(() -> new RuntimeException("Solicitante no encontrado con el id: " + id));
    }

    public Solicitante updateSolicitante(int id, Solicitante solicitanteActualizado) {
        Solicitante solicitante = solicitanteRepository.findById(id).orElseThrow(() -> new RuntimeException("Solicitante no encontrado con el id: " + id));
        solicitante.setNombre(solicitanteActualizado.getNombre());
        solicitante.setTipoDeSolicitante(solicitanteActualizado.getTipoDeSolicitante());
        solicitante.setEmail(solicitanteActualizado.getEmail());
        solicitante.setCelular(solicitanteActualizado.getCelular());
        return solicitanteRepository.save(solicitante);
    }

    public void deleteById(int id) {
        solicitanteRepository.deleteById(id);
    }
}

