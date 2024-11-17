package com.diplomado.service;

import com.diplomado.model.Invitado;
import com.diplomado.model.Miembro;
import com.diplomado.model.dto.InvitadoDTO;
import com.diplomado.model.dto.MiembroDTO;
import com.diplomado.repository.InvitadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvitadoService {

    @Autowired
    private InvitadoRepository invitadoRepository;

    public List<InvitadoDTO> findAllInitados() {
        return invitadoRepository.findAll().stream()
                .map(invitado -> InvitadoDTO.builder()
                        .idInvitado(invitado.getIdInvitados())
                        .nombre(invitado.getNombre())
                        .numCedula(invitado.getNumCedula())
                        .dependencia(invitado.getDependencia())
                        .email(invitado.getEmail())
                        .build())
                .collect(Collectors.toList());
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
