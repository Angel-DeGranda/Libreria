package com.api.libreria.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class LibroModel {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long isbn;

    @Column
    private String titulo;

    @Column
    private String autor;

    @Column
    private String editorial;

    @Column
    private String anoPublicacion;

    @Column
    private int numEjemplares;

    public Long getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public String getAnoPublicacion() {
        return anoPublicacion;
    }

    public int getNumEjemplares() {
        return numEjemplares;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public void setAnoPublicacion(String anoPublicacion) {
        this.anoPublicacion = anoPublicacion;
    }

    public void setNumEjemplares(int numEjemplares) {
        this.numEjemplares = numEjemplares;
    }


    
}
