package com.api.libreria.services;

import java.util.List;
import java.util.NoSuchElementException;
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
    private IPersonaRepository personaRepository;

    @Autowired
    private IPrestamoRepository prestamoRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<PersonaModel> obtenerPersonas() {
        return personaRepository.findAll();
    }

    public PersonaModel guardarPersona(PersonaModel persona) {
        if (persona.getContrasena() == null || persona.getContrasena().length() < 6) {
            throw new IllegalArgumentException("La contraseña es obligatoria y debe tener al menos 6 caracteres.");
        }
        if (personaRepository.existsByCorreo(persona.getCorreo())) {
            throw new IllegalStateException("Ya existe una persona con el correo: " + persona.getCorreo());
        }

        // Evita que un id enviado por el cliente sobrescriba a una persona existente
        persona.setId(null);
        persona.setContrasena(passwordEncoder.encode(persona.getContrasena()));
        return personaRepository.save(persona);
    }

    public Optional<PersonaModel> obtenerPorId(Long id) {
        return personaRepository.findById(id);
    }

    public PersonaModel actualizarPersona(PersonaModel request, Long id) {
        PersonaModel persona = personaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe una persona con id: " + id));

        if (personaRepository.existsByCorreoAndIdNot(request.getCorreo(), id)) {
            throw new IllegalStateException("Ya existe otra persona con el correo: " + request.getCorreo());
        }
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

    public void eliminarPersona(Long id) {
        if (!personaRepository.existsById(id)) {
            throw new NoSuchElementException("No existe una persona con id: " + id);
        }
        if (prestamoRepository.existsBySocioId(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar una persona con préstamos registrados. Márcala como inactiva en su lugar.");
        }
        personaRepository.deleteById(id);
    }
}
