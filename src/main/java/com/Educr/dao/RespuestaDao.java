package com.Educr.dao;

import com.Educr.domain.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RespuestaDao extends JpaRepository<Respuesta, Long> {
    List<Respuesta> findByEjercicio_IdEjercicio(Long idEjercicio); // Usamos _ para navegar por la relación
}
