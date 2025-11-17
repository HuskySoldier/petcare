package com.petcare.usuario.controller;

import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.model.Rol;
import com.petcare.usuario.repository.UsuarioRepository;
import com.petcare.usuario.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

// Swagger OpenAPI imports
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Controlador para gestión de usuarios.
 * 
 * Implementa endpoints con soporte para HATEOAS (en futuras versiones).
 */
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    // Buscar usuario por email
    @Operation(
        summary = "Buscar usuario por email",
        description = "Obtiene un usuario a partir de su email.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuario encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado"
            )
        }
    )
    @GetMapping("/by-email")
    public ResponseEntity<Usuario> findByEmail(
            @Parameter(description = "Email del usuario", required = true) @RequestParam String email) {
        return usuarioRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener usuario por ID
    @Operation(
        summary = "Obtener usuario por ID",
        description = "Obtiene un usuario a partir de su identificador.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuario encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado"
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear nuevo usuario
    @Operation(
        summary = "Crear nuevo usuario",
        description = "Crea un nuevo usuario en el sistema.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Usuario creado exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Datos inválidos"
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Usuario ya existe"
            )
        }
    )
    @PostMapping
    public Usuario createUsuario(
            @Parameter(description = "Datos del usuario a crear", required = true) @RequestBody Usuario usuario) {
        // Encriptar contraseña antes de guardar
        usuario.setPassword(encoder.encode(usuario.getPassword()));

        // Asignar rol CLIENTE si no se especifica
        if (usuario.getRol() == null) {
            Rol rolCliente = rolRepository.findByNombre("CLIENTE")
                    .orElseGet(() -> rolRepository.save(Rol.builder().nombre("CLIENTE").build()));
            usuario.setRol(rolCliente);
        }
        return usuarioRepository.save(usuario);
    }

    // PUT: Reemplazar todos los campos del usuario
    @Operation(
        summary = "Actualizar completamente un usuario",
        description = "Reemplaza todos los campos de un usuario existente.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuario actualizado exitosamente"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado"
            )
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUsuario(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id,
            @Parameter(description = "Datos actualizados del usuario", required = true) @RequestBody Usuario usuarioActualizado) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setEmail(usuarioActualizado.getEmail());

            // Si viene nueva password, encriptarla
            if (usuarioActualizado.getPassword() != null) {
                usuario.setPassword(encoder.encode(usuarioActualizado.getPassword()));
            }

            usuario.setRol(usuarioActualizado.getRol());
            usuarioRepository.save(usuario);
            return ResponseEntity.ok("Usuario actualizado exitosamente (PUT)");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // PATCH: Actualizar solo campos específicos del usuario
    @Operation(
        summary = "Actualizar parcialmente un usuario",
        description = "Actualiza uno o más campos de un usuario existente.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuario actualizado exitosamente"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado"
            )
        }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<String> patchUsuario(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id,
            @Parameter(description = "Datos parciales del usuario", required = true) @RequestBody Usuario usuarioParcial) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            if (usuarioParcial.getNombre() != null) {
                usuario.setNombre(usuarioParcial.getNombre());
            }
            if (usuarioParcial.getEmail() != null) {
                usuario.setEmail(usuarioParcial.getEmail());
            }
            if (usuarioParcial.getPassword() != null) {
                usuario.setPassword(encoder.encode(usuarioParcial.getPassword()));
            }
            if (usuarioParcial.getRol() != null) {
                usuario.setRol(usuarioParcial.getRol());
            }

            usuarioRepository.save(usuario);
            return ResponseEntity.ok("Usuario actualizado exitosamente (PATCH)");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT: Cambiar solo la contraseña (encriptada)
    @Operation(
        summary = "Actualizar contraseña de usuario",
        description = "Permite actualizar la contraseña de un usuario.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Contraseña actualizada exitosamente"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Solicitud inválida"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuario no encontrado"
            )
        }
    )
    @PutMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id,
            @Parameter(description = "Nueva contraseña", required = true) @RequestBody Map<String, String> request) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            String nuevaPassword = request.get("password");
            if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La contraseña no puede estar vacía.");
            }

            usuario.setPassword(encoder.encode(nuevaPassword));
            usuarioRepository.save(usuario);

            return ResponseEntity.ok("Contraseña actualizada exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }
    }
}
