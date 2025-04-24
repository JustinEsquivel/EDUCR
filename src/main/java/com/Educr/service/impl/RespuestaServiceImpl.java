package com.Educr.service.impl;

import com.Educr.dao.RespuestaDao;
import com.Educr.domain.Respuesta;
import com.Educr.service.RespuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RespuestaServiceImpl implements RespuestaService {
    private final RespuestaDao respuestaDao;

    @Autowired
    public RespuestaServiceImpl(RespuestaDao respuestaDao) {
        this.respuestaDao = respuestaDao;
    }

    @Override
    public Respuesta guardarRespuesta(Respuesta respuesta) {
        return respuestaDao.save(respuesta);
    }

    @Override
    public List<Respuesta> buscarPorEjercicio(Long idEjercicio) {
        return respuestaDao.findByEjercicio_IdEjercicio(idEjercicio);
    }
}