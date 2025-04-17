package com.Educr.service.impl;

import com.Educr.dao.UsuarioDao;
import com.Educr.domain.Usuario;
import com.Educr.domain.Rol;
import com.Educr.domain.RolEntity; 
import com.Educr.service.UsuarioService;
import com.Educr.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioDao usuarioDao;
    
    @Autowired
    private RolService rolService;

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioDao.findAll();
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Integer id) {
        return usuarioDao.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorCorreo(String correo) {
        return usuarioDao.findByCorreo(correo);
    }

    @Override
    public boolean existeUsuarioPorCorreo(String correo) {
        return usuarioDao.existsByCorreo(correo);
    }

    @Override
    public Usuario crearUsuario(Usuario usuario, Rol rol) {
        RolEntity rolEntity = rolService.obtenerRolDesdeEnum(rol);
        usuario.setRol(rolEntity);
        return usuarioDao.save(usuario);
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        return usuarioDao.save(usuario);
    }

    @Override
    public void eliminarUsuario(Integer id) {
        usuarioDao.deleteById(id);
    }
}