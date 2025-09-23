--Usuarios
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(200) NOT NULL,
    rol VARCHAR(30) NOT NULL
);


-- Favoritos
CREATE TABLE favoritos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    pelicula_id BIGINT NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    poster VARCHAR(300),
    fecha VARCHAR(20),
    sinopsis TEXT,
    evaluacion DOUBLE PRECISION,
    fecha_guardado TIMESTAMP DEFAULT NOW()
);

-- Índices
CREATE INDEX idx_favoritos_usuario ON favoritos(usuario_id);
CREATE INDEX idx_favoritos_pelicula ON Favoritos(pelicula_id);