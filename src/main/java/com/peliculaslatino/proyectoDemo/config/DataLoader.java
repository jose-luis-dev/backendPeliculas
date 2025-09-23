package com.peliculaslatino.proyectoDemo.config;

import com.peliculaslatino.proyectoDemo.domain.usuario.UsuarioEntity;
import com.peliculaslatino.proyectoDemo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader  {

    @Bean
    public CommandLineRunner initAdmin(UsuarioRepository usuarioRepository,
                                       PasswordEncoder passwordEncoder){
        return args -> {
            if (usuarioRepository.findByEmail("admin@demo.com").isEmpty()){
                UsuarioEntity admin = new UsuarioEntity();
                admin.setNombre("Administrador");;
                admin.setEmail("admin@demo.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRol(UsuarioEntity.Rol.valueOf("ADMIN"));
                usuarioRepository.save(admin);
                System.out.println("Usuario ADMIN creado: admin@demo.com / admin123");
            }else {
                System.out.println("Usuario ADMIN ya existe");
            }
        };
    }
}
