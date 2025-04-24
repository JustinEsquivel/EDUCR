package com.Educr.service;

import com.Educr.domain.Respuesta;
import java.util.List;

public interface RespuestaService {
    Respuesta guardarRespuesta(Respuesta respuesta);
    List<Respuesta> buscarPorEjercicio(Long idEjercicio);
}