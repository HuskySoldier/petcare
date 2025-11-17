package com.petcare.login_service.controller;

import com.petcare.login_service.dto.LoginRequest;
import com.petcare.login_service.dto.LoginResponse;
import com.petcare.login_service.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Swagger OpenAPI imports
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Controlador para login de usuarios.
 * 
 * Implementa endpoint con soporte para HATEOAS (en futuras versiones).
 */
@Tag(name = "Login", description = "Operaciones de autenticación de usuarios")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Operation(
        summary = "Autenticar usuario",
        description = "Permite autenticar un usuario con email y contraseña.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Login exitoso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Credenciales inválidas"
            )
        }
    )
    @PostMapping
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "Credenciales de login", required = true) @RequestBody LoginRequest request) {
        LoginResponse response = loginService.login(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }
}
