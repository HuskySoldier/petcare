package com.petcare.register_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
    name = "RegisterResponse",
    description = "DTO para la respuesta de registro de usuario"
)
public class RegisterResponse {
    @Schema(description = "Mensaje de respuesta", example = "Registro exitoso")
    private String mensaje;

    @Schema(description = "Correo electrónico registrado", example = "juan@email.com")
    private String email;

    @Schema(description = "Rol asignado al usuario", example = "CLIENTE")
    private String rol;
}
