package com.petcare.login_service.service;

import com.petcare.login_service.client.UsuarioClient;
import com.petcare.login_service.dto.LoginRequest;
import com.petcare.login_service.dto.LoginResponse;
import com.petcare.login_service.dto.UsuarioDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    private UsuarioClient usuarioClient;
    private PasswordEncoder passwordEncoder;
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        usuarioClient = mock(UsuarioClient.class);
        passwordEncoder = mock(PasswordEncoder.class);
        loginService = new LoginService();
        // Inyectar los mocks manualmente
        var usuarioField = LoginService.class.getDeclaredFields()[0];
        usuarioField.setAccessible(true);
        try { usuarioField.set(loginService, usuarioClient); } catch (Exception e) { throw new RuntimeException(e); }
        var encoderField = LoginService.class.getDeclaredFields()[1];
        encoderField.setAccessible(true);
        try { encoderField.set(loginService, passwordEncoder); } catch (Exception e) { throw new RuntimeException(e); }
    }

    @Test
    void login_usuarioNoExiste() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@mail.com");
        when(usuarioClient.findByEmail("test@mail.com")).thenReturn(null);

        LoginResponse response = loginService.login(request);

        assertFalse(response.isSuccess());
        assertEquals("Usuario no encontrado", response.getMessage());
    }

    @Test
    void login_contraseñaIncorrecta() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@mail.com");
        request.setPassword("1234");
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setPassword("hashed");
        when(usuarioClient.findByEmail("test@mail.com")).thenReturn(usuario);
        when(passwordEncoder.matches("1234", "hashed")).thenReturn(false);

        LoginResponse response = loginService.login(request);

        assertFalse(response.isSuccess());
        assertEquals("Contraseña incorrecta", response.getMessage());
    }

    @Test
    void login_exitoso() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@mail.com");
        request.setPassword("1234");
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setPassword("hashed");
        when(usuarioClient.findByEmail("test@mail.com")).thenReturn(usuario);
        when(passwordEncoder.matches("1234", "hashed")).thenReturn(true);

        LoginResponse response = loginService.login(request);

        assertTrue(response.isSuccess());
        assertEquals("Login exitoso", response.getMessage());
    }

    @Test
    void login_errorConexion() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@mail.com");
        when(usuarioClient.findByEmail("test@mail.com")).thenThrow(new RuntimeException("Fallo de red"));

        LoginResponse response = loginService.login(request);

        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Error al conectar"));
    }
}