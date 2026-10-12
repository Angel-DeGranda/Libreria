package com.api.libreria.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.libreria.models.LibroModel;
import com.api.libreria.models.PersonaModel;
import com.api.libreria.models.PrestamoModel;
import com.api.libreria.repositories.ILibroRepository;
import com.api.libreria.repositories.IPersonaRepository;
import com.api.libreria.repositories.IPrestamoRepository;

@Service
public class PrestamoService {

    @Autowired
    IPrestamoRepository prestamoRepository;

    @Autowired
    ILibroRepository libroRepository;

    @Autowired
    IPersonaRepository personaRepository;

    public ArrayList<PrestamoModel> obtenerPrestamos(){
        return (ArrayList<PrestamoModel>) prestamoRepository.findAll();
    }

    public PrestamoModel guardarPrestamo(PrestamoModel prestamo){
        Long socioId = prestamo.getSocio().getId();
        Long libroIsbn = prestamo.getLibro().getIsbn();

        PersonaModel socio = personaRepository.findById(socioId)
                .orElseThrow(() -> new IllegalArgumentException("No existe una persona con id: " + socioId));
        
        if ("ADMIN".equals(socio.getRol())) {
            throw new IllegalStateException("No se le puede prestar a un administrador");
        }
        if (!"ACTIVO".equals(socio.getEstado())) {
            throw new IllegalStateException("No se le puede prestar a un socio inactivo.");
        }

        boolean yaLoTienePrestado = prestamoRepository.existsBySocioIdAndLibroIsbnAndFechaDevolucionIsNull(socioId, libroIsbn);
        if (yaLoTienePrestado) {
            throw new IllegalStateException("Este socio ya tiene este libro prestado y no lo ha devuelto.");
        }

        LibroModel libro = libroRepository.findById(libroIsbn)
                .orElseThrow(() -> new IllegalArgumentException("No existe un libro con ISBN: " + libroIsbn));

        if (libro.getNumEjemplares() <= 0) {
            throw new IllegalStateException("No hay ejemplares disponibles de este libro.");
        }

        libro.setNumEjemplares(libro.getNumEjemplares() - 1);
        libroRepository.save(libro);
        prestamo.setLibro(libro);
        return prestamoRepository.save(prestamo);
    }

    

    public Optional<PrestamoModel> obtenerPorId(Long id){
        return prestamoRepository.findById(id);
    }

    public PrestamoModel actualizarPrestamo(PrestamoModel request, Long id){
        PrestamoModel prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un préstamo con id: " + id));
        prestamo.setLibro(request.getLibro());
        prestamo.setSocio(request.getSocio());
        prestamo.setFechaDevolucion(request.getFechaDevolucion());

        return prestamoRepository.save(prestamo);
    }

    public PrestamoModel marcarDevolucion(Long id){
        PrestamoModel prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un préstamo con id: " + id));

        if (prestamo.getFechaDevolucion() != null) {
            throw new IllegalStateException("Este préstamo ya fue devuelto.");
        }

        prestamo.setFechaDevolucion(LocalDateTime.now());

        LibroModel libro = prestamo.getLibro();
        libro.setNumEjemplares(libro.getNumEjemplares() + 1);
        libroRepository.save(libro);

        return prestamoRepository.save(prestamo);
    }

    public boolean eliminarPrestamo(Long id){
        try{
            PrestamoModel prestamo = prestamoRepository.findById(id).orElse(null);
            if (prestamo == null) {
                return false;
            }

            if (prestamo.getFechaDevolucion() == null) {
                LibroModel libro = prestamo.getLibro();
                libro.setNumEjemplares(libro.getNumEjemplares() + 1);
                libroRepository.save(libro);
            }

            prestamoRepository.deleteById(id);
            return true;
        } catch(Exception err){
            return false;
        }
    }

}
