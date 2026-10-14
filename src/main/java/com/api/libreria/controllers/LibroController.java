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

import com.api.libreria.models.LibroModel;
import com.api.libreria.services.LibroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/libros")
public class LibroController {
    
    @Autowired
    private LibroService libroService;
 
    @GetMapping
    public ResponseEntity<List<LibroModel>> obtenerLibros(){
        return ResponseEntity.ok(libroService.obtenerLibros());
    }
 
    @PostMapping
    public ResponseEntity<LibroModel> guardarLibro(@Valid @RequestBody LibroModel libro){
        LibroModel guardado = libroService.guardarLibro(libro);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{isbn}")
                .buildAndExpand(guardado.getIsbn())
                .toUri();
        return ResponseEntity.created(ubicacion).body(guardado);
    }
 
    @GetMapping(path = "/{isbn}")
    public ResponseEntity<LibroModel> obtenerPorId(@PathVariable("isbn") Long isbn){
        return libroService.obtenerPorId(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
 
    @PutMapping(path = "/{isbn}")
    public ResponseEntity<LibroModel> actualizarLibro(@Valid @RequestBody LibroModel libro, @PathVariable("isbn") Long isbn){
        return ResponseEntity.ok(libroService.actualizarLibro(libro, isbn));
    }
 
    @DeleteMapping(path = "/{isbn}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable("isbn") Long isbn){
        libroService.eliminarLibro(isbn);
        return ResponseEntity.noContent().build();
    }
}
