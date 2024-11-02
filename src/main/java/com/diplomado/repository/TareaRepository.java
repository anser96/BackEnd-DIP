package com.diplomado.repository;

import com.diplomado.model.Sesion;
import com.diplomado.model.Tarea;
import com.diplomado.model.dto.TareaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Integer> {
    List<Tarea> findByTipoResponsableAndResponsableIdIn(String tipoResponsable, List<Integer> responsableIds);
}
