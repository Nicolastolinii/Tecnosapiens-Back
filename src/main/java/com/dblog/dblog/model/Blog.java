package com.dblog.dblog.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "blog" )
public class Blog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "titulo", nullable = false)
    private String titulo;
    @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
    private String contenido;
    @Column(name = "time_data", nullable = false)
    private LocalDateTime timeData;
    @Column(name = "autor_id")
    private Long autorId;

    @Lob
    @Column(name = "imagen", columnDefinition = "MEDIUMBLOB", nullable = false)
    private byte[] imagen;

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Column(name = "categoria", nullable = false)
    private String categoria;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getTimeData() {
        return timeData;
    }

    public void setTimeData(LocalDateTime timeData) {
        this.timeData = timeData;
    }

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }
}
