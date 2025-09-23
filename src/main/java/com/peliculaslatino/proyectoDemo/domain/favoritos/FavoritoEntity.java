package com.peliculaslatino.proyectoDemo.domain.favoritos;

import com.peliculaslatino.proyectoDemo.domain.usuario.UsuarioEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "favoritos")
public class FavoritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Relacion con el usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "pelicula_id", nullable = false)
    private Long peliculaId;

    private String titulo;
    private String poster;
    private String fecha;
    @Column(columnDefinition = "TEXT")
    private String sinopsis;
    private Double evaluacion;

    // getters/setters
    public Long getId() { return id;}
    public void setId(Long id) { this.id = id;}

    public UsuarioEntity getUsuario() { return usuario; }
    public void setUsuario(UsuarioEntity usuario) { this.usuario = usuario; }

    public Long getPeliculaId() { return peliculaId; }
    public void setPeliculaId(Long peliculaId) { this.peliculaId = peliculaId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster;}

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha;}

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

    public Double getEvaluacion() { return evaluacion;}
    public void setEvaluacion(Double evaluacion) { this.evaluacion = evaluacion;}
}
