package com.peliculaslatino.proyectoDemo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequiest(
        @NotBlank String nombre,
        @Email String email,
        @NotBlank String password
) {}
