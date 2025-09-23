package com.peliculaslatino.proyectoDemo.controller;

import com.peliculaslatino.proyectoDemo.domain.usuario.UsuarioEntity;
import com.peliculaslatino.proyectoDemo.dto.ResetPasswordRequest;
import com.peliculaslatino.proyectoDemo.dto.UsuarioDTO;
import com.peliculaslatino.proyectoDemo.dto.auth.AuthResponse;
import com.peliculaslatino.proyectoDemo.dto.auth.LoginRequest;
import com.peliculaslatino.proyectoDemo.dto.auth.RegisterRequest;
import com.peliculaslatino.proyectoDemo.infra.security.TokenService;
import com.peliculaslatino.proyectoDemo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticación", description = "Endpoints para registro y login de usuarios")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Value("${jwt.expiration-seconds}")
    private long expiration;

    public AuthController(UsuarioService usuarioService,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authManager,
                          TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    @Operation(
            summary = "Registro de usuario",
            description = "Crea un nuevo usuario y devuelve un token JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya registrado"),

    })

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegisterRequest req) {
        if (usuarioService.existsByEmail(req.email())){
            log.info("Email ya esta registrado");
            throw new DataIntegrityViolationException("El correo ya está registrado");
        }
        UsuarioEntity u = new UsuarioEntity();
        u.setNombre(req.nombre());
        u.setEmail(req.email());
        u.setPassword(passwordEncoder.encode(req.password()));
        u.setRol(UsuarioEntity.Rol.valueOf("USER"));
        usuarioService.save(u);
        log.info("Usuario creado con exito ", req.email());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UsuarioDTO(u.getId(),u.getNombre(), u.getEmail()));
    }

    @Operation(
            summary = "Login de usuario",
            description = "Devuelve un token JWT al autenticar correctamente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req){
        Authentication auth = authManager.authenticate( new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        String token = tokenService.generarToken(req.email());
        log.info("Usuario logueado con éxito: {}", req.email());
        return ResponseEntity.ok(new AuthResponse(token, "Bearer", expiration));
    }

    //Resetear o cambiar contraseña
    @PutMapping("/usuarios/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id, @RequestBody ResetPasswordRequest req){
        UsuarioEntity u = usuarioService.getById(id);
        u.setPassword(passwordEncoder.encode(req.newPassword()));
        u.setRol(UsuarioEntity.Rol.USER);
        usuarioService.save(u);
        return ResponseEntity.ok("Contraseña reseteada con éxito");
    }
}
