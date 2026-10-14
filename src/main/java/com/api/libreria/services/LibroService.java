package com.api.libreria.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.libreria.models.LibroModel;
import com.api.libreria.repositories.ILibroRepository;
import com.api.libreria.repositories.IPrestamoRepository;

@Service
public class LibroService {
    
    @Autowired
    private ILibroRepository libroRepository;
 
    @Autowired
    private IPrestamoRepository prestamoRepository;
 
    public List<LibroModel> obtenerLibros(){
        return libroRepository.findAll();
    }
 
    public LibroModel guardarLibro(LibroModel libro){
        if (libro.getIsbn() == null) {
            throw new IllegalArgumentException("El ISBN es obligatorio.");
        }
        // Sin esta validación, save() sobrescribiría el libro que ya tenga ese ISBN
        if (libroRepository.existsById(libro.getIsbn())) {
            throw new IllegalStateException("Ya existe un libro con ISBN: " + libro.getIsbn());
        }
        return libroRepository.save(libro);
    }
 
    public Optional<LibroModel> obtenerPorId(Long isbn){
        return libroRepository.findById(isbn);
    }
 
    public LibroModel actualizarLibro(LibroModel request, Long isbn){
        LibroModel libro = libroRepository.findById(isbn)
                .orElseThrow(() -> new NoSuchElementException("No existe un libro con ISBN: " + isbn));
 
        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setEditorial(request.getEditorial());
        libro.setAnoPublicacion(request.getAnoPublicacion());
        libro.setNumEjemplares(request.getNumEjemplares());
 
        return libroRepository.save(libro);
    }
 
    public void eliminarLibro(Long isbn){
        if (!libroRepository.existsById(isbn)) {
            throw new NoSuchElementException("No existe un libro con ISBN: " + isbn);
        }
        if (prestamoRepository.existsByLibroIsbn(isbn)) {
            throw new IllegalStateException("No se puede eliminar un libro que tiene préstamos registrados.");
        }
        libroRepository.deleteById(isbn);
    }


}
