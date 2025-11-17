package com.petcare.login_service.controller;

import com.petcare.login_service.dto.LoginRequest;
import com.petcare.login_service.dto.LoginResponse;
import com.petcare.login_service.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private LoginService loginService;
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        loginService = mock(LoginService.class);
        loginController = new LoginController();
        // Inyectar el mock manualmente
        var field = LoginController.class.getDeclaredFields()[0];
        field.setAccessible(true);
        try { field.set(loginController, loginService); } catch (Exception e) { throw new RuntimeException(e); }
    }

    @Test
    void login_exitoso() {
        LoginRequest request = new LoginRequest();
        LoginResponse response = new LoginResponse("Login exitoso", true);
        when(loginService.login(request)).thenReturn(response);

        ResponseEntity<LoginResponse> result = loginController.login(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Login exitoso", result.getBody().getMessage());
    }

    @Test
    void login_fallido() {
        LoginRequest request = new LoginRequest();
        LoginResponse response = new LoginResponse("Contraseña incorrecta", false);
        when(loginService.login(request)).thenReturn(response);

        ResponseEntity<LoginResponse> result = loginController.login(request);

        assertEquals(401, result.getStatusCodeValue());
        assertEquals("Contraseña incorrecta", result.getBody().getMessage());
    }
}