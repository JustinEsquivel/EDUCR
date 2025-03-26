package com.Educr.service;

import com.Educr.domain.Inscripciones;
import java.util.List;
import java.util.Optional;

public interface InscripcionesService {
    List<Inscripciones> listarInscripciones();
    Optional<Inscripciones> obtenerInscripcionPorId(Integer id);
    List<Inscripciones> obtenerInscripcionesPorUsuario(Integer usuarioId);
    List<Inscripciones> obtenerInscripcionesPorCurso(Integer idCurso);
    boolean existeInscripcionPorUsuarioYCurso(Integer usuarioId, Integer idCurso);
    Inscripciones crearInscripcion(Inscripciones inscripcion);
    Inscripciones actualizarInscripcion(Inscripciones inscripcion);
    void eliminarInscripcion(Integer id);
}