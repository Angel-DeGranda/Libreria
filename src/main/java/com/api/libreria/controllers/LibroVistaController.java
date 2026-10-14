package com.api.libreria.controllers;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.libreria.models.LibroModel;
import com.api.libreria.services.LibroService;

import jakarta.validation.Valid;

@Controller
public class LibroVistaController {

    private final LibroService libroService;
 
    public LibroVistaController(LibroService libroService) {
        this.libroService = libroService;
    }
 
    @GetMapping({ "/", "/vista/libros" })
    public String mostrarLibros(Model model) {
        model.addAttribute("libros", libroService.obtenerLibros());
        if (!model.containsAttribute("libro")) {
            model.addAttribute("libro", new LibroModel());
        }
        return "libros";
    }
 
    @PostMapping("/vista/libros/guardar")
    public String guardarLibro(@Valid @ModelAttribute LibroModel libro, BindingResult resultado,
            RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            atributos.addFlashAttribute("error", mensajeDeErrores(resultado));
            return "redirect:/vista/libros";
        }
        try {
            libroService.guardarLibro(libro);
            atributos.addFlashAttribute("mensaje", "Libro registrado correctamente.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/libros";
    }
 
    @PostMapping("/vista/libros/{isbn}/actualizar")
    public String actualizarLibro(@PathVariable Long isbn, @Valid @ModelAttribute LibroModel libro,
            BindingResult resultado, RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            atributos.addFlashAttribute("error", mensajeDeErrores(resultado));
            return "redirect:/vista/libros";
        }
        try {
            libroService.actualizarLibro(libro, isbn);
            atributos.addFlashAttribute("mensaje", "Libro actualizado correctamente.");
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException exception) {
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/libros";
    }
 
    @PostMapping("/vista/libros/{isbn}/eliminar")
    public String eliminarLibro(@PathVariable Long isbn, RedirectAttributes atributos) {
        try {
            libroService.eliminarLibro(isbn);
            atributos.addFlashAttribute("mensaje", "Libro eliminado correctamente.");
        } catch (IllegalStateException | NoSuchElementException exception) {
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/libros";
    }
 
    private String mensajeDeErrores(BindingResult resultado) {
        return resultado.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(" "));
    }
}