package com.peliculaslatino.proyectoDemo.dto.auth;

public record AuthResponse(String token, String tokeType, long expiresIn) {
}
