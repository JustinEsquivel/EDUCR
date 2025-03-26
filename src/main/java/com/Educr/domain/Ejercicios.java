package com.Educr.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ejercicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ejercicios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio")
    private Integer idEjercicio;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @Column(nullable = false)
    private String pregunta;

    @Column(name = "respuesta_correcta", nullable = false)
    private String respuestaCorrecta;
}