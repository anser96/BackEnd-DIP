package com.diplomado.repository;

import com.diplomado.model.Acta;
import com.diplomado.model.AsistenciaMiembro;
import com.diplomado.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActaRepository extends JpaRepository<Acta, Integer> {
    List<Acta> findBySesion(Sesion sesion);

    boolean existsByNumeroActa(String numeroActa);

    @Query("SELECT a FROM Acta a WHERE a.estado = 'APROBADA' ORDER BY a.idActa DESC")
    Optional<Acta> findUltimaActaAprobada();
}

