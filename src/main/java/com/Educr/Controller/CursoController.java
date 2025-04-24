package com.Educr.controller;

import com.Educr.domain.*;
import com.Educr.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private static final String REDIRECT_CURSOS = "redirect:/cursos";
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String FORMULARIO_CURSO = "cursos/formulario";
    private static final String ATTR_CURSO = "curso";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_SUCCESS = "success";

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
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return REDIRECT_LOGIN;
        }

        model.addAttribute("cursos", cursoService.listarTodosLosCursos());
        model.addAttribute("usuario", usuario); 
        return "cursos/lista";
    }

    @GetMapping("/{id}")
    public String verDetalleCurso(@PathVariable Integer id, Model model, HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return REDIRECT_LOGIN;
        }

        return cursoService.buscarCursoPorId(id)
                .map(curso -> {
                    model.addAttribute(ATTR_CURSO, curso);
                    model.addAttribute("estaInscrito", 
                        inscripcionesService.existeInscripcionPorUsuarioYCurso(usuario.getIdUsuario(), id));
                    return "cursos/detalle";
                })
                .orElse(REDIRECT_CURSOS);
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, HttpSession session) {
        if (obtenerUsuarioSesion(session) == null) {
            return REDIRECT_LOGIN;
        }

        return cursoService.buscarCursoPorId(id)
                .map(curso -> {
                    model.addAttribute(ATTR_CURSO, curso);
                    return FORMULARIO_CURSO;
                })
                .orElse(REDIRECT_CURSOS);
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model, HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return REDIRECT_LOGIN;
        }

        Curso curso = new Curso();
        curso.setCreadoPor(usuario);
        model.addAttribute(ATTR_CURSO, curso);
        return FORMULARIO_CURSO;
    }

    @PostMapping("/guardar")
    public String guardarCurso(@ModelAttribute(ATTR_CURSO) @Valid Curso curso,
                             BindingResult result,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return REDIRECT_LOGIN;
        }

        if (result.hasErrors()) {
            return FORMULARIO_CURSO;
        }

        if (curso.getIdCurso() == null && cursoService.existeCursoPorNombre(curso.getNombre())) {
            result.rejectValue("nombre", "error.curso", "Ya existe un curso con este nombre");
            return FORMULARIO_CURSO;
        }

        curso.setCreadoPor(usuario);

        try {
            cursoService.crearCurso(curso);
            redirectAttributes.addFlashAttribute(ATTR_SUCCESS, "Curso guardado exitosamente");
            return REDIRECT_CURSOS;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, "Error al guardar el curso: " + e.getMessage());
            return FORMULARIO_CURSO;
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCurso(@PathVariable Integer id,
                                @ModelAttribute(ATTR_CURSO) @Valid Curso cursoActualizado,
                                BindingResult result,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (obtenerUsuarioSesion(session) == null) {
            return REDIRECT_LOGIN;
        }

        if (result.hasErrors()) {
            return FORMULARIO_CURSO;
        }

        return cursoService.buscarCursoPorId(id)
                .map(cursoExistente -> {
                    cursoExistente.setNombre(cursoActualizado.getNombre());
                    cursoExistente.setDescripcion(cursoActualizado.getDescripcion());
                    
                    try {
                        cursoService.actualizarCurso(cursoExistente);
                        redirectAttributes.addFlashAttribute(ATTR_SUCCESS, "Curso actualizado exitosamente");
                        return "redirect:/cursos/" + id;
                    } catch (Exception e) {
                        redirectAttributes.addFlashAttribute(ATTR_ERROR, "Error al actualizar el curso: " + e.getMessage());
                        return FORMULARIO_CURSO;
                    }
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute(ATTR_ERROR, "Curso no encontrado");
                    return REDIRECT_CURSOS;
                });
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCurso(@PathVariable Integer id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        try {
            cursoService.eliminarCurso(id);
            redirectAttributes.addFlashAttribute("success", "Curso eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el curso: " + e.getMessage());
        }

        return "redirect:/cursos";
    }

    @PostMapping("/inscribir/{cursoId}")
    public String inscribirEnCurso(@PathVariable Integer cursoId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute(ATTR_ERROR, "Debes iniciar sesión para inscribirte");
            return REDIRECT_LOGIN;
        }

        return cursoService.buscarCursoPorId(cursoId)
                .map(curso -> {
                    if (inscripcionesService.existeInscripcionPorUsuarioYCurso(usuario.getIdUsuario(), cursoId)) {
                        redirectAttributes.addFlashAttribute(ATTR_ERROR, "Ya estás inscrito en este curso");
                        return "redirect:/cursos/" + cursoId;
                    }

                    Inscripciones nuevaInscripcion = new Inscripciones();
                    nuevaInscripcion.setUsuario(usuario);
                    nuevaInscripcion.setCurso(curso);

                    try {
                        inscripcionesService.crearInscripcion(nuevaInscripcion);
                        redirectAttributes.addFlashAttribute(ATTR_SUCCESS, 
                            "¡Inscripción exitosa al curso " + curso.getNombre() + "!");
                        return "redirect:/perfil";
                    } catch (Exception e) {
                        redirectAttributes.addFlashAttribute(ATTR_ERROR, 
                            "Error al inscribirse: " + e.getMessage());
                        return "redirect:/cursos/" + cursoId;
                    }
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute(ATTR_ERROR, "Curso no encontrado");
                    return REDIRECT_CURSOS;
                });
    }

    @PostMapping("/cancelar/{cursoId}")
    public String cancelarInscripcion(@PathVariable Integer cursoId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return REDIRECT_LOGIN;
        }

        inscripcionesService.obtenerInscripcionesPorUsuario(usuario.getIdUsuario()).stream()
            .filter(i -> i.getCurso().getIdCurso().equals(cursoId))
            .findFirst()
            .ifPresentOrElse(
                inscripcion -> {
                    try {
                        inscripcionesService.eliminarInscripcion(inscripcion.getId());
                        redirectAttributes.addFlashAttribute(ATTR_SUCCESS, "Inscripción cancelada");
                    } catch (Exception e) {
                        redirectAttributes.addFlashAttribute(ATTR_ERROR, "Error al cancelar inscripción");
                    }
                },
                () -> redirectAttributes.addFlashAttribute(ATTR_ERROR, "No estás inscrito en este curso")
            );

        return "redirect:/perfil";
    }

    @GetMapping("/{id}/ejercicios")
    public String verEjerciciosCurso(@PathVariable Integer id, HttpSession session) {
        if (obtenerUsuarioSesion(session) == null) {
            return REDIRECT_LOGIN;
        }

        return cursoService.buscarCursoPorId(id)
                .map(curso -> "redirect:/ejercicios/curso/" + id)
                .orElse(REDIRECT_CURSOS);
    }

    private Usuario obtenerUsuarioSesion(HttpSession session) {
        return (Usuario) session.getAttribute("usuario");
    }
}