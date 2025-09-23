package com.peliculaslatino.proyectoDemo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record DatosPelicula (@JsonAlias("id")String idPelicula,
                             @JsonAlias("title")String titulo,
                             @JsonAlias("poster_path")String poster,
                             @JsonAlias("release_date")String fechaDeLazamiento,
                             @JsonAlias("overview")String sinopsis,
                             @JsonAlias("vote_average")String evaluacion){}
