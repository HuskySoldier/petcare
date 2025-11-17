package com.petcare.register_service.controller;

import com.petcare.register_service.dto.RegisterResponse;
import com.petcare.register_service.dto.UsuarioDTO;
import com.petcare.register_service.service.RegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterControllerTest {

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private RegisterService registerService;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Ana");
        usuarioDTO.setApellido("Pérez");
        usuarioDTO.setEmail("ana@example.com");
        usuarioDTO.setTelefono("123456789");
        usuarioDTO.setPassword("clave123");
    }

    @Test
    void testRegistrarExitoso() {
        RegisterResponse expected = new RegisterResponse("Registro exitoso", usuarioDTO.getEmail(), "CLIENTE");

        when(registerService.register(usuarioDTO)).thenReturn(expected);

        RegisterResponse actual = registerController.registrar(usuarioDTO);

        assertEquals("Registro exitoso", actual.getMensaje());
        assertEquals("ana@example.com", actual.getEmail());
        assertEquals("CLIENTE", actual.getRol());
    }
}
