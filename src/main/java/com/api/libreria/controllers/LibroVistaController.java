package com.api.libreria.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.libreria.models.LibroModel;
import com.api.libreria.services.LibroService;

@Controller
public class LibroVistaController {

    private final LibroService libroService;

    public LibroVistaController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping({ "/", "/vista/libros" })
    public String mostrarLibros(Model model) {
        model.addAttribute("libros", libroService.obtenerlibros());
        if (!model.containsAttribute("libro")) {
            model.addAttribute("libro", new LibroModel());
        }
        return "libros";
    }

    @PostMapping("/vista/libros/guardar")
    public String guardarLibro(@ModelAttribute LibroModel libro, RedirectAttributes atributos) {
        libroService.guardarLibro(libro);
        atributos.addFlashAttribute("mensaje", "Libro registrado correctamente.");
        return "redirect:/vista/libros";
    }

    @PostMapping("/vista/libros/{isbn}/actualizar")
    public String actualizarLibro(@PathVariable Long isbn, @ModelAttribute LibroModel libro,
            RedirectAttributes atributos) {
        try {
            libroService.actualizarLibro(libro, isbn);
            atributos.addFlashAttribute("mensaje", "Libro actualizado correctamente.");
        } catch (IllegalArgumentException exception) {
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/libros";
    }

    @PostMapping("/vista/libros/{isbn}/eliminar")
    public String eliminarLibro(@PathVariable Long isbn, RedirectAttributes atributos) {
        if (libroService.eliminarLibro(isbn)) {
            atributos.addFlashAttribute("mensaje", "Libro eliminado correctamente.");
        } else {
            atributos.addFlashAttribute("error", "No se pudo eliminar el libro.");
        }
        return "redirect:/vista/libros";
    }
}