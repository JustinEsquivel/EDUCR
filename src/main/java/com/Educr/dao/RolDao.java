package com.Educr.dao;

import com.Educr.domain.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RolDao extends JpaRepository<RolEntity, Integer> {
    
    // Buscar rol por nombre exacto
    Optional<RolEntity> findByNombre(String nombre);
    
    // Verifica existencia por nombre
    boolean existsByNombre(String nombre);
    
    Optional<RolEntity> findByNombreIgnoreCase(String nombre);
}