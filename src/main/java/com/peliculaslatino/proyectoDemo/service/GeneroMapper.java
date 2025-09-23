package com.peliculaslatino.proyectoDemo.service;

import com.peliculaslatino.proyectoDemo.dto.GeneroDTO;
import com.peliculaslatino.proyectoDemo.model.Genero;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneroMapper {

    public GeneroDTO toDTO(Genero datos){
        return new GeneroDTO(
                Long.parseLong(String.valueOf(datos.idGenero())),
                datos.nombre()
        );
    }

    public List<GeneroDTO> toDTOList(List<Genero> lista){
        return lista.stream()
                .map(this::toDTO)
                .toList();
    }

}
