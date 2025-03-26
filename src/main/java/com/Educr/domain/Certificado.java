package com.Educr.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false) // Mapea la columna usuario_id
    private Usuario usuario; // Relación con Usuario

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false) // Mapea la columna curso_id
    private Curso curso; // Relación con Curso

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();
}