package com.Educr.dao;

import com.Educr.domain.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RolDao extends JpaRepository<RolEntity, Integer> {
    
    // Buscar rol por nombre exacto
    Optional<RolEntity> findByNombre(String nombre);
    
    // Verificar existencia por nombre
    boolean existsByNombre(String nombre);
    
    // Buscar rol ignorando mayúsculas/minúsculas
    Optional<RolEntity> findByNombreIgnoreCase(String nombre);
}