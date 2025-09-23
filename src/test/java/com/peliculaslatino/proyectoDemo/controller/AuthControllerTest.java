package com.peliculaslatino.proyectoDemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peliculaslatino.proyectoDemo.dto.auth.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debería devolver 201 y registrar usuario cuando los datos son válidos")
    void testRegistroExitoso() throws Exception {

        RegisterRequest req = new RegisterRequest("Luis Test13", "luis13@test.com", "123456");

        mockMvc.perform(post("/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated()) // 201
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("luis13@test.com"));
    }

    @Test
    @DisplayName("Debería devolver 400 cuando el correo ya está registrado")
    void testRegistroCorreoDuplicado() throws Exception {
        var request = Map.of("nombre","Luis Test10", "email","luis10@test.com", "password","1234566");

        // Primera vez crea el correo
        mockMvc.perform(post("/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Segunda vez debe fallar
        mockMvc.perform(post("/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El correo ya está registrado"));
    }

    @Test
    @DisplayName("Debería devolver 200 y token válido cuando las credenciales son correctas")
    void testLoginExitoso() throws Exception {
        var login = Map.of(
                "email", "luis1@test.com",
                "password","123456"
        );
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Debería devolver 404 y mensaje de usuario no encontrado cuando el correo no existe")
    void testLoginUsuarioNoExiste() throws Exception {
        var login = Map.of(
                "email", "noexiste@test.com",
                "password", "123456"
        );

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Credenciales inválidas"))
                .andExpect(jsonPath("$.mensaje").value("Correo o contraseña incorrectos"));
    }

    @Test
    @DisplayName("Debería devolver 401 y mensaje de credenciales inválidas cuando la contraseña es incorrecta")
    void testLoginContrasenaIncorrecta() throws Exception {
        var login = Map.of(
                "email", "luis@test.com",
                "password", "malaClave"
        );

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Correo o contraseña incorrectos"));
    }

}
