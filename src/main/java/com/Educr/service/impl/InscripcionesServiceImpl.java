package com.Educr.service.impl;

import com.Educr.dao.InscripcionesDao;
import com.Educr.domain.Inscripciones;
import com.Educr.service.InscripcionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InscripcionesServiceImpl implements InscripcionesService {

    @Autowired
    private InscripcionesDao inscripcionesDao;

    @Override
    public List<Inscripciones> listarInscripciones() {
        return inscripcionesDao.findAll();
    }

    @Override
    public Optional<Inscripciones> obtenerInscripcionPorId(Integer id) {
        return inscripcionesDao.findById(id);
    }

    @Override
    public List<Inscripciones> obtenerInscripcionesPorUsuario(Integer usuarioId) {
        return inscripcionesDao.findByUsuario_IdUsuario(usuarioId);
    }

    @Override
    public List<Inscripciones> obtenerInscripcionesPorCurso(Integer idCurso) {
        return inscripcionesDao.findByCurso_IdCurso(idCurso);
    }

    @Override
    public boolean existeInscripcionPorUsuarioYCurso(Integer usuarioId, Integer idCurso) {
        return inscripcionesDao.existsByUsuario_IdUsuarioAndCurso_IdCurso(usuarioId, idCurso);
    }

    @Override
    public Inscripciones crearInscripcion(Inscripciones inscripcion) {
        return inscripcionesDao.save(inscripcion);
    }

    @Override
    public Inscripciones actualizarInscripcion(Inscripciones inscripcion) {
        return inscripcionesDao.save(inscripcion);
    }

    @Override
    public void eliminarInscripcion(Integer id) {
        inscripcionesDao.deleteById(id);
    }
}