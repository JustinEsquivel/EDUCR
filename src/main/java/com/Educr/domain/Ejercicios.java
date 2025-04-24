package com.Educr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "ejercicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ejercicios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio")
    private Integer idEjercicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @NotBlank(message = "La pregunta no puede estar vacía")
    @Size(max = 500, message = "La pregunta no puede exceder los 500 caracteres")
    @Column(nullable = false, length = 500)
    private String pregunta;

    @NotBlank(message = "Debe especificar una respuesta correcta")
    @Column(name = "respuesta_correcta", nullable = false)
    private String respuestaCorrecta;
    
    @ElementCollection
    @CollectionTable(name = "opciones_respuesta", joinColumns = @JoinColumn(name = "id_ejercicio"))
    @Column(name = "opcion")
    private List<String> opciones;
 
    
    @OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Respuesta> respuestas;

    // Método para agregar opción
    public void agregarOpcion(String opcion) {
        this.opciones.add(opcion);
    }

    // Método para eliminar opción
    public void eliminarOpcion(String opcion) {
        this.opciones.remove(opcion);
    }
}