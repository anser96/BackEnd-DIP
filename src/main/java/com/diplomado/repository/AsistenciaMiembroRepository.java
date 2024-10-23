package com.diplomado.repository;

import com.diplomado.model.AsistenciaMiembro;
import com.diplomado.model.AsistenciaMiembroId;
import com.diplomado.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsistenciaMiembroRepository extends JpaRepository<AsistenciaMiembro, AsistenciaMiembroId> {
    List<AsistenciaMiembro> findBySesion(Sesion sesion);
}

