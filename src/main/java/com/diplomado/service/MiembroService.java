package com.diplomado.service;

import com.diplomado.model.Miembro;
import com.diplomado.repository.MiembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiembroService {
    @Autowired
    private MiembroRepository miembroRepository;

    public List<Miembro> findAllMiembros(){
        return miembroRepository.findAll();
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
}
