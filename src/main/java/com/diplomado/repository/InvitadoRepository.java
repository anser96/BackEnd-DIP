package com.diplomado.repository;

import com.diplomado.model.Invitado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvitadoRepository extends JpaRepository<Invitado, Integer> {
    Optional<Invitado> findByEmail(String email);
}

