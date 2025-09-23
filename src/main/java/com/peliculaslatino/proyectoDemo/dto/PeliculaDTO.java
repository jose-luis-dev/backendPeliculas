package com.peliculaslatino.proyectoDemo.dto;

public record PeliculaDTO(Long id,
                          String titulo,
                          String poster,
                          String fecha,
                          String sinopsis,
                          Double evaluacion ) {}