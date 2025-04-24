
package com.Educr.domain;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "respuestas")
@Data
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "id_ejercicio", nullable = false)
    private Ejercicios ejercicio;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(nullable = false)
    private String texto;
    
    @Column(name = "es_correcta", nullable = false)
    private boolean esCorrecta;
    
}
