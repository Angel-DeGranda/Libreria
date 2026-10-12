package com.api.libreria.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.libreria.models.PersonaModel;

@Repository 
public interface IPersonaRepository extends JpaRepository<PersonaModel, Long> {
    ArrayList<PersonaModel> findByNombre(String nombre);
}
