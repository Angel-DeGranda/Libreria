package com.api.libreria.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "prestamos")
public class PrestamoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Al crear un préstamo basta con mandar el ISBN: { "libro": { "isbn": 1 } }.
    @NotNull(message = "El libro es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "libro_isbn", nullable = false)
    private LibroModel libro;

    // Igual que el libro: basta con mandar el id: { "socio": { "id": 1 } }
    @NotNull(message = "El socio es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "socio_id", nullable = false)
    private PersonaModel socio;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaPrestamo;

    @Column
    private LocalDateTime fechaDevolucion;

    @PrePersist
    private void asignarFechas() {
        fechaPrestamo = LocalDateTime.now();
        fechaDevolucion = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LibroModel getLibro() {
        return libro;
    }

    public void setLibro(LibroModel libro) {
        this.libro = libro;
    }

    public PersonaModel getSocio() {
        return socio;
    }

    public void setSocio(PersonaModel socio) {
        this.socio = socio;
    }

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }


}