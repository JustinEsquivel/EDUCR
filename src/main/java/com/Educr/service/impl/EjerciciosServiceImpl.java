package com.Educr.service.impl;

import com.Educr.dao.EjerciciosDao;
import com.Educr.domain.Ejercicios;
import com.Educr.service.EjerciciosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EjerciciosServiceImpl implements EjerciciosService {

    @Autowired
    private EjerciciosDao ejerciciosDao;

    @Override
    public List<Ejercicios> listarTodosLosEjercicios() {
        return ejerciciosDao.findAll();
    }

    @Override
    public Optional<Ejercicios> buscarEjercicioPorId(Integer id) {
        return ejerciciosDao.findById(id);
    }

    @Override
    public List<Ejercicios> buscarEjerciciosPorCurso(Integer idCurso) {
        return ejerciciosDao.findByCurso_IdCurso(idCurso);
    }

    @Override
    public Ejercicios crearEjercicio(Ejercicios ejercicio) {
        return ejerciciosDao.save(ejercicio);
    }

    @Override
    public Ejercicios actualizarEjercicio(Ejercicios ejercicio) {
        return ejerciciosDao.save(ejercicio);
    }

    @Override
    public void eliminarEjercicio(Integer id) {
        ejerciciosDao.deleteById(id);
    }
}