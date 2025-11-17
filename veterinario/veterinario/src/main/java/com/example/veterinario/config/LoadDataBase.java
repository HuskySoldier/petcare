package com.example.veterinario.config;

import com.example.veterinario.model.Veterinario;
import com.example.veterinario.repository.VeterinarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDataBase {

    @Bean
    CommandLineRunner initDatabase(VeterinarioRepository repository) {
        return args -> {
            if (repository.findAll().isEmpty()) {

                repository.save(new Veterinario(
                        null,
                        12345678,
                        "Carlos",
                        "Pérez",
                        "Cirugía",
                        "carlos.vet@petcare.com",
                        3L // ID de usuario Carlos en el microservicio usuario
                ));

                repository.save(new Veterinario(
                        null,
                        23456789,
                        "Ana",
                        "Soto",
                        "Medicina Interna",
                        "ana.vet@petcare.com",
                        4L // ID de usuario Ana
                ));

                repository.save(new Veterinario(
                        null,
                        34567890,
                        "Luis",
                        "Gómez",
                        "Odontología",
                        "luis.vet@petcare.com",
                        5L // ID de usuario Luis
                ));

                System.out.println(" Veterinarios precargados correctamente.");
            } else {
                System.out.println(" Ya existen veterinarios en la base de datos. No se insertaron duplicados.");
            }
        };
    }
}
