package com.diplomado.model;

public class JwtResponse {
    private String token; // Token JWT generado

    // Constructor vacío
    public JwtResponse() {
    }

    // Constructor con parámetros
    public JwtResponse(String token) {
        this.token = token;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
