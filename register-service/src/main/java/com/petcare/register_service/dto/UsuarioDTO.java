package com.petcare.register_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    name = "UsuarioDTO",
    description = "DTO que representa un usuario para registro"
)
public class UsuarioDTO {
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @Schema(description = "Correo electrónico", example = "juan@email.com")
    private String email;

    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    private String telefono;

    @Schema(description = "Contraseña del usuario", example = "123456")
    private String password;

    // El rol ya no se envía desde register, pero puede recibirse desde usuario-service
    private RolDTO rol;
}
