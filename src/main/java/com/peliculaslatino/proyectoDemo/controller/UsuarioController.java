package com.peliculaslatino.proyectoDemo.controller;

import com.peliculaslatino.proyectoDemo.domain.usuario.UsuarioEntity;
import com.peliculaslatino.proyectoDemo.dto.ResetPasswordRequest;
import com.peliculaslatino.proyectoDemo.dto.UsuarioDTO;
import com.peliculaslatino.proyectoDemo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "Endpoints para listar usuarios, resetear contraseña, eliminar usuarios (ADMIN)")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(
            summary = "Listar usuarios",
            description = "Solo accesible para rol ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @PutMapping("/usuarios/{id}/reset-admin-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resetAdminPassword(@PathVariable Long id, @RequestBody ResetPasswordRequest req){
        UsuarioEntity u = service.getById(id);

        // Solo el admin puede cambiar su propia contraseña
        u.setPassword(passwordEncoder.encode(req.newPassword()));
        u.setRol(UsuarioEntity.Rol.ADMIN);

        service.save(u);
        return ResponseEntity.ok("Contraseña de admin reseteada con éxito");
    }

    //listar todos los usuarios (solo Admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioDTO> listar(){
        return service.listarTodos();
    }

    //Eliminar usuario por ID (Solo Admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
