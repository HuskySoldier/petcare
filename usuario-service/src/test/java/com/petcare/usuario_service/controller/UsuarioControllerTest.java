package com.petcare.usuario_service.controller;


import com.petcare.usuario.controller.UsuarioController;
import com.petcare.usuario.model.Usuario;
// import com.petcare.usuario.model.Rol;
import com.petcare.usuario_service.util.RolTestUtil;
import com.petcare.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setEmail("juan@correo.com");
        usuario.setPassword("password");
        usuario.setRol(RolTestUtil.rolCliente());
    }

    @Test
    void testFindByEmail_UserExists() {
        when(usuarioRepository.findByEmail("juan@correo.com")).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> response = usuarioController.findByEmail("juan@correo.com");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        Usuario body = response.getBody();
        assertNotNull(body);
        assertEquals("juan@correo.com", body.getEmail());
        assertEquals("CLIENTE", body.getRol().getNombre());
        verify(usuarioRepository, times(1)).findByEmail("juan@correo.com");
    }

    @Test
    void testFindByEmail_UserNotFound() {
        when(usuarioRepository.findByEmail("noexiste@correo.com")).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioController.findByEmail("noexiste@correo.com");

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(usuarioRepository, times(1)).findByEmail("noexiste@correo.com");
    }
}