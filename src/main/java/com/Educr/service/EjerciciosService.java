package com.Educr.service;

import com.Educr.domain.Ejercicios;
import java.util.List;
import java.util.Optional;

public interface EjerciciosService {
    List<Ejercicios> listarTodosLosEjercicios();
    Optional<Ejercicios> buscarEjercicioPorId(Integer id);
    List<Ejercicios> buscarEjerciciosPorCurso(Integer idCurso);
    Ejercicios crearEjercicio(Ejercicios ejercicio);
    Ejercicios actualizarEjercicio(Ejercicios ejercicio);
    void eliminarEjercicio(Integer id);
    public Optional<Ejercicios> obtenerEjercicio(Integer idEjercicio);

}