package com.petcare.register_service.service;

import com.petcare.register_service.client.UsuarioClient;
import com.petcare.register_service.dto.*;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RegisterService {

    @Autowired
    private UsuarioClient usuarioClient;

    public RegisterResponse register(UsuarioDTO usuarioDTO) {

        // Validaciones adicionales por si las anotaciones @NotBlank fallan
        if (!StringUtils.hasText(usuarioDTO.getNombre()) ||
            !StringUtils.hasText(usuarioDTO.getApellido()) ||
            !StringUtils.hasText(usuarioDTO.getEmail()) ||
            !StringUtils.hasText(usuarioDTO.getPassword()) ||
            !StringUtils.hasText(usuarioDTO.getTelefono())) {
            throw new RuntimeException("Todos los campos son obligatorios");
        }

        try {
            UsuarioDTO existente = usuarioClient.buscarPorEmail(usuarioDTO.getEmail());
            if (existente != null && existente.getEmail() != null) {
                throw new RuntimeException("Ya existe un usuario registrado con ese correo electrónico");
            }
        } catch (FeignException.NotFound e) {
            // OK: El usuario no existe, se puede registrar
        } catch (FeignException e) {
            // Si el error es 404, significa que no existe, se puede registrar
            if (e.status() == 404) {
                // OK
            } else {
                throw new RuntimeException("Error al verificar si el usuario ya está registrado");
            }
        }

        // Crear nuevo usuario
        UsuarioDTO nuevo = new UsuarioDTO();
        nuevo.setNombre(usuarioDTO.getNombre());
        nuevo.setApellido(usuarioDTO.getApellido());
        nuevo.setEmail(usuarioDTO.getEmail());
        nuevo.setTelefono(usuarioDTO.getTelefono());
        // Enviar la contraseña en texto plano
        nuevo.setPassword(usuarioDTO.getPassword());
        // No se envía el rol, lo asigna usuario-service

        try {
            UsuarioDTO creado = usuarioClient.crearUsuario(nuevo);
            String rolNombre = (creado.getRol() != null) ? creado.getRol().getNombre() : null;
            return new RegisterResponse("Registro exitoso", creado.getEmail(), rolNombre);
        } catch (FeignException e) {
            throw new RuntimeException("Usuario ya registrado: " + e.getMessage());
        }
    }
}
