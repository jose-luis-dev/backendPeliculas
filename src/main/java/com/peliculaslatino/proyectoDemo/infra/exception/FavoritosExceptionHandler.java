package com.peliculaslatino.proyectoDemo.infra.exception;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
@RestControllerAdvice
public class FavoritosExceptionHandler {

    public static class FavoritoDuplicadoException extends RuntimeException {
        public FavoritoDuplicadoException(String mensaje){
            super(mensaje);
        }
    }

    public static class FavoritoNoEncontradoException extends RuntimeException {
        public FavoritoNoEncontradoException(String mensaje){
            super(mensaje);
        }
    }

    public static class SinFavoritosException extends RuntimeException {
        public SinFavoritosException(String mensaje){
            super(mensaje);
        }
    }

}
