# backendPeliculas

Backend en Spring Boot para la búsqueda de peliculas usando la API de TMDB, gestión de usuarios y favoritos.

Objetivo del proyecto: 
Realizar búsqueda y gestión de películas, con autenticación de usuarios y favoritos. Proporciona endpoints REST documentados con **Swagger/OpenAPI** para facilitar su consumo por un frontend.

----------------------------------------------------

## 🔹 Tecnologías

- Java 24  
- Spring Boot 3.5.4  
- PostgreSQL 17.5  
- Hibernate / JPA  
- Flyway para migraciones de base de datos  
- Spring Security con JWT  
- Springdoc/OpenAPI para documentación  
- Maven Wrapper (`mvnw`) para compilación y empaquetado  

----------------------------------------------------

## 🔹 Estructura del proyecto

backendPeliculas/
- ├─ src/ # Código fuente del proyecto
- ├─ .mvn/wrapper/ # Maven Wrapper
- ├─ pom.xml # Archivo de construcción Maven
- ├─ mvnw, mvnw.cmd # Scripts para ejecutar Maven sin instalarlo globalmente
- ├─ .gitignore
- ├─ LICENSE
- └─ README.md

----------------------------------------------------

## 🔹 Configuración

### Variables de entorno

El proyecto utiliza un archivo `env.properties` para almacenar datos sensibles:

properties
DB_HOST=localhost
DB_PELICULAS=peliculas_db
DB_USER=postgres
DB_PASSWORD=tu_contraseña
JWT_SECRET1=tu_clave_jwt
TMDB_API_TOKEN=tu_token_tmdb

"Este archivo no se sube a GitHub por seguridad."

### Configuración de application.yml

- application.yml: Contiene la configuración común para dev y test.
- Los perfiles dev y prod se pueden crear para ajustar el puerto, base de datos o logging según el entorno.

----------------------------------------------------

## 🔹 Ejecución 

  1. Clonar el repositorio:
      git clone https://github.com/jose-luis-dev/backendPeliculas.git
      cd backendPeliculas

  2. Compilar y empaquetar sin correr test:
      ./mvnw clean package -DskipTests

  3. Ejecutar el JAR generado:
      java -jar target/backendPeliculas-SNAPSHOT.jar
  
  4. El backend quedará corriendo en http://localhost:8080

----------------------------------------------------
     
## 🔹 Documentación de la API

- Swagger UI está disponible en:
  http://localhost:8080/swagger-ui.html

Allí podrás consultar todos los endpoints disponibles, sus métodos HTTP, parámetros y respuestas.

----------------------------------------------------
     
## 🔹 Endpoints principales

- Usuarios: login, registro
- Favoritos: agregar, eliminar, listar peliculas favoritas
- Búsqueda de peliculas: Usando la API de TMDB

| Todos los endpoints están protegidos por JWT donde aplica.


----------------------------------------------------
     
## 🔹 Base de datos

- Se utiliza Flyway para migraciones.
- Al iniciar, se crea un usuario admin si no existe.
- Configuración ddl-auto:
  - Dev/Test: update (puede crear tablas).
  - Prod: validate (solo valida existencia de tablas).


----------------------------------------------------
     
## 🔹 Notas importantes

- Este backend puede ser consumido por cualquier frontend que implemente los endpoints documentados.
- Para seguridad, no subas tu env.properties con credenciales a repositorios publicos.
- El proyecto usa Maven Wrapper, por lo que no necesitas tener Maven instalado globalmente.
  



