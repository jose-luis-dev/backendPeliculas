package com.peliculaslatino.proyectoDemo.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {

    private final Algorithm algorithm;
    private final String issuer;
    private final long expirationSeconds;

    public TokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.expiration-seconds}") long expirationSeconds
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.issuer = issuer;
        this.expirationSeconds = expirationSeconds;
    }

    public String generarToken(String subject) {
        try {
            Instant ahora = Instant.now();
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(subject)
                    .withExpiresAt(Date.from(ahora))
                    .withExpiresAt(Date.from(ahora.plusSeconds(expirationSeconds)))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error al generar el token JWT ", exception);
        }

    }

    public String validarYObetenerSubject(String token){
        try {
            return JWT.require(algorithm).withIssuer(issuer).build()
                    .verify(token).getSubject();
        }catch (JWTVerificationException e) {
            return null;
        }
    }
}
