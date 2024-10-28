package com.diplomado.repository;

import com.diplomado.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String correo); // Buscar por correo para la autenticación

    boolean existsByCorreo(String correo); // Verificar si el correo ya está registrado
}