package com.Educr.service.impl;

import com.Educr.dao.EjerciciosDao;
import com.Educr.domain.Ejercicios;
import com.Educr.service.EjerciciosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EjerciciosServiceImpl implements EjerciciosService {

    private final EjerciciosDao ejerciciosDao;

    @Autowired
    public EjerciciosServiceImpl(EjerciciosDao ejerciciosDao) {
        this.ejerciciosDao = ejerciciosDao;
    }

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
        if (ejercicio.getOpciones() == null || ejercicio.getOpciones().size() < 2) {
            throw new IllegalArgumentException("Debe proporcionar al menos 2 opciones de respuesta");
        }
        
        if (!ejercicio.getOpciones().contains(ejercicio.getRespuestaCorrecta())) {
            throw new IllegalArgumentException("La respuesta correcta debe estar entre las opciones proporcionadas");
        }
        
        return ejerciciosDao.save(ejercicio);
    }

    @Override
    public Ejercicios actualizarEjercicio(Ejercicios ejercicio) {
        return crearEjercicio(ejercicio);
    }

    @Override
    public void eliminarEjercicio(Integer id) {
        if (!ejerciciosDao.existsById(id)) {
            throw new IllegalArgumentException("Ejercicio no encontrado con ID: " + id);
        }
        
        ejerciciosDao.deleteById(id);
    }

    @Override
    public Optional<Ejercicios> obtenerEjercicio(Integer idEjercicio) {
        return ejerciciosDao.findById(idEjercicio);
    }
}