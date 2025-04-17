package com.Educr.controller;

import com.Educr.domain.*;
import com.Educr.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/certificados")
public class CertificadoController {

    private final CertificadoService certificadoService;
    private final CursoService cursoService;
    private final InscripcionesService inscripcionesService;

    @Autowired
    public CertificadoController(CertificadoService certificadoService, 
                               CursoService cursoService,
                               InscripcionesService inscripcionesService) {
        this.certificadoService = certificadoService;
        this.cursoService = cursoService;
        this.inscripcionesService = inscripcionesService;
    }

    @GetMapping("/usuario")
    public String listarCertificadosUsuario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        List<Certificado> certificados = certificadoService.buscarCertificadosPorUsuario(usuario.getIdUsuario());
        model.addAttribute("certificados", certificados);
        return "certificados/lista";
    }

    @GetMapping("/generar/{cursoId}")
    public String generarCertificado(@PathVariable Integer cursoId,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        Optional<Curso> cursoOpt = cursoService.buscarCursoPorId(cursoId);
        if (cursoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Curso no encontrado");
            return "redirect:/cursos";
        }

        if (!inscripcionesService.existeInscripcionPorUsuarioYCurso(usuario.getIdUsuario(), cursoId)) {
            redirectAttributes.addFlashAttribute("error", "No estás inscrito en este curso");
            return "redirect:/cursos/" + cursoId;
        }

        if (certificadoService.existeCertificadoPorUsuarioYCurso(usuario.getIdUsuario(), cursoId.toString())) {
            redirectAttributes.addFlashAttribute("error", "Ya tienes un certificado para este curso");
            return "redirect:/cursos/" + cursoId;
        }

        Certificado certificado = new Certificado();
        certificado.setUsuario(usuario);
        certificado.setCurso(cursoOpt.get());
        
        certificadoService.guardarCertificado(certificado);
        
        redirectAttributes.addFlashAttribute("exito", "Certificado generado exitosamente");
        return "redirect:/certificados/usuario";
    }

    @GetMapping("/ver/{id}")
    public String verCertificado(@PathVariable Integer id,
                               HttpSession session,
                               Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        Optional<Certificado> certificadoOpt = certificadoService.buscarCertificadoPorId(id);
        if (certificadoOpt.isEmpty() || 
            !certificadoOpt.get().getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return "redirect:/certificados/usuario";
        }

        model.addAttribute("certificado", certificadoOpt.get());
        return "certificados/ver";
    }
}