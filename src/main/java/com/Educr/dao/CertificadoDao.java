package com.Educr.dao;

import com.Educr.domain.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CertificadoDao extends JpaRepository<Certificado, Integer> {
    List<Certificado> findByUsuario_IdUsuario(Integer usuarioId); // Usa idUsuario
    boolean existsByUsuario_IdUsuarioAndCurso_IdCurso(Integer usuarioId, Integer cursoId); // Usa idUsuario
}