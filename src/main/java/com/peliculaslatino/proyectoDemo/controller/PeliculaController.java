package com.peliculaslatino.proyectoDemo.controller;

import com.peliculaslatino.proyectoDemo.dto.GeneroDTO;
import com.peliculaslatino.proyectoDemo.dto.PeliculaDTO;
import com.peliculaslatino.proyectoDemo.model.DatosPelicula;
import com.peliculaslatino.proyectoDemo.model.RespuestaBusqueda;
import com.peliculaslatino.proyectoDemo.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "Peliculas", description = "Endpoints relacionados con búsqueda de películas en TMDB")
@RestController
@RequestMapping("/movie")
public class PeliculaController {

    private final PeliculaService peliculaService;

    public PeliculaController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private ConvierteDatos conversor;

    @Autowired
    private PeliculaMapper mapper;

    @Autowired
    private GeneroMapper generoMapper;

    @Operation(
            summary = "Buscar películas por nombre",
            description = "Devuelve una lista de películas usando TMDB."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Películas encontradas"),
            @ApiResponse(responseCode = "400", description = "Falta parámetro de búsqueda"),
            @ApiResponse(responseCode = "503", description = "Servicio de películas no disponible")
    })

    //Buscar titulo de peliculas
    @GetMapping("/search")
    public List<PeliculaDTO> buscar(@RequestParam String nombre){
        String json = consumoAPI.obtenerDatos("/search/movie?query="+ nombre.replace(" ", "+")+"&language=es-MX&page=1");
        RespuestaBusqueda respuesta = conversor.obtenerDatos(json, RespuestaBusqueda.class);
        return mapper.toDTOList(respuesta.results());
    }

    //Ver informacion de una pelicula por ID
    @GetMapping("/{idPelicula}")
    public List<PeliculaDTO> obtenerDetallePelicula(@PathVariable int idPelicula) {
        String json = consumoAPI.obtenerDatos("/movie/" + idPelicula + "?language=es-MX");
        DatosPelicula datos = conversor.obtenerDatos(json, DatosPelicula.class);
        return mapper.toDTOList(Collections.singletonList(datos));
    }

    // Buscar peliculas mejor valoradas
    @GetMapping("/top_rated")
    public List<PeliculaDTO> obtenerMejorValoradosPeliculas(){
        String json = consumoAPI.obtenerDatos("/movie/top_rated?language=es-MX&page=1");
        RespuestaBusqueda respuesta = conversor.obtenerDatos(json, RespuestaBusqueda.class);
        return mapper.toDTOList(respuesta.results());
    }

    // Buscar peliculas proximo a estrenar
    @GetMapping("/upcoming")
    public List<PeliculaDTO> obtenerProximosEstrenos(){
        String json = consumoAPI.obtenerDatos("/movie/upcoming?language=es-MX&page=1");
        RespuestaBusqueda respuesta = conversor.obtenerDatos(json, RespuestaBusqueda.class);
        return mapper.toDTOList(respuesta.results());
    }

    //  Peliculas ahora en reproduccion
    @GetMapping("/now_playing")
    public List<PeliculaDTO> obtenerPeliculasEnReproduccion(){
        String json = consumoAPI.obtenerDatos("/movie/now_playing?language=es-MX&page=1");
        RespuestaBusqueda respuesta = conversor.obtenerDatos(json, RespuestaBusqueda.class);
        return mapper.toDTOList(respuesta.results());
    }

    //Mostrar peliculas popularidad descendientes
    @GetMapping("/discover")
    public List<PeliculaDTO> descubrirPeliculas(){
        String json = consumoAPI.obtenerDatos("/discover/movie?sort_by=popularity.desc&language=es-MX&page=1");
        RespuestaBusqueda respuesta = conversor.obtenerDatos(json, RespuestaBusqueda.class);
        return mapper.toDTOList(respuesta.results());
    }


    //Buscar peliculas populares
    @GetMapping("/popular")
    public List<PeliculaDTO> obtenerPopulares(){
        return peliculaService.obtenerPeliculasPopulares();
    }


    // Lista de generos
    @GetMapping("/genres")
    public List<GeneroDTO> obtenerGeneros(){
        return peliculaService.obtenerGeneros();
    }

    //Buscar Peliculas Por Genero
    @GetMapping("/genre/{idGenero}")
    public List<PeliculaDTO> obtenerPeliculasPorGenero(@PathVariable Long idGenero){
        return peliculaService.obtenerPeliculasPorGenero(idGenero);
    }

    //PeliculasSimilares
    @GetMapping("/{idPelicula}/similar")
    public List<PeliculaDTO> obtenerPeliculasSimilares(@PathVariable Long idPelicula){
        return peliculaService.obtenerPeliculasSimilares(idPelicula);
    }


}
