package com.peliculaslatino.proyectoDemo.dto;

public record FavoritoDTO(
        Long id,
        Long usuarioId,
        Long peliculaId,
        String titulo,
        String poster,
        String fecha,
        String sinopsis,
        Double evaluacion
) {}
