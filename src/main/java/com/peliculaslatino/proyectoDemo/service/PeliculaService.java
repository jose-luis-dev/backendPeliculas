package com.peliculaslatino.proyectoDemo.service;

import com.peliculaslatino.proyectoDemo.dto.GeneroDTO;
import com.peliculaslatino.proyectoDemo.dto.PeliculaDTO;
import com.peliculaslatino.proyectoDemo.model.DatosPelicula;
import com.peliculaslatino.proyectoDemo.model.RespuestaBusqueda;
import com.peliculaslatino.proyectoDemo.model.RespuestaGeneros;
import com.peliculaslatino.proyectoDemo.model.RespuestaSimilares;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeliculaService {

    private final PeliculaMapper peliculaMapper;
    private final GeneroMapper generoMapper;
    private final ConsumoAPI consumoAPI;
    private final IConvierteDatos conversor;

    public PeliculaService(PeliculaMapper peliculaMapper,
                           GeneroMapper generoMapper,
                           ConsumoAPI consumoAPI,
                           ConvierteDatos conversor){
        this.peliculaMapper = peliculaMapper;
        this.generoMapper = generoMapper;
        this.consumoAPI = consumoAPI;
        this.conversor = conversor;
    }


    //Peliculas Populares
    @Cacheable("peliculasPopulares")
    public List<PeliculaDTO> obtenerPeliculasPopulares(){
        String json = consumoAPI.obtenerDatos("/movie/popular?language=es-MX&page=1");
        RespuestaBusqueda  respuesta = conversor.obtenerDatos(json, RespuestaBusqueda.class);
        return peliculaMapper.toDTOList(respuesta.results());
    }

    // Peliculas por genero
    @Cacheable(value = "peliculasPorGenero", key = "#generoId")
    public List<PeliculaDTO> obtenerPeliculasPorGenero(Long generoId) {
        String json = consumoAPI.obtenerDatos("/discover/movie?with_genres=" + generoId + "&language=es-MX&page=1");
        RespuestaBusqueda respuesta = conversor.obtenerDatos(json, RespuestaBusqueda.class);
        return peliculaMapper.toDTOList(respuesta.results());
    }

    // Lista de generos disponibles
    @Cacheable("generos")
    public List<GeneroDTO> obtenerGeneros() {
        String json = consumoAPI.obtenerDatos("/genre/movie/list?language=es-MX");
        RespuestaGeneros respuesta = conversor.obtenerDatos(json, RespuestaGeneros.class);
        return generoMapper.toDTOList(respuesta.genres());
    }

    //Detalle de la pelicula
    @Cacheable(value = "detallePelicula", key = "#idPelicula")
    public PeliculaDTO obtenerDetallePelicula(Long idPelicula){
        String json = consumoAPI.obtenerDatos("/movie/"+ idPelicula + "?language=es-MX");
        DatosPelicula datos = conversor.obtenerDatos(json, DatosPelicula.class);
        return peliculaMapper.toDTO(datos);
    }

    //Pelicula similares
    @Cacheable(value = "peliculasSimilares", key = "#idPelicula")
    public List<PeliculaDTO> obtenerPeliculasSimilares(Long idPelicula){
        String json = consumoAPI.obtenerDatos("/movie/" + idPelicula + "/similar?language=es-MX&page=1");
        RespuestaSimilares respuesta = conversor.obtenerDatos(json, RespuestaSimilares.class);
        return peliculaMapper.toDTOList(respuesta.results());
    }
}
