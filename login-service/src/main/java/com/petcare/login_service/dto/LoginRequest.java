package com.petcare.login_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    name = "LoginRequest",
    description = "DTO para la solicitud de inicio de sesión"
)
public class LoginRequest {
    @Schema(
        description = "Correo electrónico del usuario",
        example = "usuario@email.com",
        required = true
    )
    private String email;

    @Schema(
        description = "Contraseña del usuario",
        example = "123456",
        required = true
    )
    private String password;
}
