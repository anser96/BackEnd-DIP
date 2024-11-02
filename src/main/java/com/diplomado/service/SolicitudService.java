package com.diplomado.service;

import com.diplomado.model.Sesion;
import com.diplomado.model.Solicitud;
import com.diplomado.model.dto.SesionDTO;
import com.diplomado.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    public Solicitud save(Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }

    public Solicitud findById(int idSolicitud) {
        return solicitudRepository.findById(idSolicitud).orElseThrow();
    }

    public List<Solicitud> getPendientes() {
        return solicitudRepository.findByEstado("PENDIENTE");
    }

    public Solicitud responder(int solicitudId, String respuesta) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId).orElseThrow();
        solicitud.setRespuesta(respuesta);
        return solicitudRepository.save(solicitud);
    }

    public List<Solicitud> findByEstado(String estado) {
        return solicitudRepository.findByEstado(estado);
    }

    public  List<Solicitud> findAll(){
        return solicitudRepository.findAll();
    }
}

