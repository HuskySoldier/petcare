package com.petcare.usuario.config;


import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.model.Rol;
import com.petcare.usuario.repository.UsuarioRepository;
import com.petcare.usuario.repository.RolRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepo, RolRepository rolRepo) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            Rol adminRol = rolRepo.findByNombre("ADMINISTRADOR").orElseGet(() -> rolRepo.save(Rol.builder().nombre("ADMINISTRADOR").build()));
            Rol clienteRol = rolRepo.findByNombre("CLIENTE").orElseGet(() -> rolRepo.save(Rol.builder().nombre("CLIENTE").build()));
            Rol vetRol = rolRepo.findByNombre("VETERINARIO").orElseGet(() -> rolRepo.save(Rol.builder().nombre("VETERINARIO").build()));

            if (usuarioRepo.findByEmail("admin@petcare.com").isEmpty()) {
                usuarioRepo.save(Usuario.builder()
                        .nombre("Admin")
                        .apellido("Principal")
                        .email("admin@petcare.com")
                        .password(encoder.encode("admin123"))
                        .telefono("111111111")
                        .rol(adminRol)
                        .build());
            }

            if (usuarioRepo.findByEmail("maria@petcare.com").isEmpty()) {
                usuarioRepo.save(Usuario.builder()
                        .nombre("María")
                        .apellido("López")
                        .email("maria@petcare.com")
                        .password(encoder.encode("maria123"))
                        .telefono("222222222")
                        .rol(clienteRol)
                        .build());
            }

            if (usuarioRepo.findByEmail("carlos.vet@petcare.com").isEmpty()) {
                usuarioRepo.save(Usuario.builder()
                        .nombre("Carlos")
                        .apellido("Vet")
                        .email("carlos.vet@petcare.com")
                        .password(encoder.encode("vet123"))
                        .telefono("333333333")
                        .rol(vetRol)
                        .build());
            }
            System.out.println(" Verificación completa: usuarios precargados si no existían.");
        };
    }
}
