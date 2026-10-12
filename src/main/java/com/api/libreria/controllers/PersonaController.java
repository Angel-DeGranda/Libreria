package com.api.libreria.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.libreria.models.PersonaModel;
import com.api.libreria.services.PersonaService;

@RestController 
@RequestMapping("/personas")
public class PersonaController {
    
    @Autowired 
    private PersonaService personaService;

    @GetMapping
    public ArrayList<PersonaModel> obtenerPersonas(){
        return this.personaService.obtenerPersonas();
    }

    @PostMapping
    public PersonaModel guardarPersona(@RequestBody PersonaModel persona){
        return this.personaService.guardarPersona(persona);
    }

    @GetMapping(path = "/{id}")
    public Optional<PersonaModel> obtenerPorId(@PathVariable("id") Long id){
        return this.personaService.obtenerPorId(id);
    }

    @PutMapping(path = "/{id}")
    public PersonaModel actualizarPersona(@RequestBody PersonaModel persona, @PathVariable("id") Long id){
        return this.personaService.actualizarPersona(persona, id);
    }


    @DeleteMapping(path = "/{id}")
    public String eliminarPorId(@PathVariable("id") Long id){
        boolean ok = this.personaService.eliminarPersona(id);
        if(ok){
            return "Se eliminó la persona con id: " + id;
        }
        return "No se encontró una persona con id: " + id;
    }
}
