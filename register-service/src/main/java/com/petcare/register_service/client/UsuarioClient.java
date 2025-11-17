package com.petcare.register_service.client;

import com.petcare.register_service.dto.UsuarioDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "usuario-service", url = "http://localhost:8082/usuarios")
public interface UsuarioClient {

    
    @PostMapping 
    UsuarioDTO crearUsuario(@RequestBody UsuarioDTO usuarioDTO);


    @GetMapping("/by-email")
    UsuarioDTO buscarPorEmail(@RequestParam String email);}


