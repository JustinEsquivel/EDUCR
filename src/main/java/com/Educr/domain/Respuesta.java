
package com.Educr.domain;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "respuestas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Respuesta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta")
    private Integer idRespuesta;

    @ManyToOne
    @JoinColumn(name = "id_ejercicio", nullable = false)
    private Ejercicios ejercicio;

    @Column(nullable = false)
    private String texto;

    @Column(name = "es_correcta", nullable = false)
    private boolean esCorrecta;
}
