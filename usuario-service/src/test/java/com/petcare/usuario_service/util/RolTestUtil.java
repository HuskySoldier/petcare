package com.petcare.usuario_service.util;

import com.petcare.usuario.model.Rol;

public class RolTestUtil {
    public static Rol rolCliente() {
        return Rol.builder().id(1L).nombre("CLIENTE").build();
    }
    public static Rol rolAdmin() {
        return Rol.builder().id(2L).nombre("ADMINISTRADOR").build();
    }
    public static Rol rolVeterinario() {
        return Rol.builder().id(3L).nombre("VETERINARIO").build();
    }
}
