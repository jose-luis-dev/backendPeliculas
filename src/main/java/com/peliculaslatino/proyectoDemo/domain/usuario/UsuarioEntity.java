package com.peliculaslatino.proyectoDemo.domain.usuario;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.USER;

    // getters/setters
    public Long getId() { return  id; }
    public void setId(Long id) {this.id = id;}

    public String getNombre() {return  nombre;}
    public void  setNombre(String nombre) {this.nombre = nombre;}

    public String getEmail() {return email;}
    public void  setEmail(String email) {this.email = email;}

    public void setPassword(String password) {this.password = password;}

    public Rol getRol() { return rol;}
    public void setRol(Rol rol) { this.rol = rol;}

    // UserDetails
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + rol.name());
    }
    @Override public String getPassword() {return password;}
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public enum Rol { USER, ADMIN }
}
