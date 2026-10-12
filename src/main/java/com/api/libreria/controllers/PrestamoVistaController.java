package com.api.libreria.controllers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.libreria.models.PrestamoModel;
import com.api.libreria.services.LibroService;
import com.api.libreria.services.PersonaService;
import com.api.libreria.services.PrestamoService;

@Controller
public class PrestamoVistaController {

    private final PrestamoService prestamoService;
    private final LibroService libroService;
    private final PersonaService personaService;

    public PrestamoVistaController(PrestamoService prestamoService, LibroService libroService, PersonaService personaService) {
        this.prestamoService = prestamoService;
        this.libroService = libroService;
        this.personaService = personaService;
    }

    @GetMapping("/vista/prestamos")
    public String mostrarPrestamos(Model model) {
        model.addAttribute("prestamos", prestamoService.obtenerPrestamos());
        model.addAttribute("libros", libroService.obtenerlibros());
        model.addAttribute("personas", personaService.obtenerPersonas().stream()
                .filter(p -> "ACTIVO".equals(p.getEstado()) && "USUARIO".equals(p.getRol()))
                .collect(Collectors.toList()));  
        if (!model.containsAttribute("prestamo")) {
            model.addAttribute("prestamo", new PrestamoModel());
        }
        return "prestamos";
    }

    @PostMapping("/vista/prestamos/guardar")
    public String guardarPrestamo(@ModelAttribute PrestamoModel prestamo, RedirectAttributes atributos) {
        try{
            prestamoService.guardarPrestamo(prestamo);
            atributos.addFlashAttribute("mensaje", "Préstamo registrado correctamente.");
        } catch(IllegalArgumentException | IllegalStateException exception){
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/prestamos";
    }

    @PostMapping("/vista/prestamos/{id}/devolver")
    public String devolverPrestamo(@PathVariable Long id, RedirectAttributes atributos) {
        try {
            prestamoService.marcarDevolucion(id);
            atributos.addFlashAttribute("mensaje", "Préstamo marcado como devuelto.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/prestamos";
    }

    @PostMapping("/vista/prestamos/{id}/eliminar")
    public String eliminarPrestamo(@PathVariable Long id, RedirectAttributes atributos) {
        if (prestamoService.eliminarPrestamo(id)) {
            atributos.addFlashAttribute("mensaje", "Préstamo eliminado correctamente.");
        } else {
            atributos.addFlashAttribute("error", "No se pudo eliminar el préstamo.");
        }
        return "redirect:/vista/prestamos";
    }
}