package com.diplomado.repository;

import com.diplomado.model.Descripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescripcionRepository extends JpaRepository<Descripcion, Integer> {
}

