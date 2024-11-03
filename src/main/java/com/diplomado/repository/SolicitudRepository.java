package com.diplomado.repository;

import com.diplomado.model.Sesion;
import com.diplomado.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {
    List<Solicitud> findByEstado(String estado);

    List<Solicitud> findBySesion(Sesion sesion);
}

