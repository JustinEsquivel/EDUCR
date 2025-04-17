package com.Educr.service.impl;

import com.Educr.dao.CertificadoDao;
import com.Educr.domain.Certificado;
import com.Educr.service.CertificadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CertificadoServiceImpl implements CertificadoService {

    @Autowired
    private CertificadoDao certificadoDao;

    @Override
    public List<Certificado> listarCertificados() {
        return certificadoDao.findAll();
    }

    @Override
    public Optional<Certificado> buscarCertificadoPorId(Integer id) {
        return certificadoDao.findById(id);
    }

    @Override
    public List<Certificado> buscarCertificadosPorUsuario(Integer usuarioId) {
        return certificadoDao.findByUsuario_IdUsuario(usuarioId);
    }

    @Override
    public boolean existeCertificadoPorUsuarioYCurso(Integer usuarioId, String cursoId) {
        return certificadoDao.existsByUsuario_IdUsuarioAndCurso_IdCurso(usuarioId, Integer.parseInt(cursoId)); 
    }

    @Override
    public Certificado guardarCertificado(Certificado certificado) {
        return certificadoDao.save(certificado);
    }

    @Override
    public Certificado actualizarCertificado(Certificado certificado) {
        return certificadoDao.save(certificado);
    }

    @Override
    public void eliminarCertificado(Integer id) {
        certificadoDao.deleteById(id);
    }
}