package com.api.libreria.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.libreria.models.LibroModel;
import com.api.libreria.repositories.ILibroRepository;

@Service
public class LibroService {
    
    @Autowired
    ILibroRepository libroRepository;

    public ArrayList<LibroModel> obtenerlibros(){
        return (ArrayList<LibroModel>) libroRepository.findAll();
    }

    public LibroModel guardarLibro(LibroModel libro){
        return libroRepository.save(libro);
    }

    public Optional<LibroModel> obtenerPorId(Long isbn){
        return libroRepository.findById(isbn);
    }

    public LibroModel actualizarLibro(LibroModel request, Long isbn){
        LibroModel libro = libroRepository.findById(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No existe un libro con ISBN: " + isbn));
        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setAnoPublicacion(request.getAnoPublicacion());
        libro.setNumEjemplares(request.getNumEjemplares());

        return libroRepository.save(libro);
    }

    public boolean eliminarLibro(Long isbn){
        try{
            libroRepository.deleteById(isbn);
            return true;
        } catch(Exception err){
            return false;
        }
    }


}
