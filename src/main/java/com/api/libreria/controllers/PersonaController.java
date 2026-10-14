package com.api.libreria.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.libreria.models.PersonaModel;
import com.api.libreria.services.PersonaService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/personas")
public class PersonaController {
    
    @Autowired
    private PersonaService personaService;
 
    @GetMapping
    public ResponseEntity<List<PersonaModel>> obtenerPersonas(){
        return ResponseEntity.ok(personaService.obtenerPersonas());
    }
 
    @PostMapping
    public ResponseEntity<PersonaModel> guardarPersona(@Valid @RequestBody PersonaModel persona){
        PersonaModel guardada = personaService.guardarPersona(persona);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardada.getId())
                .toUri();
        return ResponseEntity.created(ubicacion).body(guardada);
    }
 
    @GetMapping(path = "/{id}")
    public ResponseEntity<PersonaModel> obtenerPorId(@PathVariable("id") Long id){
        return personaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
 
    @PutMapping(path = "/{id}")
    public ResponseEntity<PersonaModel> actualizarPersona(@Valid @RequestBody PersonaModel persona, @PathVariable("id") Long id){
        return ResponseEntity.ok(personaService.actualizarPersona(persona, id));
    }
 
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable("id") Long id){
        personaService.eliminarPersona(id);
        return ResponseEntity.noContent().build();
    }
}
