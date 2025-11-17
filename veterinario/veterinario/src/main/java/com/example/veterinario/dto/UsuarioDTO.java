package com.example.veterinario.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;       // Necesario para recibir el ID del usuario creado
    private String nombre;
    private String apellido;
    private String email;   // Agregar si quieres setear el correo
    private String rol;
    private String telefono;
    private String password;  // Si quieres crear el usuario con clave
}

