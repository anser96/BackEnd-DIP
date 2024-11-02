package com.diplomado.model;

import com.diplomado.model.dto.UsuarioDTO;

public class JwtResponse {
    private String token; // Token JWT generado
    private UsuarioDTO usuario; // Información del usuario sin la contraseña

    public JwtResponse() {
    }

    public JwtResponse(String token, UsuarioDTO usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }
}
