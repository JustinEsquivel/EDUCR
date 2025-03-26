package com.Educr.controller;

import java.util.Optional;
import jakarta.servlet.http.HttpSession;
import com.Educr.domain.Rol;
import com.Educr.domain.Usuario;
import com.Educr.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EducrController {

    @Autowired
    private UsuarioService usuarioService;

    // Página de inicio (landing page)
    @GetMapping("/")
    public String mostrarInicio() {
        return "index";
    }

    // Página de acceso (login/registro)
    @GetMapping("/acceso")
    public String mostrarAcceso(HttpSession session) {
        // Si ya está logueado, redirigir al dashboard
        if (session.getAttribute("usuario") != null) {
            return "redirect:/dashboard";
        }
        return "acceso";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model, HttpSession session) {
        // Si ya está logueado, redirigir al dashboard
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
            
            // En un sistema real, aquí deberías hashear la contraseña
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
        // Si ya está logueado, redirigir al dashboard
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
                // Guardar usuario en sesión
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
        return "redirect:/";
    }

    // Página principal después de login
    @GetMapping("/dashboard")
    public String mostrarDashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        return "dashboard";
    }
}