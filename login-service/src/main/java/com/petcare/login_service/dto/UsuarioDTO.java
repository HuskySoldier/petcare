package com.petcare.login_service.dto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(
    name = "UsuarioDTO",
    description = "DTO que representa un usuario para login"
)
public class UsuarioDTO {
    @Schema(description = "Identificador del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Correo electrónico", example = "juan@email.com")
    private String email;

    @Schema(description = "Contraseña cifrada", example = "$2a$10$...")
    private String password;

    @Schema(description = "Rol del usuario", example = "CLIENTE")
    private RolDTO rol;



public UsuarioDTO register(UsuarioDTO usuarioDTO) {
    // Crear una instancia de PasswordEncoder
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    // Cifra la contraseña antes de almacenarla
    String encodedPassword = passwordEncoder.encode(usuarioDTO.getPassword());
    usuarioDTO.setPassword(encodedPassword);

    // Guardar el usuario en la base de datos
    // Lógica para guardar el usuario en la base de datos
    return usuarioDTO;
}
}
