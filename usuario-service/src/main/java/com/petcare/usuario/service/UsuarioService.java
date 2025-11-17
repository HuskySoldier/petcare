package com.petcare.usuario.service;

import com.petcare.usuario.DTO.RegisterRequest;
import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.model.Rol;
import com.petcare.usuario.repository.UsuarioRepository;
import com.petcare.usuario.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Usuario registrarUsuario(RegisterRequest request) {
        // Buscar el rol CLIENTE, si no existe lo crea
        Rol rolCliente = rolRepository.findByNombre("CLIENTE")
                .orElseGet(() -> rolRepository.save(Rol.builder().nombre("CLIENTE").build()));

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword())) // Cifrar la contraseña
                .telefono(request.getTelefono())
                .rol(rolCliente)
                .build();

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
