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
 
import com.api.libreria.models.LibroModel;
import com.api.libreria.services.LibroService;

@RestController
@RequestMapping("/libros")
public class LibroController {
    
    @Autowired
    private LibroService libroService;

    @GetMapping
    public ArrayList<LibroModel> obtenerLibro(){
        return this.libroService.obtenerlibros();
    }

    @PostMapping
    public LibroModel guardarLibro(@RequestBody LibroModel libro){
        return this.libroService.guardarLibro(libro);
    }

    @GetMapping(path = "/{isbn}")
    public Optional<LibroModel> obtenerPorId(@PathVariable("isbn") Long isbn){
        return this.libroService.obtenerPorId(isbn);
    }

    @PutMapping(path = "/{isbn}")
    public LibroModel actualizarLibro(@RequestBody LibroModel libro, @PathVariable("isbn") Long isbn){
        return this.libroService.actualizarLibro(libro, isbn);
    }

    @DeleteMapping(path = "/{isbn}")
    public String eliminarPorId(@PathVariable("isbn") Long isbn){
        boolean ok = this.libroService.eliminarLibro(isbn);
        if(ok){
            return "Se eliminó el libro con ISBN: " + isbn;
        }
        return "No se encontró un libro con ISBN: " + isbn;
    }
}
