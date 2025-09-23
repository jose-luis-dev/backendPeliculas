package com.peliculaslatino.proyectoDemo.service;

import com.peliculaslatino.proyectoDemo.dto.PeliculaDTO;
import com.peliculaslatino.proyectoDemo.model.DatosPelicula;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PeliculaMapper {
    public PeliculaDTO toDTO(DatosPelicula datos){
        return new PeliculaDTO(
                Long.parseLong(datos.idPelicula()),
                datos.titulo(),
                datos.poster(),
                datos.fechaDeLazamiento(),
                datos.sinopsis(),
                Double.parseDouble(datos.evaluacion())
        );
    }

    public List<PeliculaDTO> toDTOList(List<DatosPelicula> lista){
        return lista.stream()
                .map(this::toDTO)
                .toList();
    }
}