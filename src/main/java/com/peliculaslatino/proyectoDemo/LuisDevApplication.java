package com.peliculaslatino.proyectoDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LuisDevApplication{
	public static void main(String[] args) {
		SpringApplication.run(LuisDevApplication.class, args);
	}
}
