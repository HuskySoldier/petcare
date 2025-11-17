package com.petcare.register_service.service;

import com.petcare.register_service.client.UsuarioClient;
import com.petcare.register_service.dto.RegisterResponse;
import com.petcare.register_service.dto.UsuarioDTO;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterServiceTest {

    @InjectMocks
    private RegisterService registerService;

    @Mock
    private UsuarioClient usuarioClient;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setApellido("López");
        usuarioDTO.setEmail("juan@example.com");
        usuarioDTO.setTelefono("987654321");
        usuarioDTO.setPassword("clave123");
    }

    @Test
    void testRegistroExitoso() {
        when(usuarioClient.buscarPorEmail(usuarioDTO.getEmail()))
                .thenThrow(mock(FeignException.NotFound.class));

        when(usuarioClient.crearUsuario(any(UsuarioDTO.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RegisterResponse response = registerService.register(usuarioDTO);

        assertEquals("Registro exitoso", response.getMensaje());
        assertEquals("juan@example.com", response.getEmail());
        assertEquals("CLIENTE", response.getRol());
    }

    @Test
    void testUsuarioYaExiste() {
        when(usuarioClient.buscarPorEmail(usuarioDTO.getEmail()))
                .thenReturn(usuarioDTO);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registerService.register(usuarioDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe un usuario"));
    }

    @Test
    void testCamposFaltantes() {
        usuarioDTO.setNombre("");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registerService.register(usuarioDTO);
        });

        assertEquals("Todos los campos son obligatorios", exception.getMessage());
    }

    @Test
    void testErrorFeignGenerico() {
        when(usuarioClient.buscarPorEmail(usuarioDTO.getEmail()))
                .thenThrow(mock(FeignException.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registerService.register(usuarioDTO);
        });

        assertEquals("Error al verificar si el usuario ya está registrado", exception.getMessage());
    }

    @Test
    void testErrorAlCrearUsuario() {
        when(usuarioClient.buscarPorEmail(usuarioDTO.getEmail()))
                .thenThrow(mock(FeignException.NotFound.class));
        when(usuarioClient.crearUsuario(any(UsuarioDTO.class)))
                .thenThrow(mock(FeignException.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registerService.register(usuarioDTO);
        });

        assertTrue(exception.getMessage().contains("Usuario ya registrado"));
    }
}
