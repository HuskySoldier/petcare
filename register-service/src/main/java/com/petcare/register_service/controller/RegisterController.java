package com.petcare.register_service.controller;

import com.petcare.register_service.dto.RegisterResponse;
import com.petcare.register_service.dto.UsuarioDTO;
import com.petcare.register_service.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Swagger OpenAPI imports
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Controlador para registro de usuarios.
 * 
 * Implementa endpoint con soporte para HATEOAS (en futuras versiones).
 */
@Tag(name = "Registro", description = "Operaciones de registro de usuarios")
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Permite registrar un nuevo usuario en el sistema.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Usuario registrado exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Datos inválidos"
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Usuario ya registrado"
            )
        }
    )
    @PostMapping
    public RegisterResponse registrar(
            @Parameter(description = "Datos del usuario a registrar", required = true) @RequestBody UsuarioDTO usuarioDTO) {
        return registerService.register(usuarioDTO);
    }
}

