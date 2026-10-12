package com.api.libreria.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.libreria.models.LibroModel;

@Repository
public interface ILibroRepository extends JpaRepository<LibroModel, Long> {
    ArrayList<LibroModel> findByAutor(String autor);
}
