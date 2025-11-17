package com.example.veterinario.Controller;

import com.example.veterinario.controller.VeterinarioController;
import com.example.veterinario.dto.UsuarioDTO;
import com.example.veterinario.model.Veterinario;
import com.example.veterinario.service.VeterinarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VeterinarioController.class)
class VeterinarioControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private VeterinarioService veterinarioService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void listarVeterinarios_cuandoHayDatos_deberiaRetornarOk() throws Exception {
                Veterinario v1 = new Veterinario(1L, 12345678, "Juan", "Perez", "Cardiologia", "juan@mail.com", 101L);
                Veterinario v2 = new Veterinario(2L, 87654321, "Ana", "Gomez", "Dermatologia", "ana@mail.com", 102L);

                when(veterinarioService.listarVeterinarios()).thenReturn(List.of(v1, v2));

                mockMvc.perform(get("/api/v1/veterinario/Total"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                                .andExpect(jsonPath("$[1].correo").value("ana@mail.com"));
        }

        @Test
        void listarVeterinarios_cuandoNoHayDatos_deberiaRetornarNoContent() throws Exception {
                when(veterinarioService.listarVeterinarios()).thenReturn(List.of());

                mockMvc.perform(get("/api/v1/veterinario/Total"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void obtenerVeterinarioPorId_cuandoExiste_deberiaRetornarOk() throws Exception {
                Veterinario v = new Veterinario(1L, 12345678, "Juan", "Perez", "Cardiologia", "juan@mail.com", 101L);

                when(veterinarioService.buscarVeterinarioPorId(1L)).thenReturn(v);

                mockMvc.perform(get("/api/v1/veterinario/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nombre").value("Juan"))
                                .andExpect(jsonPath("$.apellido").value("Perez"));
        }

        @Test
        void obtenerVeterinarioPorId_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
                when(veterinarioService.buscarVeterinarioPorId(999L)).thenReturn(null);

                mockMvc.perform(get("/api/v1/veterinario/999"))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("Veterinario con ID 999 no encontrado."));
        }

        @Test
        void agregarVeterinario_cuandoDatosValidos_deberiaRetornarCreated() throws Exception {
                Veterinario v = new Veterinario(null, 87654321, "Ana", "Gomez", "Dermatologia", "ana@mail.com", 102L);
                Veterinario vGuardado = new Veterinario(2L, 87654321, "Ana", "Gomez", "Dermatologia", "ana@mail.com",
                                102L);

                when(veterinarioService.agregarVeterinario(any(Veterinario.class), eq(101L))).thenReturn(vGuardado);

                mockMvc.perform(post("/api/v1/veterinario")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-USER-ID", "101")
                                .content(objectMapper.writeValueAsString(v)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.veterinarioId").value(2))
                                .andExpect(jsonPath("$.nombre").value("Ana"));
        }

        @Test
        void agregarVeterinario_cuandoServicioLanzaError_deberiaRetornarBadRequest() throws Exception {
                Veterinario v = new Veterinario(null, 87654321, "Ana", "Gomez", "Dermatologia", "ana@mail.com", 102L);

                when(veterinarioService.agregarVeterinario(any(Veterinario.class), eq(101L)))
                                .thenThrow(new IllegalArgumentException("Error: usuario no válido"));

                mockMvc.perform(post("/api/v1/veterinario")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-USER-ID", "101")
                                .content(objectMapper.writeValueAsString(v)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(
                                                org.hamcrest.Matchers.containsString("Error: usuario no válido")));
        }

        @Test
        void deleteVeterinarioPorId_cuandoExito_deberiaRetornarNoContent() throws Exception {
                when(veterinarioService.obtenerUsuarioDesdeClient(101L))
                                .thenReturn(new UsuarioDTO() {
                                        {
                                                setRol("ADMINISTRADOR");
                                        }
                                });
                doNothing().when(veterinarioService).validarAccesoPorRol("ADMINISTRADOR",
                                List.of("ADMINISTRADOR", "JEFE_CLINICA"));
                when(veterinarioService.buscarVeterinarioPorId(1L)).thenReturn(new Veterinario());

                doNothing().when(veterinarioService).eliminarVeterinario(1L);

                mockMvc.perform(delete("/api/v1/veterinario/1")
                                .header("X-USER-ID", "101"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void deleteVeterinarioPorId_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
                when(veterinarioService.obtenerUsuarioDesdeClient(101L))
                                .thenReturn(new UsuarioDTO() {
                                        {
                                                setRol("ADMINISTRADOR");
                                        }
                                });
                doNothing().when(veterinarioService).validarAccesoPorRol("ADMINISTRADOR",
                                List.of("ADMINISTRADOR", "JEFE_CLINICA"));
                when(veterinarioService.buscarVeterinarioPorId(999L)).thenReturn(null);

                mockMvc.perform(delete("/api/v1/veterinario/999")
                                .header("X-USER-ID", "101"))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("Veterinario no encontrado"));
        }

        @Test
        void modificarVeterinario_cuandoExito_deberiaRetornarOk() throws Exception {
                Veterinario vetOriginal = new Veterinario(1L, 12345678, "Juan", "Perez", "Cardiologia", "juan@mail.com",
                                101L);
                Veterinario vetModificado = new Veterinario(1L, 12345678, "Juan Carlos", "Perez", "Cardiologia",
                                "juan@mail.com", 101L);

                when(veterinarioService.obtenerUsuarioDesdeClient(101L))
                                .thenReturn(new UsuarioDTO() {
                                        {
                                                setRol("ADMINISTRADOR");
                                        }
                                });
                doNothing().when(veterinarioService).validarAccesoPorRol("ADMINISTRADOR",
                                List.of("ADMINISTRADOR", "JEFE_CLINICA"));
                when(veterinarioService.buscarVeterinarioPorId(1L)).thenReturn(vetOriginal);
                when(veterinarioService.guardarVeterinario(any(Veterinario.class))).thenReturn(vetModificado);

                mockMvc.perform(put("/api/v1/veterinario/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-USER-ID", "101")
                                .content(objectMapper.writeValueAsString(vetModificado)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nombre").value("Juan Carlos"));
        }

        @Test
        void modificarVeterinario_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
                Veterinario vetModificado = new Veterinario(1L, 12345678, "Juan Carlos", "Perez", "Cardiologia",
                                "juan@mail.com", 101L);

                when(veterinarioService.obtenerUsuarioDesdeClient(101L))
                                .thenReturn(new UsuarioDTO() {
                                        {
                                                setRol("ADMINISTRADOR");
                                        }
                                });
                doNothing().when(veterinarioService).validarAccesoPorRol("ADMINISTRADOR",
                                List.of("ADMINISTRADOR", "JEFE_CLINICA"));
                when(veterinarioService.buscarVeterinarioPorId(1L)).thenReturn(null);

                mockMvc.perform(put("/api/v1/veterinario/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-USER-ID", "101")
                                .content(objectMapper.writeValueAsString(vetModificado)))
                                .andExpect(status().isNotFound());
        }
}
