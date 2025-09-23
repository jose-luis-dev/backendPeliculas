package com.peliculaslatino.proyectoDemo.controller;

import com.peliculaslatino.proyectoDemo.domain.favoritos.FavoritoEntity;
import com.peliculaslatino.proyectoDemo.domain.usuario.UsuarioEntity;
import com.peliculaslatino.proyectoDemo.repository.FavoritoRepository;
import com.peliculaslatino.proyectoDemo.repository.UsuarioRepository;
import com.peliculaslatino.proyectoDemo.service.FavoritoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
public class FavoritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FavoritoRepository favoritoRepository;

    private UsuarioEntity usuario, usuario2;

    @BeforeEach
    void setUp() {
        // Crear usuario1 de prueba
        usuario = new UsuarioEntity();
        usuario.setNombre("Usuario1 Test");
        usuario.setEmail("usuario1@test.com");
        usuario.setPassword("123456");
        usuario = usuarioRepository.save(usuario);

        // Crear usuario2 de prueba
        usuario2 = new UsuarioEntity();
        usuario2.setNombre("Usuario2 Test");
        usuario2.setEmail("usuario2@test.com");
        usuario2.setPassword("123456");
        usuario2 = usuarioRepository.save(usuario2);

        // Crear un favorito para usuario2
        FavoritoEntity favorito = new FavoritoEntity();
        favorito.setUsuario(usuario2);
        favorito.setPeliculaId(999L);
        favorito.setTitulo("Pelicula Usuario2");
        favorito.setPoster("poster.jpg");
        favorito.setFecha(LocalDate.now().toString());
        favorito.setSinopsis("Sipnosis");
        favorito.setEvaluacion(7.5);
        favoritoRepository.save(favorito);
    }

    @Test
    @DisplayName("Debe permitir guardar un favorito y devolver 201")
    @WithMockUser(username = "usuario1@test.com", roles = {"USER"})
    void guardarFavorito() throws Exception {
        String json = """
        {
            "peliculaId": 12345,
            "titulo": "Pelicula Test",
            "poster": "poster.jpg",
            "fecha": "2025-09-02",
            "sinopsis": "Una pelicula de prueba",
            "evaluacion": 8.5
        }
        """;

        mockMvc.perform(post("/favoritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Pelicula Test"));

    }


    @Test
    @DisplayName("No debe permitir guardar un favorito duplicado")
    @WithMockUser(username = "usuario1@test.com", roles = {"USER"})
    void noDebePermitirDuplicados() throws Exception {
        String json = """
                {
                    "peliculaId": 54321,
                    "titulo": "Pelicula Duplicada",
                    "poster": "poster.jpg",
                    "fecha": "2025-09-02",
                    "sinopsis": "Una pelicula de prueba Duplicada",
                    "evaluacion": 8.5
                }
                """;

        mockMvc.perform(post("/favoritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/favoritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isConflict());

    }

    @Test
    @DisplayName("Debe listar los favoritos del usuario")
    @WithMockUser(username = "usuario1@test.com", roles = {"USER"})
    void listarFavoritos() throws Exception {
        // Insertar 3 favoritos antes de consultar
        String json1 = """
        {"peliculaId": 1, "titulo": "Matrix", "poster": "m.jpg", "fecha": "2025-09-02", "sinopsis": "Neo descubre la verdad", "evaluacion": 9.0}
        """;
        String json2 = """
        {"peliculaId": 2, "titulo": "Terminator 2", "poster": "t2.jpg", "fecha": "2025-09-02", "sinopsis": "John Connor y el T-800", "evaluacion": 8.5}
        """;
        String json3 = """
        {"peliculaId": 3, "titulo": "The Godfather", "poster": "g.jpg", "fecha": "2025-09-02", "sinopsis": "La familia Corleone", "evaluacion": 9.2}
        """;

        mockMvc.perform(post("/favoritos").contentType(MediaType.APPLICATION_JSON).content(json1))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/favoritos").contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/favoritos").contentType(MediaType.APPLICATION_JSON).content(json3))
                .andExpect(status().isCreated());


        mockMvc.perform(get("/favoritos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

    }

    @Test
    @DisplayName("Debe eliminar un favorito y no aparecer en el listado")
    @WithMockUser(username = "usuario1@test.com", roles = {"USER"})
    void eliminarFavoritos() throws Exception {
        // Insertar favorito
        String json = """
                {
                  "peliculaId": 999,
                  "titulo": "Pelicula a eliminar",
                  "poster": "delete.jpg",
                  "fecha": "2025-09-02",
                  "sinopsis": "Se elimina en el test",
                  "evaluacion": 7.5
                }
                """;

        mockMvc.perform(post("/favoritos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isCreated());

        // Validación que aparece en el listado
        mockMvc.perform(get("/favoritos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Pelicula a eliminar"));

        // Se elimina la pelicula favorita
        mockMvc.perform(delete("/favoritos")
                .param("usuarioId", usuario.getId().toString())
                .param("peliculaId", "999"))
                .andExpect(status().isOk());

        // Se valida que no aparezca en el listado
        mockMvc.perform(get("/favoritos"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("No debe permitir eliminar un favorito inexistente")
    @WithMockUser(username = "usuario1@test.com", roles = {"USER"})
    void noDebeEliminarFavoritoInexistente() throws Exception {
        // Intentamos eliminar un favorito que no existe
        mockMvc.perform(delete("/favoritos")
                .param("usuarioId", usuario.getId().toString())
                .param("peliculaId", "99999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("La película no está en favoritos."));
    }

    @Test
    @DisplayName("Un usuario no puede eliminar favoritos de otro -> 403")
    @WithMockUser(username = "usuario1@test.com", roles = {"USER"})
    void noDebeEliminarFavoritoDeOtroUsuario() throws Exception {


        // Usuario1 intenta eliminar el favorito de usuario2
        mockMvc.perform(delete("/favoritos")
                .param("usuarioId", usuario2.getId().toString())
                .param("peliculaId", "999"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("Un usuario no puede listar favoritos de otro -> 403")
    @WithMockUser(username = "usuario1@test.com", roles = {"USER"})
    void noDebeListarFavoritosDeOtroUsuario() throws Exception {
        // Usuario1 intenta listar favoritos de usuario2
        mockMvc.perform(get("/favoritos/" + usuario2.getId()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin puede listar favoritos de otros usuarios -> 200")
    @Sql(statements = {
            "INSERT INTO usuarios (id, nombre, email, password, rol) " +
                    "VALUES (100, 'Admin Test', 'admin@test.com', '123456', 'ADMIN')"
    })
    @WithUserDetails(value = "admin@test.com")
    void adminDebeListarFavoritosDeOtroUsuario() throws Exception {
        mockMvc.perform(get("/favoritos/" + usuario2.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Pelicula Usuario2"));
    }

    @Test
    @DisplayName("Admin puede eliminar favoritos de otro usuario -> 200")
    @Sql(statements = {
            "INSERT INTO usuarios (id, nombre, email, password, rol) " +
                    "VALUES (101, 'Admin Test', 'admin@test.com', '123456', 'ADMIN')"
    })
    @WithUserDetails(value = "admin@test.com")
    void adminDebeEliminarFavoritoDeOtroUsuario() throws Exception {
        // Verifica que el usuario2 tiene 1 favorito antes
        mockMvc.perform(get("/favoritos/" + usuario2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        // Admin elimina el favorito de usuario2
        mockMvc.perform(delete("/favoritos")
                .param("usuarioId", usuario2.getId().toString())
                .param("peliculaId", "999"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
