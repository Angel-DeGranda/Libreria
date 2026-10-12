package com.api.libreria.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.libreria.models.PersonaModel;
import com.api.libreria.repositories.IPersonaRepository;
import com.api.libreria.repositories.IPrestamoRepository;

@Service 
public class PersonaService {
    
    @Autowired
    IPersonaRepository personaRepository;

    @Autowired
    IPrestamoRepository prestamoRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
 
    public ArrayList<PersonaModel> obtenerPersonas(){   
        return (ArrayList<PersonaModel>) personaRepository.findAll();
    }
 
    public PersonaModel guardarPersona(PersonaModel persona){
        persona.setContrasena(passwordEncoder.encode(persona.getContrasena()));
        return personaRepository.save(persona);
    }
 
    public Optional<PersonaModel> obtenerPorId(Long id){
        return personaRepository.findById(id);
    }
 
    public PersonaModel actualizarPersona(PersonaModel request, Long id){
        PersonaModel persona = personaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe una persona con id: " + id));

        if ("INACTIVO".equals(request.getEstado()) && prestamoRepository.existsBySocioIdAndFechaDevolucionIsNull(id)) {
            throw new IllegalStateException("No se puede marcar como inactivo a un socio con préstamos sin devolver.");
        }

        persona.setNombre(request.getNombre());
        persona.setApellidos(request.getApellidos());
        persona.setCorreo(request.getCorreo());
        persona.setRol(request.getRol());
        persona.setEstado(request.getEstado());
        persona.setFechaNacimiento(request.getFechaNacimiento());
        persona.setTelefono(request.getTelefono());
        persona.setDireccion(request.getDireccion());
 
        return personaRepository.save(persona);
    }
 
    public boolean eliminarPersona(Long id){
        try{
            personaRepository.deleteById(id);
            return true;
        } catch(Exception err){
            return false;
        }
    }
}
