package com.diplomado.model.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private int idUsuario;
    private String nombre;
    private String correo;
    private String rol;
}
