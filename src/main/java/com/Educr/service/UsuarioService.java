package com.Educr.service;

import com.Educr.domain.Usuario;
import com.Educr.domain.Rol;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listarUsuarios();
    Optional<Usuario> obtenerUsuarioPorId(Integer id);
    Optional<Usuario> obtenerUsuarioPorCorreo(String correo);
    boolean existeUsuarioPorCorreo(String correo);
    Usuario crearUsuario(Usuario usuario, Rol rol); 
    Usuario actualizarUsuario(Usuario usuario);
    void eliminarUsuario(Integer id);
}