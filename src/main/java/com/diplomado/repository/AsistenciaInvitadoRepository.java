package com.diplomado.repository;

import com.diplomado.model.AsistenciaInvitado;
import com.diplomado.model.AsistenciaInvitadoId;
import com.diplomado.model.AsistenciaMiembro;
import com.diplomado.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsistenciaInvitadoRepository extends JpaRepository<AsistenciaInvitado, AsistenciaInvitadoId> {
    List<AsistenciaInvitado> findBySesion(Sesion sesion);
}

