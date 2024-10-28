package com.diplomado.security;
import com.diplomado.model.Usuario;
import com.diplomado.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo));

        // Convertir el usuario a un objeto UserDetails de Spring Security
        return new User(usuario.getCorreo(), usuario.getContrasena(), Collections.emptyList());
    }
}