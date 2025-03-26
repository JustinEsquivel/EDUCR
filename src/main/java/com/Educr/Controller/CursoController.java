package com.Educr.controller;

import com.Educr.domain.*;
import com.Educr.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Importación faltante

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;
    private final UsuarioService usuarioService;
    private final InscripcionesService inscripcionesService;
    
    @Autowired
    public CursoController(CursoService cursoService, 
                         UsuarioService usuarioService, 
                         InscripcionesService inscripcionesService) {
        this.cursoService = cursoService;
        this.usuarioService = usuarioService;
        this.inscripcionesService = inscripcionesService;
    }

    @GetMapping
    public String listarCursos(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        List<Curso> cursos = cursoService.listarTodosLosCursos();
        model.addAttribute("cursos", cursos);
        return "cursos/lista";
    }

    @GetMapping("/{id}")
    public String verDetalleCurso(@PathVariable Integer id, 
                                 Model model,
                                 HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        Optional<Curso> cursoOpt = cursoService.buscarCursoPorId(id);
        
        if (cursoOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            model.addAttribute("curso", curso);
            
            boolean estaInscrito = inscripcionesService.existeInscripcionPorUsuarioYCurso(
                usuario.getIdUsuario(), 
                id
            );
            model.addAttribute("estaInscrito", estaInscrito);
            return "cursos/detalle";
        }
        
        return "redirect:/cursos";
    }

    @PostMapping("/inscribir/{cursoId}")
    public String inscribirEnCurso(@PathVariable Integer cursoId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para inscribirte en un curso");
            return "redirect:/login";
        }

        Optional<Curso> cursoOpt = cursoService.buscarCursoPorId(cursoId);

        if (!cursoOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Curso no encontrado");
            return "redirect:/cursos";
        }

        Curso curso = cursoOpt.get();

        if (inscripcionesService.existeInscripcionPorUsuarioYCurso(usuario.getIdUsuario(), curso.getIdCurso())) {
            redirectAttributes.addFlashAttribute("error", "Ya estás inscrito en este curso");
            return "redirect:/cursos/" + cursoId;
        }

        Inscripciones nuevaInscripcion = new Inscripciones();
        nuevaInscripcion.setUsuario(usuario);
        nuevaInscripcion.setCurso(curso);
        
        try {
            inscripcionesService.crearInscripcion(nuevaInscripcion);
            redirectAttributes.addFlashAttribute("exito", 
                "¡Inscripción exitosa al curso " + curso.getNombre() + "!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al inscribirse al curso: " + e.getMessage());
            return "redirect:/cursos/" + cursoId;
        }
        
        return "redirect:/perfil";
    }

    @PostMapping("/cancelar/{cursoId}")
    public String cancelarInscripcion(@PathVariable Integer cursoId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        List<Inscripciones> inscripciones = inscripcionesService
            .obtenerInscripcionesPorUsuario(usuario.getIdUsuario())
            .stream()
            .filter(i -> i.getCurso().getIdCurso().equals(cursoId))
            .toList();
        
        if (!inscripciones.isEmpty()) {
            try {
                inscripcionesService.eliminarInscripcion(inscripciones.get(0).getId());
                redirectAttributes.addFlashAttribute("exito", "Inscripción cancelada correctamente");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Error al cancelar la inscripción");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "No estás inscrito en este curso");
        }
        
        return "redirect:/perfil";
    }
}