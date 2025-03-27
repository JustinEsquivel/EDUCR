package com.Educr.controller;

import com.Educr.domain.Usuario;
import com.Educr.domain.Inscripciones;
import com.Educr.service.UsuarioService;
import com.Educr.service.InscripcionesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List; 

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private InscripcionesService inscripcionesService; 

    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        List<Inscripciones> inscripciones = inscripcionesService
            .obtenerInscripcionesPorUsuario(usuario.getIdUsuario()); 

        model.addAttribute("usuario", usuario);
        model.addAttribute("inscripciones", inscripciones);

        return "perfil";
    }
    @GetMapping("/cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();  // Cerrar la sesión
        return "redirect:/";  // Redirigir a la página principal
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
        

        session.setAttribute("usuario", usuario);
        
        redirectAttributes.addFlashAttribute("exito", "Perfil actualizado correctamente");
        return "redirect:/perfil";
    }
}
