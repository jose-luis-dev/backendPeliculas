package com.peliculaslatino.proyectoDemo.dto;

import jakarta.validation.constraints.NotNull;

public record FavoritoRequest(
        @NotNull Long peliculaId,
        String titulo,
        String poster,
        String fecha,
        String sinopsis,
        Double evaluacion
) {}
