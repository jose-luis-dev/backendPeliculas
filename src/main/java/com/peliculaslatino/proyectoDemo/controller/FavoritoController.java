package com.peliculaslatino.proyectoDemo.controller;

import com.peliculaslatino.proyectoDemo.domain.usuario.UsuarioEntity;
import com.peliculaslatino.proyectoDemo.dto.FavoritoDTO;
import com.peliculaslatino.proyectoDemo.dto.FavoritoRequest;
import com.peliculaslatino.proyectoDemo.service.FavoritoService;
import com.peliculaslatino.proyectoDemo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Favoritos", description = "Endpoints para gestión de favoritos de usuarios")
@RestController
@RequestMapping("/favoritos")
public class FavoritoController {

    private final FavoritoService service;
    private final UsuarioService usuarioService;

    public FavoritoController(FavoritoService service, UsuarioService usuarioService){
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Agregar película a favoritos",
            description = "Permite a un usuario guardar una película en su lista de favoritos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Película agregada a favoritos"),
            @ApiResponse(responseCode = "409", description = "La película ya está en favoritos")
    })
    // Guardar peliculaFavorita
    @PostMapping
    public ResponseEntity <FavoritoDTO> guardar(@RequestBody @Valid FavoritoRequest req){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UsuarioEntity usuario = usuarioService.findByEmail(email);

        FavoritoDTO dto = service.guardar(usuario, req);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.id())
                .toUri();

        return ResponseEntity.created(location).body(dto);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> listar(@PathVariable Long usuarioId, Authentication authentication){
        String emailAuth = authentication.getName();
        UsuarioEntity actual = usuarioService.findByEmail(emailAuth);

        if (actual == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuario no encontrado");
        }

        // Si es ADMIN, puede listar favoritos de cualquier usuario
        if (actual.getRol() == UsuarioEntity.Rol.ADMIN) {
            return ResponseEntity.ok(service.listaPorUsuario(usuarioId));
        }

        if (!actual.getId().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var lista = service.listaPorUsuario(usuarioId);
        if (lista.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(service.listaPorUsuario(usuarioId));
    }

    @Operation(
            summary = "Listar favoritos del usuario",
            description = "Devuelve todas las películas guardadas en favoritos por el usuario autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado exitoso."),
            @ApiResponse(responseCode = "404", description = "No se encontraron favoritos.")
    })

    @GetMapping
    public ResponseEntity<?> listarFavoritos(Authentication authentication){
        String email = authentication.getName();
        UsuarioEntity usuario = usuarioService.findByEmail(email);

        var lista = service.listaPorUsuario(usuario.getId());
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @Operation(
            summary = "Eliminar película de favoritos",
            description = "Permite eliminar una película de la lista de favoritos por el usuario autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Película eliminada correctamente."),
            @ApiResponse(responseCode = "404", description = "Película no encontrada en favoritos.")
    })

    @DeleteMapping
    public ResponseEntity<?> eliminar(@RequestParam Long usuarioId,
                                      @RequestParam Long peliculaId,
                                      Authentication authentication){
        String emailAuth = authentication.getName();
        UsuarioEntity actual = usuarioService.findByEmail(emailAuth);

        if (actual == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuario no encontrado");
        }

        // Si es ADMIN, puede eliminar favoritos de cualquier usuario
        if (actual.getRol() == UsuarioEntity.Rol.ADMIN) {
            service.eliminar(usuarioId, peliculaId);
            return ResponseEntity.ok().build();
        }

        // Si no es ADMIN, solo puede eliminar sus propios favoritos
        if (!actual.getId().equals(usuarioId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        service.eliminar(usuarioId, peliculaId);
        return ResponseEntity.ok().build();
    }
}
