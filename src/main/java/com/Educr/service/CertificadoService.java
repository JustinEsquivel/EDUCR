package com.Educr.service;

import com.Educr.domain.Certificado;
import java.util.List;
import java.util.Optional;

public interface CertificadoService {
    List<Certificado> listarCertificados();
    Optional<Certificado> buscarCertificadoPorId(Integer id);
    List<Certificado> buscarCertificadosPorUsuario(Integer usuarioId);
    boolean existeCertificadoPorUsuarioYCurso(Integer usuarioId, String cursoId); // Firma correcta
    Certificado guardarCertificado(Certificado certificado);
    Certificado actualizarCertificado(Certificado certificado);
    void eliminarCertificado(Integer id);
}