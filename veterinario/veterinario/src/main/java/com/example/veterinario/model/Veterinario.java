package com.example.veterinario.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "veterinario")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
    name = "Veterinario",
    description = "Entidad que representa un veterinario"
)
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del veterinario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long veterinarioId;

    @Min(value = 1000000, message = "El RUT debe tener al menos 7 dígitos")
    @Column(nullable = false)
    @Schema(description = "RUT del veterinario", example = "12345678", required = true)
    private Integer rut;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    @Schema(description = "Nombre del veterinario", example = "Pedro", required = true)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Column(nullable = false)
    @Schema(description = "Apellido del veterinario", example = "González", required = true)
    private String apellido;

    @NotBlank(message = "La especialidad no puede estar vacía")
    @Column(nullable = false)
    @Schema(description = "Especialidad del veterinario", example = "Dermatología", required = true)
    private String especialidad;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El correo debe ser válido")
    @Column(nullable = false)
    @Schema(description = "Correo electrónico del veterinario", example = "pedro@vet.com", required = true)
    private String correo;

    @NotNull(message = "El usuarioId no puede ser nulo")
    @Column(nullable = false)
    @Schema(description = "Identificador del usuario asociado", example = "5", required = true)
    private Long usuarioId;
}

