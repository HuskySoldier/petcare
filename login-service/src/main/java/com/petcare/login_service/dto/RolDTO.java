package com.petcare.login_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    name = "RolDTO",
    description = "DTO para el rol del usuario (login-service)"
)
public class RolDTO {
    @Schema(description = "Nombre del rol", example = "CLIENTE")
    private String nombre;
}
