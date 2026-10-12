package com.api.libreria.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.libreria.models.PersonaModel;
import com.api.libreria.services.PersonaService;

@Controller
public class PersonaVistaController {
    
    private final PersonaService personaService;

    public PersonaVistaController(PersonaService personaService){
        this.personaService = personaService;
    }

    @GetMapping({ "/", "/vista/personas" })
    public String mostrarLibros(Model model) {
        model.addAttribute("personas", personaService.obtenerPersonas());
        if (!model.containsAttribute("persona")) {
            model.addAttribute("persona", new PersonaModel());
        }
        return "personas";
    }

    @PostMapping("/vista/personas/guardar")
    public String guardarPersona(@ModelAttribute PersonaModel persona, RedirectAttributes atributos) {
        personaService.guardarPersona(persona);
        atributos.addFlashAttribute("mensaje", "Persona registrada correctamente.");
        return "redirect:/vista/personas";
    }

    @PostMapping("/vista/personas/{id}/actualizar")
    public String actualizarPersona(@PathVariable Long id, @ModelAttribute PersonaModel persona, RedirectAttributes atributos) {
        try {
            personaService.actualizarPersona(persona, id);
            atributos.addFlashAttribute("mensaje", "Persona actualizada correctamente.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/personas";
    }

    @PostMapping("/vista/personas/{id}/eliminar")
    public String eliminarPersona(@PathVariable Long id, RedirectAttributes atributos) {
        if (personaService.eliminarPersona(id)) {
            atributos.addFlashAttribute("mensaje", "Persona eliminada correctamente.");
        } else {
            atributos.addFlashAttribute("error", "No se pudo eliminar la persona.");
        }
        return "redirect:/vista/personas";
    }
}
