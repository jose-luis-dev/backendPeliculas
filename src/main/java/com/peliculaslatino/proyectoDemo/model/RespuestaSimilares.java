package com.peliculaslatino.proyectoDemo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RespuestaSimilares(@JsonAlias("results") List<DatosPelicula> results) {
}
