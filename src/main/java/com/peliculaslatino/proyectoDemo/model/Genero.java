package com.peliculaslatino.proyectoDemo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Genero(
        @JsonAlias("id") int idGenero,
        @JsonAlias("name")String nombre) {
}
