# Backend Peliculas API

API REST desarrollada con Spring Boot para la búsqueda de películas usando la API de TMDB, con autenticación de usuarios y gestión de películas favoritas mediante JWT.
Proporciona endpoints REST documentados con **Swagger/OpenAPI** para facilitar su consumo por un frontend.

## Qué demuestra este proyecto
- Desarrollo de APIs REST con Spring Boot
- Integración de APIs externas (TMDB)
- Autenticación y autorización con JWT
- Persistencia de datos con JPA / Hibernate
- Migraciones de base de datos con Flyway
- Documentación de endpoints con Swagger/OpenAPI

## Tecnologías

- Java 24  
- Spring Boot 3.5.4
- Spring Security + JWT  
- PostgreSQL 17.5  
- Hibernate / JPA  
- Flyway
- Springdoc/OpenAPI
- Maven Wrapper (`mvnw`) para compilación y empaquetado  

## Funcionalidades
- Registro y autenticación de usuarios
- Búsqueda de películas usando la API de TMDB
- Gestión de películas favoritas
- Endpoints REST protegidos por JWT

## Arquitectura
- Arquitectura en capas (Controller, Service, Repository)
- Uso de DTOs para entrada y salida de datos
- Separación de responsabilidades

## Documentación de la API

- Swagger UI está disponible en:
  http://localhost:8080/swagger-ui.html

(podrás consultar todos los endpoints disponibles, sus métodos HTTP, parámetros y respuestas.)

## Configuración
El proyecto utiliza un archivo `env.properties` para almacenar datos sensibles:
(no se sube al repositorio por seguridad).

Variables de entorno
DB_HOST
DB_PELICULAS
DB_USER
DB_PASSWORD
JWT_SECRET1
TMDB_API_TOKEN

### Configuración de application.yml
- application.yml: Contiene la configuración común para dev y test.
- Los perfiles dev y prod se pueden crear para ajustar el puerto, base de datos o logging según el entorno.

## Ejecución 

  1. Clonar el repositorio:
      git clone https://github.com/jose-luis-dev/backendPeliculas.git
      cd backendPeliculas

  2. Compilar y empaquetar sin correr test:
      ./mvnw clean package -DskipTests

  3. Ejecutar el JAR generado:
      java -jar target/backendPeliculas-SNAPSHOT.jar

## Notas finales
- Proyecto académico con enfoque en buenas prácticas backend
- Puede ser consumido por cualquier frontend
- No subir archivos con credenciales al repositorio

     
## Endpoints principales
- POST /auth/register
- POST /auth/login
- GET /movies/search
- POST /favorites
- GET /favorites
     
## Base de datos
- PostgreSQL como motor principal
- Migraciones gestionadas con Flyway
- Creación automática de usuario administrador en entorno de desarrollo
- Estrategia ddl-auto:
  - dev/test: update
  - prod: validate

## Estructura del proyecto
backendPeliculas/
├─ controller/
├─ service/
├─ repository/
├─ dto/
├─ config/
└─ security/

