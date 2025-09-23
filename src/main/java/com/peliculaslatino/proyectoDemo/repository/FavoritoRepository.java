package com.peliculaslatino.proyectoDemo.repository;

import com.peliculaslatino.proyectoDemo.domain.favoritos.FavoritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<FavoritoEntity, Long> {
    List<FavoritoEntity> findByUsuario_Id(Long usuarioId);
    boolean existsByUsuario_IdAndPeliculaId(Long usuarioId, Long peliculaId);
    Optional<FavoritoEntity> findByUsuario_IdAndPeliculaId(Long usuarioId, Long peliculaId);
    void deleteByUsuario_IdAndPeliculaId(Long usuarioId, Long peliculaId);
}
