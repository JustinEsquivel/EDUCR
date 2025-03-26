package com.Educr.service;

import com.Educr.domain.RolEntity;
import com.Educr.domain.Rol;
import java.util.List;
import java.util.Optional;

public interface RolService {
    
    // Operaciones CRUD básicas
    List<RolEntity> listarRoles();
    Optional<RolEntity> obtenerRolPorId(Integer id);
    RolEntity guardarRol(RolEntity rol);
    RolEntity actualizarRol(RolEntity rol);
    void eliminarRol(Integer id);
    
    // Operaciones específicas
    Optional<RolEntity> buscarRolPorNombre(String nombre);
    boolean existeRolPorNombre(String nombre);
    RolEntity obtenerRolDesdeEnum(Rol rol);
    void inicializarRolesBasicos();
}