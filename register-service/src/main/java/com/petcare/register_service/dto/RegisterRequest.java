package com.petcare.register_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    name = "RegisterRequest",
    description = "DTO para la solicitud de registro de usuario"
)
public class RegisterRequest {
    @Schema(description = "Nombre del usuario", example = "Juan", required = true)
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez", required = true)
    private String apellido;

    @Schema(description = "Correo electrónico", example = "juan@email.com", required = true)
    private String email;

    @Schema(description = "Teléfono de contacto", example = "+56912345678", required = true)
    private String telefono;

    @Schema(description = "Contraseña del usuario", example = "123456", required = true)
    private String password;
}
