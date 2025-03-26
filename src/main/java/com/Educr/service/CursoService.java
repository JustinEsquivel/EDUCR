package com.Educr.service;

import com.Educr.domain.Curso;
import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listarTodosLosCursos();
    Optional<Curso> buscarCursoPorId(Integer id);  // Cambiado de String a Integer
    Optional<Curso> buscarCursoPorNombre(String nombre);
    boolean existeCursoPorNombre(String nombre);
    Curso crearCurso(Curso curso);
    Curso actualizarCurso(Curso curso);
    void eliminarCurso(Integer id);  // Cambiado de String a Integer
}