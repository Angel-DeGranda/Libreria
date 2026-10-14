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

import com.api.libreria.models.PersonaModel;
import com.api.libreria.services.PersonaService;

import jakarta.validation.Valid;

@Controller
public class PersonaVistaController {
    
    private final PersonaService personaService;
 
    public PersonaVistaController(PersonaService personaService) {
        this.personaService = personaService;
    }
 
    @GetMapping("/vista/personas")
    public String mostrarPersonas(Model model) {
        model.addAttribute("personas", personaService.obtenerPersonas());
        if (!model.containsAttribute("persona")) {
            model.addAttribute("persona", new PersonaModel());
        }
        return "personas";
    }
 
    @PostMapping("/vista/personas/guardar")
    public String guardarPersona(@Valid @ModelAttribute PersonaModel persona, BindingResult resultado,
            RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            atributos.addFlashAttribute("error", mensajeDeErrores(resultado));
            return "redirect:/vista/personas";
        }
        try {
            personaService.guardarPersona(persona);
            atributos.addFlashAttribute("mensaje", "Persona registrada correctamente.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/personas";
    }
 
    @PostMapping("/vista/personas/{id}/actualizar")
    public String actualizarPersona(@PathVariable Long id, @Valid @ModelAttribute PersonaModel persona,
            BindingResult resultado, RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            atributos.addFlashAttribute("error", mensajeDeErrores(resultado));
            return "redirect:/vista/personas";
        }
        try {
            personaService.actualizarPersona(persona, id);
            atributos.addFlashAttribute("mensaje", "Persona actualizada correctamente.");
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException exception) {
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/personas";
    }
 
    @PostMapping("/vista/personas/{id}/eliminar")
    public String eliminarPersona(@PathVariable Long id, RedirectAttributes atributos) {
        try {
            personaService.eliminarPersona(id);
            atributos.addFlashAttribute("mensaje", "Persona eliminada correctamente.");
        } catch (IllegalStateException | NoSuchElementException exception) {
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/personas";
    }
 
    private String mensajeDeErrores(BindingResult resultado) {
        return resultado.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(" "));
    }
}
