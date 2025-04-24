package com.Educr.service.impl;

import com.Educr.dao.RolDao;
import com.Educr.domain.Rol;
import com.Educr.domain.RolEntity;
import com.Educr.service.RolService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolDao rolDao;

    @Override
    public List<RolEntity> listarRoles() {
        return rolDao.findAll();
    }

    @Override
    public Optional<RolEntity> obtenerRolPorId(Integer id) {
        return rolDao.findById(id);
    }

    @Override
    public RolEntity guardarRol(RolEntity rol) {
        return rolDao.save(rol);
    }

    @Override
    public RolEntity actualizarRol(RolEntity rol) {
        return rolDao.save(rol);
    }

    @Override
    public void eliminarRol(Integer id) {
        rolDao.deleteById(id);
    }

    @Override
    public Optional<RolEntity> buscarRolPorNombre(String nombre) {
        return rolDao.findByNombreIgnoreCase(nombre);
    }

    @Override
    public boolean existeRolPorNombre(String nombre) {
        return rolDao.existsByNombre(nombre);
    }

    @Override
    public RolEntity obtenerRolDesdeEnum(Rol rol) {
        return rolDao.findByNombreIgnoreCase(rol.getNombre())
                   .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rol));
    }

    @PostConstruct
    @Transactional
    public void inicializarRolesBasicos() {
        for (Rol rol : Rol.values()) {
            if (!rolDao.existsByNombre(rol.getNombre())) {
                RolEntity nuevoRol = new RolEntity();
                nuevoRol.setNombre(rol.getNombre());
                rolDao.save(nuevoRol);
            }
        }
    }
}