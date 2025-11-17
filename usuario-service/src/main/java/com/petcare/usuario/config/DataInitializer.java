package com.petcare.usuario.config;

import com.petcare.usuario.model.Rol;
import com.petcare.usuario.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initRoles(RolRepository rolRepository) {
        return args -> {
            if (rolRepository.findByNombre("CLIENTE").isEmpty()) {
                rolRepository.save(Rol.builder().nombre("CLIENTE").build());
            }
        };
    }
}
