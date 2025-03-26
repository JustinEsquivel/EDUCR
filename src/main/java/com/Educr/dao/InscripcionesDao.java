package com.Educr.dao;

import com.Educr.domain.Inscripciones;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InscripcionesDao extends JpaRepository<Inscripciones, Integer> {
    List<Inscripciones> findByUsuario_IdUsuario(Integer usuarioId);  // Cambiado a findByUsuario_IdUsuario
    List<Inscripciones> findByCurso_IdCurso(Integer idCurso);
    boolean existsByUsuario_IdUsuarioAndCurso_IdCurso(Integer usuarioId, Integer idCurso);  // Cambiado a existsByUsuario_IdUsuario
}