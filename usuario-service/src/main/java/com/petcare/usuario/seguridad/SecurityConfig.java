package com.petcare.usuario.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/usuarios/**",
                    "/usuario",
                    "/usuario/",
                    "/usuario/**",
                    "/usuarioservice/swagger-ui.html",
                    "/usuarioservice/swagger-ui/**",
                    "/usuarioservice/api-docs",
                    "/usuarioservice/api-docs/**"
                ).permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }

}
