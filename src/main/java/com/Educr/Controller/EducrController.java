package com.Educr.controller;


import com.Educr.domain.*;
import com.Educr.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;       
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.servlet.mvc.support.RedirectAttributes; 
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
public class EducrController {
    
    @Autowired
    private InscripcionesService inscripcionesService;
    @Autowired
    private CursoService cursoService;
    @Autowired
    private UsuarioService usuarioService;
    @GetMapping("/")
    public String mostrarInicio() {
        return "index";
    }

    @GetMapping("/acceso")
    public String mostrarAcceso(HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            return "redirect:/dashboard";
        }
        return "acceso";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model, HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("rolesDisponibles", Rol.values());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String correo,
            @RequestParam String contraseña,
            @RequestParam String rolSeleccionado,
            RedirectAttributes redirectAttributes) {

        try {
            if (usuarioService.existeUsuarioPorCorreo(correo)) {
                redirectAttributes.addFlashAttribute("error", "El correo ya está registrado");
                return "redirect:/registro";
            }

            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setCorreo(correo);
            usuario.setContraseña(contraseña);

            Rol rol = Rol.valueOf(rolSeleccionado);
            usuarioService.crearUsuario(usuario, rol);

            redirectAttributes.addFlashAttribute("exito", "Registro exitoso. Por favor inicia sesión.");
            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Rol seleccionado no válido");
            return "redirect:/registro";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error en el registro: " + e.getMessage());
            return "redirect:/registro";
        }
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model, HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String correo,
            @RequestParam String contraseña,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorCorreo(correo);
            
            if (usuarioOpt.isPresent() && usuarioOpt.get().getContraseña().equals(contraseña)) {
                session.setAttribute("usuario", usuarioOpt.get());
                // Establecer tiempo de expiración de sesión (30 minutos)
                session.setMaxInactiveInterval(30 * 60);
                return "redirect:/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Correo o contraseña incorrectos");
                return "redirect:/login";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al iniciar sesión: " + e.getMessage());
            return "redirect:/login";
        }
    }
    
    @GetMapping("/logout")
        public String logout(HttpSession session) {
            session.invalidate();
            return "redirect:/login";  
        }

    @GetMapping("/dashboard")
    public String mostrarDashboard(HttpSession session, Model model) 
    {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        List<Curso> cursosDestacados = cursoService.listarTodosLosCursos().stream()
            .limit(3)
            .collect(Collectors.toList());

        List<Inscripciones> inscripciones = inscripcionesService
            .obtenerInscripcionesPorUsuario(usuario.getIdUsuario());

        int ejerciciosCompletados = 2; 

        model.addAttribute("usuario", usuario);
        model.addAttribute("cursosDestacados", cursosDestacados);
        model.addAttribute("inscripciones", inscripciones);
        model.addAttribute("ejerciciosCompletados", ejerciciosCompletados);

        return "dashboard";
    }
    
}