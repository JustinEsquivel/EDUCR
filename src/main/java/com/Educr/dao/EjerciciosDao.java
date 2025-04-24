package com.Educr.dao;

import com.Educr.domain.Ejercicios;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EjerciciosDao extends JpaRepository<Ejercicios, Integer> {
    List<Ejercicios> findByCurso_IdCurso(Integer idCurso);
}