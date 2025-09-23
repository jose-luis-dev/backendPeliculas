package com.peliculaslatino.proyectoDemo.service;

import com.peliculaslatino.proyectoDemo.domain.favoritos.FavoritoEntity;
import com.peliculaslatino.proyectoDemo.domain.usuario.UsuarioEntity;
import com.peliculaslatino.proyectoDemo.dto.FavoritoDTO;
import com.peliculaslatino.proyectoDemo.dto.FavoritoRequest;
import com.peliculaslatino.proyectoDemo.infra.exception.FavoritosExceptionHandler;
import com.peliculaslatino.proyectoDemo.repository.FavoritoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoritoService {

    private final FavoritoRepository repo;
    private final UsuarioService usuarioService;

    public FavoritoService(FavoritoRepository repo, UsuarioService usuarioService){
        this.repo = repo;
        this.usuarioService = usuarioService;
    }
    

    public FavoritoDTO guardar(UsuarioEntity usuario,  FavoritoRequest req) throws FavoritosExceptionHandler.FavoritoDuplicadoException {
        UsuarioEntity u = usuarioService.getById(usuario.getId());

        //Evitar los duplicados
        if (repo.existsByUsuario_IdAndPeliculaId(usuario.getId(), req.peliculaId())){
            throw new FavoritosExceptionHandler.FavoritoDuplicadoException("La pelicula ya está en favoritos.");
        }

        FavoritoEntity f = new FavoritoEntity();
        f.setUsuario(u);
        f.setPeliculaId(req.peliculaId());
        f.setTitulo(req.titulo());
        f.setPoster(req.poster());
        f.setFecha(req.fecha());
        f.setSinopsis(req.sinopsis());
        f.setEvaluacion(req.evaluacion());

        f = repo.save(f);
        return new FavoritoDTO(
                f.getId(), u.getId(), f.getPeliculaId(), f.getTitulo(),
                f.getPoster(), f.getFecha(), f.getSinopsis(), f.getEvaluacion()
        );
    }

    public List<FavoritoDTO>listaPorUsuario(Long usuarioId) {
        var favoritos = repo.findByUsuario_Id(usuarioId);

        return favoritos.stream()
                .map(f -> new FavoritoDTO(
                        f.getId(),
                        f.getUsuario().getId(),
                        f.getPeliculaId(),
                        f.getTitulo(),
                        f.getPoster(),
                        f.getFecha(),
                        f.getSinopsis(),
                        f.getEvaluacion()
                ))
                .toList();
    }

    @Transactional
    public void eliminar(Long usuarioId, Long peliculaId) {
        if (!repo.existsByUsuario_IdAndPeliculaId(usuarioId, peliculaId)){
            throw new FavoritosExceptionHandler.FavoritoNoEncontradoException("La película no está en favoritos.");
        }
        repo.deleteByUsuario_IdAndPeliculaId(usuarioId, peliculaId);
    }

}
