package com.Educr.controller;

import com.Educr.domain.Curso;
import com.Educr.domain.Ejercicios;
import com.Educr.domain.Respuesta;
import com.Educr.service.CursoService;
import com.Educr.service.EjerciciosService;
import com.Educr.service.RespuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import java.security.Principal;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;
import com.Educr.domain.Usuario;


@Controller
@RequestMapping("/ejercicios")
public class EjerciciosController {

    private final EjerciciosService ejerciciosService;
    private final CursoService cursoService;
    private final RespuestaService respuestaService;

    @Autowired
    public EjerciciosController(EjerciciosService ejerciciosService, 
                              CursoService cursoService,
                              RespuestaService respuestaService) {
        this.ejerciciosService = ejerciciosService;
        this.cursoService = cursoService;
        this.respuestaService = respuestaService;
    }


    @GetMapping("/curso/{cursoId}")
    public String verEjercicios(@PathVariable Integer cursoId, Model model, Principal principal) {
        Optional<Curso> cursoOpt = cursoService.obtenerCurso(cursoId);
        if (cursoOpt.isEmpty()) {
            return "redirect:/cursos";
        }

        Curso curso = cursoOpt.get();
        List<Ejercicios> ejercicios = ejerciciosService.buscarEjerciciosPorCurso(cursoId);

        // Obtener el rol del usuario actual
        String rol = principal != null ? ((Usuario) principal).getRol().getNombre() : "";

        model.addAttribute("curso", curso);
        model.addAttribute("ejercicios", ejercicios);
        model.addAttribute("rol", rol);  // Agregar el rol al modelo

        return "ejercicios/lista";
    }


   @GetMapping("/nuevo/{cursoId}")
    public String nuevoEjercicio(@PathVariable Integer cursoId, Model model) {
        Optional<Curso> cursoOpt = cursoService.obtenerCurso(cursoId);
        if (cursoOpt.isEmpty()) {
            return "redirect:/cursos";
        }

        Ejercicios ejercicio = new Ejercicios();
        ejercicio.setCurso(cursoOpt.get());

        ejercicio.setOpciones(new ArrayList<>());
        ejercicio.getOpciones().add("");

        model.addAttribute("curso", cursoOpt.get());
        model.addAttribute("ejercicio", ejercicio);

        return "ejercicios/formulario";
    }

    @PostMapping("/guardar/{cursoId}")
    public String guardarEjercicio(@PathVariable Integer cursoId,
                                 @ModelAttribute("ejercicio") @Valid Ejercicios ejercicio,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        Optional<Curso> cursoOpt = cursoService.obtenerCurso(cursoId);
        if (cursoOpt.isEmpty()) {
            return "redirect:/cursos";
        }

        if (ejercicio.getOpciones() == null || ejercicio.getOpciones().size() < 2) {
            result.rejectValue("opciones", "error.ejercicio", "Debe haber al menos 2 opciones");
        }

        if (result.hasErrors()) {
            model.addAttribute("curso", cursoOpt.get());
            return "ejercicios/formulario";
        }

        ejercicio.setCurso(cursoOpt.get());
        ejerciciosService.crearEjercicio(ejercicio);

        redirectAttributes.addFlashAttribute("success", "Ejercicio guardado exitosamente");
        return "redirect:/ejercicios/curso/" + cursoId;
    }

    @GetMapping("/editar/{id}")
    public String editarEjercicio(@PathVariable Integer id, Model model) {
        Optional<Ejercicios> ejercicioOpt = ejerciciosService.obtenerEjercicio(id);
        if (ejercicioOpt.isEmpty()) {
            return "redirect:/ejercicios/curso";
        }
        
        Ejercicios ejercicio = ejercicioOpt.get();
        model.addAttribute("ejercicio", ejercicio);
        model.addAttribute("curso", ejercicio.getCurso());
        
        return "ejercicios/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarEjercicio(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Optional<Ejercicios> ejercicioOpt = ejerciciosService.obtenerEjercicio(id);
        if (ejercicioOpt.isEmpty()) {
            return "redirect:/ejercicios/curso";
        }
        
        Ejercicios ejercicio = ejercicioOpt.get();
        ejerciciosService.eliminarEjercicio(id);
        
        redirectAttributes.addFlashAttribute("success", "Ejercicio eliminado exitosamente");
        return "redirect:/ejercicios/curso/" + ejercicio.getCurso().getIdCurso();
    }

