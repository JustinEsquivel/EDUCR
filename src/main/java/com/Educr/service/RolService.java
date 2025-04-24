package com.Educr.service;

import com.Educr.domain.RolEntity;
import com.Educr.domain.Rol;
import java.util.List;
import java.util.Optional;

public interface RolService {
    
    List<RolEntity> listarRoles();
    Optional<RolEntity> obtenerRolPorId(Integer id);
    RolEntity guardarRol(RolEntity rol);
    RolEntity actualizarRol(RolEntity rol);
    void eliminarRol(Integer id);
    
    Optional<RolEntity> buscarRolPorNombre(String nombre);
    boolean existeRolPorNombre(String nombre);
    RolEntity obtenerRolDesdeEnum(Rol rol);
    void inicializarRolesBasicos();
}