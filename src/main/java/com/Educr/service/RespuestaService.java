package com.Educr.service;

import com.Educr.domain.Respuesta;
import java.util.List;

public interface RespuestaService {
    List<Respuesta> buscarPorEjercicio(Long idEjercicio); 
    Respuesta guardarRespuesta(Respuesta respuesta); 
}
