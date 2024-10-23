package com.diplomado.repository;

import com.diplomado.model.AsistenciaMiembro;
import com.diplomado.model.AsistenciaMiembroId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsistenciaMiembroRepository extends JpaRepository<AsistenciaMiembro, AsistenciaMiembroId> {
}

