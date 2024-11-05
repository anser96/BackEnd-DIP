package com.diplomado.repository;

import com.diplomado.model.Miembro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MiembroRepository extends JpaRepository<Miembro, Integer> {
    Optional<Miembro> findByEmail(String email);
}
