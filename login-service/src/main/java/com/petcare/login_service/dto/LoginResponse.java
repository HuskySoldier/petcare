package com.petcare.login_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
    name = "LoginResponse",
    description = "DTO para la respuesta de inicio de sesión"
)
public class LoginResponse {
    @Schema(
        description = "Mensaje de la respuesta",
        example = "Inicio de sesión exitoso"
    )
    private String message;

    @Schema(
        description = "Indica si el inicio de sesión fue exitoso",
        example = "true"
    )
    private boolean success;
}
