package com.petcare.usuario_service.service;


import com.petcare.usuario.DTO.RegisterRequest;
import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.model.Rol;
import com.petcare.usuario_service.util.RolTestUtil;
import com.petcare.usuario.repository.UsuarioRepository;
import com.petcare.usuario.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private com.petcare.usuario.repository.RolRepository rolRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarUsuario() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Juan");
        request.setApellido("Pérez");
        request.setEmail("juan@example.com");
        request.setPassword("1234");
        request.setTelefono("99999999");

        Usuario usuarioMock = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(request.getPassword())
                .telefono(request.getTelefono())
                .rol(RolTestUtil.rolCliente())
                .build();

        when(rolRepository.findByNombre("CLIENTE")).thenReturn(Optional.of(RolTestUtil.rolCliente()));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        Usuario creado = usuarioService.registrarUsuario(request);

        assertEquals("juan@example.com", creado.getEmail());
        assertEquals("CLIENTE", creado.getRol().getNombre());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testBuscarPorEmail() {
        String email = "test@example.com";
        Usuario usuario = Usuario.builder().email(email).build();

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.buscarPorEmail(email);

        assertTrue(resultado.isPresent());
        assertEquals(email, resultado.get().getEmail());
    }
}
