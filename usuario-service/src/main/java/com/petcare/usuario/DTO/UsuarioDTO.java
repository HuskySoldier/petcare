package com.petcare.usuario.DTO;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String password;
    private String rol;
}

