package com.dblog.dblog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "blog" )
@AllArgsConstructor
@NoArgsConstructor
public class Blog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "titulo", nullable = false)
    private String titulo;
    @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
    private String contenido;
    @Column(name = "views")
    private Integer view;

    @Column(name = "time_data", nullable = false)
    private LocalDateTime timeData;
    @Column(name = "autor_id")
    private Long autorId;

    @Column(name = "categoria", nullable = false)
    private String categoria;
    @Lob
    @Column(name = "imagen")
    private String imagen;


    @Column(name = "autor_user")
    private String autor;
    @Column(name = "autor_img")
    private String autorImg;

}
