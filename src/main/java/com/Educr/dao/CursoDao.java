package com.Educr.dao;

import com.Educr.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CursoDao extends JpaRepository<Curso, Integer> {  
    Optional<Curso> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}