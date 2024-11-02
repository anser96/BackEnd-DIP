package com.diplomado.service;

import com.diplomado.model.Invitado;
import com.diplomado.model.Miembro;
import com.diplomado.repository.InvitadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitadoService {

    @Autowired
    private InvitadoRepository invitadoRepository;

    public List<Invitado> findAllInitados(){
        return invitadoRepository.findAll();
    }

    public Invitado findById(int id){
        return invitadoRepository.findById(id).orElse(null);
    }

    public Invitado save(Invitado invitado){
        return invitadoRepository.save(invitado);
    }

    public void deleteById(int id){
        invitadoRepository.deleteById(id);
    }
}
