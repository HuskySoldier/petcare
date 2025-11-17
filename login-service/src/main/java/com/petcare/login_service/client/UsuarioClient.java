package com.petcare.login_service.client;

import com.petcare.login_service.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "usuario-service", url = "http://localhost:8082")  // URL de UsuarioService
public interface UsuarioClient {

    
    @GetMapping("/usuarios/by-email")
    UsuarioDTO findByEmail(@RequestParam("email") String email);
}
