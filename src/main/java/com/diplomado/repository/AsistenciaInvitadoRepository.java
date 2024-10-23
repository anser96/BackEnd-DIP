package com.diplomado.repository;

import com.diplomado.model.AsistenciaInvitado;
import com.diplomado.model.AsistenciaInvitadoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsistenciaInvitadoRepository extends JpaRepository<AsistenciaInvitado, AsistenciaInvitadoId> {
}

