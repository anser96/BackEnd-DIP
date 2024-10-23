package com.diplomado.repository;

import com.diplomado.model.Acta;
import com.diplomado.model.AsistenciaMiembro;
import com.diplomado.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActaRepository extends JpaRepository<Acta, Integer> {
    List<Acta> findBySesion(Sesion sesion);
}

