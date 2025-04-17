package com.Educr.service.impl;

import com.Educr.dao.CursoDao;
import com.Educr.domain.Curso;
import com.Educr.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoDao cursoDao;

    @Override
    public List<Curso> listarTodosLosCursos() {
        return cursoDao.findAll();
    }

    @Override
    public Optional<Curso> buscarCursoPorId(Integer id) {
        return cursoDao.findById(id);
    }

    @Override
    public Optional<Curso> buscarCursoPorNombre(String nombre) {
        return cursoDao.findByNombre(nombre);
    }

    @Override
    public boolean existeCursoPorNombre(String nombre) {
        return cursoDao.existsByNombre(nombre);
    }

    @Override
    public Curso crearCurso(Curso curso) {
        return cursoDao.save(curso);
    }

    @Override
    public Curso actualizarCurso(Curso curso) {
        return cursoDao.save(curso);
    }

    @Override
    public void eliminarCurso(Integer id) {
        cursoDao.deleteById(id);
    }
    @Override
    public Optional<Curso> obtenerCurso(Integer idCurso) {
        return cursoDao.findById(idCurso);
    }
}