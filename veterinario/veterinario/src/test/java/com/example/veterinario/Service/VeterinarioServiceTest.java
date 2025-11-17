package com.example.veterinario.Service;

import com.example.veterinario.client.VeterinarioClient;
import com.example.veterinario.dto.UsuarioDTO;
import com.example.veterinario.model.Veterinario;
import com.example.veterinario.repository.VeterinarioRepository;
import com.example.veterinario.service.VeterinarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VeterinarioServiceTest {

    @InjectMocks
    private VeterinarioService veterinarioService;

    @Mock
    private VeterinarioRepository veterinariorepository;

    @Mock
    private VeterinarioClient veterinarioClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarVeterinarios_deberiaRetornarLista() {
        Veterinario v1 = new Veterinario(1L, 12345678, "Juan", "Perez", "Cardiologia", "juan@mail.com", 101L);
        Veterinario v2 = new Veterinario(2L, 87654321, "Ana", "Gomez", "Dermatologia", "ana@mail.com", 102L);

        when(veterinariorepository.findAll()).thenReturn(List.of(v1, v2));

        List<Veterinario> resultado = veterinarioService.listarVeterinarios();

        assertEquals(2, resultado.size());
        verify(veterinariorepository, times(1)).findAll();
    }

    @Test
    void agregarVeterinario_datosValidos_deberiaGuardarYRetornarVeterinario() {
        Veterinario v = new Veterinario(null, 12345678, "Juan", "Perez", "Cardiologia", "juan@mail.com", null);
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(101L);
        usuarioDTO.setRol("ADMINISTRADOR");

        // Mock que retorna usuario autenticado (para validar rol)
        when(veterinarioClient.findById(101L))
                .thenReturn(new ResponseEntity<>(usuarioDTO, HttpStatus.OK));

        // Cuando se llama a crear usuario, simula respuesta con ID nuevo
        when(veterinarioClient.crearUsuario(any(UsuarioDTO.class)))
                .thenReturn(new ResponseEntity<>(usuarioDTO, HttpStatus.CREATED));

        // Mock el guardado en repo, ya con usuarioId seteado
        Veterinario veterinarioGuardado = new Veterinario(1L, 12345678, "Juan", "Perez", "Cardiologia", "juan@mail.com",
                101L);
        when(veterinariorepository.save(any(Veterinario.class))).thenReturn(veterinarioGuardado);

        Veterinario resultado = veterinarioService.agregarVeterinario(v, 101L);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals(101L, resultado.getUsuarioId());

        verify(veterinarioClient, times(1)).findById(101L);
        verify(veterinarioClient, times(1)).crearUsuario(any(UsuarioDTO.class));
        verify(veterinariorepository, times(1)).save(any(Veterinario.class));
    }

    @Test
    void buscarVeterinarioPorId_cuandoExiste_deberiaRetornarVeterinario() {
        Veterinario v = new Veterinario(1L, 12345678, "Juan", "Perez", "Cardiologia", "juan@mail.com", 101L);

        when(veterinariorepository.findById(1L)).thenReturn(Optional.of(v));

        Veterinario resultado = veterinarioService.buscarVeterinarioPorId(1L);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        verify(veterinariorepository, times(1)).findById(1L);
    }

    @Test
    void eliminarVeterinario_deberiaLlamarDelete() {
        doNothing().when(veterinariorepository).deleteById(1L);

        veterinarioService.eliminarVeterinario(1L);

        verify(veterinariorepository, times(1)).deleteById(1L);
    }
}
