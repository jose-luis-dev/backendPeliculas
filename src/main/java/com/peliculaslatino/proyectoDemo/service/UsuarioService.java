package com.peliculaslatino.proyectoDemo.service;

import com.peliculaslatino.proyectoDemo.domain.usuario.UsuarioEntity;
import com.peliculaslatino.proyectoDemo.dto.UsuarioDTO;
import com.peliculaslatino.proyectoDemo.dto.UsuarioRequiest;
import com.peliculaslatino.proyectoDemo.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;

    public  UsuarioService(UsuarioRepository repo, PasswordEncoder passwordEncoder){
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }


    // Registrar nuevo usuario
    public UsuarioDTO registrar(UsuarioRequiest req) {

        //Verificar si ya existe el usuario
        if (repo.existsByEmail(req.email())){
            throw new RuntimeException("El email ya está registrado");
        }

        UsuarioEntity u = new UsuarioEntity();
        u.setNombre(req.nombre());
        u.setEmail(req.email());
        // Encriptar antes de guardar
        u.setPassword(passwordEncoder.encode( req.password()));

        u.setRol(UsuarioEntity.Rol.USER);
        u = repo.save(u);
        return new UsuarioDTO(u.getId(), u.getNombre(), u.getEmail());
    }

    //Implementación requerida
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No existe usuario con email: " + email));
    }
    // Se agrega metodos de utilidad
    public UsuarioEntity getById(Long id){
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    public UsuarioEntity save(UsuarioEntity u) {
        return repo.save(u);
    }

    public UsuarioEntity findByEmail(String email){
        return repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    public List<UsuarioDTO> listarTodos(){
        return repo.findAll().stream()
                .map(u -> new UsuarioDTO(u.getId(), u.getNombre(), u.getEmail()))
                .toList();
    }

    public void eliminar(Long id){
        if (!repo.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con el Id: " +  id);
        }
        repo.deleteById(id);
    }

}