    @GetMapping("/resolver/{cursoId}")
    public String resolverEjercicios(@PathVariable Integer cursoId, Model model) {
        Optional<Curso> cursoOpt = cursoService.obtenerCurso(cursoId);
        if (cursoOpt.isEmpty()) {
            return "redirect:/cursos";
        }
        
        List<Ejercicios> ejercicios = ejerciciosService.buscarEjerciciosPorCurso(cursoId);
        if (ejercicios.isEmpty()) {
            model.addAttribute("message", "Este curso no tiene ejercicios disponibles");
        }
        
        model.addAttribute("curso", cursoOpt.get());
        model.addAttribute("ejercicios", ejercicios);
        
        return "usuario/formulario_usuario";
    }

    @PostMapping("/resolver/{cursoId}")
    public String procesarRespuestas(@PathVariable Integer cursoId,
                                    @RequestParam MultiValueMap<String, String> respuestas,
                                    Principal principal,
                                    Model model) {
        List<Ejercicios> ejercicios = ejerciciosService.buscarEjerciciosPorCurso(cursoId);
        int correctas = 0;
        int total = ejercicios.size();

        for (Ejercicios ejercicio : ejercicios) {
            String id = String.valueOf(ejercicio.getIdEjercicio());
            String respuestaUsuario = respuestas.getFirst("respuesta_" + id);
            
            if (respuestaUsuario != null && respuestaUsuario.equalsIgnoreCase(ejercicio.getRespuestaCorrecta())) {
                correctas++;
            }
            
            Respuesta respuesta = new Respuesta();
            respuesta.setEjercicio(ejercicio);
            respuesta.setTexto(respuestaUsuario);
            respuesta.setEsCorrecta(respuestaUsuario != null && 
                                 respuestaUsuario.equalsIgnoreCase(ejercicio.getRespuestaCorrecta()));
            respuestaService.guardarRespuesta(respuesta);
        }

        model.addAttribute("total", total);
        model.addAttribute("correctas", correctas);
        model.addAttribute("porcentaje", (correctas * 100) / total);
        model.addAttribute("cursoId", cursoId);
        
        return "usuario/resultados";
    }
    @GetMapping("/responder/{id}")
public String responderEjercicio(@PathVariable("id") Integer id, 
                               Model model,
                               Principal principal) {
    Optional<Ejercicios> ejercicioOpt = ejerciciosService.obtenerEjercicio(id);
    if (ejercicioOpt.isEmpty()) {
        return "redirect:/cursos";
    }

    Ejercicios ejercicio = ejercicioOpt.get();

    if (ejercicio.getOpciones() == null || ejercicio.getOpciones().isEmpty()) {
        ejercicio.setOpciones(List.of(
            ejercicio.getRespuestaCorrecta(),
            "Opción 2",
            "Opción 3"
        ));
    }

    model.addAttribute("ejercicio", ejercicio);
    return "ejercicios/responder";
    }

    @PostMapping("/responder/verificar")
    public String verificarRespuesta(@RequestParam("id") Integer id,
                                     @RequestParam("respuesta") String respuesta,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        Optional<Ejercicios> ejercicioOpt = ejerciciosService.obtenerEjercicio(id);
        if (ejercicioOpt.isEmpty()) {
            return "redirect:/cursos";
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        Ejercicios ejercicio = ejercicioOpt.get();
        boolean correcta = ejercicio.getRespuestaCorrecta().equalsIgnoreCase(respuesta);

        Respuesta respuestaUsuario = new Respuesta();
        respuestaUsuario.setEjercicio(ejercicio);
        respuestaUsuario.setTexto(respuesta);
        respuestaUsuario.setEsCorrecta(correcta);
        respuestaUsuario.setUsuario(usuario);

        respuestaService.guardarRespuesta(respuestaUsuario);

        redirectAttributes.addFlashAttribute("ejercicio", ejercicio);
        redirectAttributes.addFlashAttribute("respuestaUsuario", respuesta);
        redirectAttributes.addFlashAttribute("esCorrecta", correcta);

        return "redirect:/ejercicios/resultado/" + id;
    }


    @GetMapping("/resultado/{id}")
    public String mostrarResultado(@PathVariable Integer id, Model model) {
        if (!model.containsAttribute("ejercicio")) {
            Optional<Ejercicios> ejercicioOpt = ejerciciosService.obtenerEjercicio(id);
            if (ejercicioOpt.isEmpty()) {
                return "redirect:/cursos";
            }
            model.addAttribute("ejercicio", ejercicioOpt.get());
        }
        return "ejercicios/resultado";
    }
}