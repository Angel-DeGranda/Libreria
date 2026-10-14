package com.api.libreria.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "libros")
public class LibroModel {
    
    @Id
    @Positive(message = "El ISBN debe ser un número positivo")
    private Long isbn;
 
    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false)
    private String titulo;
 
    @NotBlank(message = "El autor es obligatorio")
    @Column(nullable = false)
    private String autor;
 
    @Column
    private String editorial;
 
    @NotBlank(message = "El año de publicación es obligatorio")
    @Pattern(regexp = "^\\d{4}$", message = "El año de publicación debe tener 4 dígitos")
    @Column
    private String anoPublicacion;
 
    @Min(value = 0, message = "El número de ejemplares no puede ser negativo")
    @Column
    private int numEjemplares;

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getAnoPublicacion() {
        return anoPublicacion;
    }

    public void setAnoPublicacion(String anoPublicacion) {
        this.anoPublicacion = anoPublicacion;
    }

    public int getNumEjemplares() {
        return numEjemplares;
    }

    public void setNumEjemplares(int numEjemplares) {
        this.numEjemplares = numEjemplares;
    }



    
}
