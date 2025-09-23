package com.peliculaslatino.proyectoDemo.service;


import com.peliculaslatino.proyectoDemo.domain.usuario.UsuarioEntity;
import com.peliculaslatino.proyectoDemo.dto.FavoritoDTO;
import com.peliculaslatino.proyectoDemo.dto.FavoritoRequest;
import com.peliculaslatino.proyectoDemo.infra.exception.FavoritosExceptionHandler;
import com.peliculaslatino.proyectoDemo.repository.FavoritoRepository;
import com.peliculaslatino.proyectoDemo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class FavoritoServiceTest {

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FavoritoRepository favoritoRepository;

    @BeforeEach
    void limpiarTablas(){
        favoritoRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    private UsuarioEntity crearUsuario(String nombre, String email){
        UsuarioEntity u = new UsuarioEntity();
        u.setNombre(nombre);
        u.setEmail(email);
//        u.setPassword("pwd");
        u.setRol(UsuarioEntity.Rol.USER);
        return usuarioRepository.save(u);
    }

    private FavoritoRequest reqDemo(long peliculaId) {
        return new FavoritoRequest(
                peliculaId,
                "Titulo " + peliculaId,
                "https://img/" + peliculaId + ".jpg",
                "1999-01-01",
                "Sinopsis demo",
                8.7
        );
    }

    @Test
    @DisplayName("Debe permitir guardar una película en favoritos cuando no existe duplicado")
    void debeGuardarFavoritoCuandoNoExisteDuplicado(){
        UsuarioEntity user = crearUsuario("Luis", "luis@test.com");
        FavoritoRequest req = reqDemo(603L);

        var dto = favoritoService.guardar(user, req);

        assertNotNull(dto);
        assertNotNull(dto.id());
        assertEquals(user.getId(), dto.usuarioId());
        assertEquals(603L, dto.peliculaId());
        assertTrue(favoritoRepository.existsByUsuario_IdAndPeliculaId(user.getId(), 603L));
    }

    @Test
    @DisplayName("No debe permitir guardar una pelicula duplicada en favoritos")
    void noDebePermitirDuplicados(){
        UsuarioEntity user = crearUsuario("Ana", "ana@test.com");
        FavoritoRequest req = reqDemo(100L);

        favoritoService.guardar(user, req);

        assertThrows(
                FavoritosExceptionHandler.FavoritoDuplicadoException.class,
                () -> favoritoService.guardar(user, req)
        );

    }

    @Test
    @DisplayName("Debe listar las películas favoritas de un usuario")
    void debeListarFavoritos(){
        UsuarioEntity user = crearUsuario("Carlos", "carlos@test.com");

        favoritoService.guardar(user, reqDemo(101L));
        favoritoService.guardar(user, reqDemo(202L));
        favoritoService.guardar(user, reqDemo(303L));

        List<FavoritoDTO> favoritos = favoritoService.listaPorUsuario(user.getId());

        assertEquals(3, favoritos.size());
        assertTrue(favoritos.stream().anyMatch(f -> f.peliculaId().equals(101l)));
        assertTrue(favoritos.stream().anyMatch(f -> f.peliculaId().equals(202l)));
        assertTrue(favoritos.stream().anyMatch(f -> f.peliculaId().equals(303l)));
    }

    @Test
    @DisplayName("Debe devolver lista vacia si el usuario no tiene favoritos")
    void listaVaciaSiNoHayFavoritos(){
        UsuarioEntity user = crearUsuario("Laura", "laura@test.com");

        List<FavoritoDTO> favoritos = favoritoService.listaPorUsuario(user.getId());

        assertTrue(favoritos.isEmpty());
    }


    @Test
    @DisplayName("Debe eliminar una pelicula de favoritos si existe")
    void eliminarFavoritoExistente(){
        UsuarioEntity user = crearUsuario("Pedro", "pedro@test.com");
        FavoritoDTO favorito = favoritoService.guardar(user, reqDemo(777L));

        favoritoService.eliminar(user.getId(), favorito.peliculaId());

        List<FavoritoDTO> favoritos = favoritoService.listaPorUsuario(user.getId());
        assertTrue(favoritos.isEmpty(), "La lista debe quedar vacia después de eliminar");

    }

    @Test
    @DisplayName("Debe lanzar excepción si se intenta eliminar un favorito que no existe")
    void eliminarFavoritoInexistente(){
        UsuarioEntity user = crearUsuario("Ana","ana@test.com");

        assertThrows(FavoritosExceptionHandler.FavoritoNoEncontradoException.class,
                () -> favoritoService.eliminar(user.getId(), 999L),
                "Debe lanzar excepcion porque el favorito no existe");
    }





}
