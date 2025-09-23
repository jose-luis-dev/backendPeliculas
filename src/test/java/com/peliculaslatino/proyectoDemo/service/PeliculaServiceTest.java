package com.peliculaslatino.proyectoDemo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ActiveProfiles("test")
public class PeliculaServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Aqui en MockBean esta una linea roja y dice Usage API marked for removal
    private ConsumoAPI consumoAPI;

    @Autowired
    private ConvierteDatos conversor;

    @Test
    @DisplayName("Debe devolver película al buscar por titulo - > 200")
    void buscarPeliculaPorTituloOk() throws Exception {
        String jsonSimulado = """
            {
              "results": [
                {
                  "id": 603,
                  "title": "Matrix",
                  "overview": "Un hacker descubre la verdad...",
                  "poster_path": "/poster.jpg",
                  "release_date": "1999-03-31",
                  "vote_average": 8.7
                }
              ]
            }
            """;

        Mockito.when(consumoAPI.obtenerDatos(Mockito.anyString()))
                .thenReturn(jsonSimulado);

        mockMvc.perform(get("/movie/search")
                .param("nombre", "Matrix"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Matrix"))
                .andExpect(jsonPath("$[0].sinopsis").value("Un hacker descubre la verdad..."));
    }

    @Test
    @DisplayName("Debe devolver 503 cuando la API externa no responde")
    void apiExternaNoDisponible() throws Exception {
        Mockito.when(consumoAPI.obtenerDatos(Mockito.anyString()))
                .thenThrow(new RuntimeException("TMDB no disponible"));

        mockMvc.perform(get("/movie/search")
                .param("nombre", "Matrix"))
                .andDo(print())
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("Servicio de películas no disponible"));
    }

    @Test
    @DisplayName("Debe devolver 400 cuando falta parámetro de búsqueda")
    void faltaParametroBusqueda() throws Exception {
        mockMvc.perform(get("/movie/search"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Parámetro requerido faltante"));
    }

    @Test
    @DisplayName("Debe devolver lista vacia si no hay resultados")
    void sinResultadosDeBusqueda() throws Exception {
        String jsonVacio = """
                {
                    "results":[]
                }
                """;

        Mockito.when(consumoAPI.obtenerDatos(Mockito.anyString()))
                .thenReturn(jsonVacio);
        mockMvc.perform(get("/movie/search").param("nombre","Desconocida"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Debe devolver 500 cuando la respuesta de TMDB es invalida")
    void respuestaTmdbInvalida() throws Exception {
        Mockito.when(consumoAPI.obtenerDatos(Mockito.anyString()))
                .thenReturn("json no válido");

        mockMvc.perform(get("/movie/search").param("nombre", "Matrix"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error interno del servidor"));
    }


}
