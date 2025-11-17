package com.example.veterinario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.veterinario.client.VeterinarioClient;
import com.example.veterinario.dto.UsuarioDTO;
import com.example.veterinario.model.Veterinario;
import com.example.veterinario.repository.VeterinarioRepository;

@Service
public class VeterinarioService {
    @Autowired
    private VeterinarioRepository veterinariorepository;

    public List<Veterinario> listarVeterinarios() {
        return veterinariorepository.findAll();
    }

    @Autowired
    private VeterinarioClient veterinarioClient;

    public Veterinario agregarVeterinario(Veterinario veterinario, Long idUsuario) {
        // Validar que quien hace la petición tenga rol válido
        UsuarioDTO usuarioAutenticado = obtenerUsuarioDesdeClient(idUsuario);
        validarAccesoPorRol(usuarioAutenticado.getRol(), List.of("ADMINISTRADOR", "JEFE_CLINICA"));

        // Si no viene usuarioId, creamos el usuario en el microservicio de usuario
        if (veterinario.getUsuarioId() == null) {
            UsuarioDTO nuevoUsuario = new UsuarioDTO();
            nuevoUsuario.setNombre(veterinario.getNombre());
            nuevoUsuario.setApellido(veterinario.getApellido());
            nuevoUsuario.setEmail(veterinario.getCorreo());
            nuevoUsuario.setRol("VETERINARIO");
            nuevoUsuario.setTelefono("12345678"); // Puedes recibir este dato si quieres
            nuevoUsuario.setPassword("123456"); // Puedes generar un password aleatorio o recibirlo

            ResponseEntity<UsuarioDTO> response = veterinarioClient.crearUsuario(nuevoUsuario);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalArgumentException("No se pudo crear el usuario asociado.");
            }

            Long nuevoUsuarioId = response.getBody().getId();
            if (nuevoUsuarioId == null) {
                throw new IllegalArgumentException("El usuario creado no devolvió un ID válido.");
            }

            veterinario.setUsuarioId(nuevoUsuarioId);
        }

        // Si llega aquí, usuarioId ya existe (nuevo o enviado) => guardamos veterinario
        return veterinariorepository.save(veterinario);
    }

    // Método auxiliar para obtener usuario desde cliente
    public UsuarioDTO obtenerUsuarioDesdeClient(Long idUsuario) {
        ResponseEntity<UsuarioDTO> response = veterinarioClient.findById(idUsuario);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Usuario autenticado no encontrado o no válido");
        }
        return response.getBody();
    }

    // Método para validar roles
    public void validarAccesoPorRol(String rolUsuario, List<String> rolesPermitidos) {
        if (rolUsuario == null || !rolesPermitidos.contains(rolUsuario.toUpperCase())) {
            throw new RuntimeException("Acceso denegado: no tienes permisos para realizar esta acción.");
        }
    }

    public Veterinario buscarVeterinarioPorId(Long id) {
        return veterinariorepository.findById(id).orElse(null);
    }

    public void eliminarVeterinario(Long id) {
        veterinariorepository.deleteById(id);
    }

    public Veterinario guardarVeterinario(Veterinario veterinario) {
        return veterinariorepository.save(veterinario);
    }

}
