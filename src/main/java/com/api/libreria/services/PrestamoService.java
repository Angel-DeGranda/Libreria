package com.api.libreria.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.libreria.models.LibroModel;
import com.api.libreria.models.PersonaModel;
import com.api.libreria.models.PrestamoModel;
import com.api.libreria.repositories.ILibroRepository;
import com.api.libreria.repositories.IPersonaRepository;
import com.api.libreria.repositories.IPrestamoRepository;

import jakarta.transaction.Transactional;

@Service
public class PrestamoService {

    @Autowired
    private IPrestamoRepository prestamoRepository;

    @Autowired
    private ILibroRepository libroRepository;

    @Autowired
    private IPersonaRepository personaRepository;

    public List<PrestamoModel> obtenerPrestamos() {
        return prestamoRepository.findAll();
    }

    @Transactional
    public PrestamoModel guardarPrestamo(PrestamoModel prestamo) {
        validarReferencias(prestamo);

        PersonaModel socio = buscarSocioPrestable(prestamo.getSocio().getId());
        LibroModel libro = buscarLibro(prestamo.getLibro().getIsbn());
        validarNoDuplicado(socio.getId(), libro.getIsbn());
        descontarEjemplar(libro);

        // Se usan las entidades completas de la BD (no las que mandó el cliente) y se
        // ignora cualquier id recibido
        prestamo.setId(null);
        prestamo.setSocio(socio);
        prestamo.setLibro(libro);
        return prestamoRepository.save(prestamo);
    }

    public Optional<PrestamoModel> obtenerPorId(Long id) {
        return prestamoRepository.findById(id);
    }

    @Transactional
    public PrestamoModel actualizarPrestamo(PrestamoModel request, Long id) {
        PrestamoModel prestamo = buscarPrestamo(id);
        if (prestamo.getFechaDevolucion() != null) {
            throw new IllegalStateException("No se puede modificar un préstamo que ya fue devuelto.");
        }
        validarReferencias(request);

        Long nuevoSocioId = request.getSocio().getId();
        Long nuevoIsbn = request.getLibro().getIsbn();
        boolean cambiaSocio = !prestamo.getSocio().getId().equals(nuevoSocioId);
        boolean cambiaLibro = !prestamo.getLibro().getIsbn().equals(nuevoIsbn);
        if (!cambiaSocio && !cambiaLibro) {
            return prestamo;
        }

        PersonaModel socio = cambiaSocio ? buscarSocioPrestable(nuevoSocioId) : prestamo.getSocio();
        LibroModel libro = cambiaLibro ? buscarLibro(nuevoIsbn) : prestamo.getLibro();
        validarNoDuplicado(socio.getId(), libro.getIsbn());

        if (cambiaLibro) {
            descontarEjemplar(libro);
            devolverEjemplar(prestamo.getLibro());
        }

        prestamo.setSocio(socio);
        prestamo.setLibro(libro);
        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public PrestamoModel marcarDevolucion(Long id) {
        PrestamoModel prestamo = buscarPrestamo(id);
        if (prestamo.getFechaDevolucion() != null) {
            throw new IllegalStateException("Este préstamo ya fue devuelto.");
        }

        prestamo.setFechaDevolucion(LocalDateTime.now());
        devolverEjemplar(prestamo.getLibro());
        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public void eliminarPrestamo(Long id) {
        PrestamoModel prestamo = buscarPrestamo(id);

        // Si el préstamo seguía activo, el ejemplar vuelve al inventario
        if (prestamo.getFechaDevolucion() == null) {
            devolverEjemplar(prestamo.getLibro());
        }
        prestamoRepository.delete(prestamo);
    }

    private PrestamoModel buscarPrestamo(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe un préstamo con id: " + id));
    }

    private LibroModel buscarLibro(Long isbn) {
        return libroRepository.findById(isbn)
                .orElseThrow(() -> new NoSuchElementException("No existe un libro con ISBN: " + isbn));
    }

    private PersonaModel buscarSocioPrestable(Long socioId) {
        PersonaModel socio = personaRepository.findById(socioId)
                .orElseThrow(() -> new NoSuchElementException("No existe una persona con id: " + socioId));

        if ("ADMIN".equals(socio.getRol())) {
            throw new IllegalStateException("No se le puede prestar a un administrador.");
        }
        if (!"ACTIVO".equals(socio.getEstado())) {
            throw new IllegalStateException("No se le puede prestar a un socio inactivo.");
        }
        return socio;
    }

    private void validarReferencias(PrestamoModel prestamo) {
        if (prestamo.getSocio() == null || prestamo.getSocio().getId() == null) {
            throw new IllegalArgumentException("Debe indicar el id del socio.");
        }
        if (prestamo.getLibro() == null || prestamo.getLibro().getIsbn() == null) {
            throw new IllegalArgumentException("Debe indicar el ISBN del libro.");
        }
    }

    private void validarNoDuplicado(Long socioId, Long isbn) {
        if (prestamoRepository.existsBySocioIdAndLibroIsbnAndFechaDevolucionIsNull(socioId, isbn)) {
            throw new IllegalStateException("Este socio ya tiene este libro prestado y no lo ha devuelto.");
        }
    }

    private void descontarEjemplar(LibroModel libro) {
        if (libro.getNumEjemplares() <= 0) {
            throw new IllegalStateException("No hay ejemplares disponibles de este libro.");
        }
        libro.setNumEjemplares(libro.getNumEjemplares() - 1);
        libroRepository.save(libro);
    }

    private void devolverEjemplar(LibroModel libro) {
        libro.setNumEjemplares(libro.getNumEjemplares() + 1);
        libroRepository.save(libro);
    }

}
