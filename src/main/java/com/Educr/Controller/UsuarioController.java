package com.Educr.controller;

import com.Educr.domain.Usuario;
import com.Educr.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(@RequestParam String nombre,
                                 @RequestParam String apellido,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuarioService.actualizarUsuario(usuario);
        
        // Actualizar el usuario en la sesión
        session.setAttribute("usuario", usuario);
        
        redirectAttributes.addFlashAttribute("exito", "Perfil actualizado correctamente");
        return "redirect:/perfil";
    }
}