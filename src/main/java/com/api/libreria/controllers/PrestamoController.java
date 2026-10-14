package com.api.libreria.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.libreria.models.PrestamoModel;
import com.api.libreria.services.PrestamoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;
 
    @GetMapping
    public ResponseEntity<List<PrestamoModel>> obtenerPrestamos(){
        return ResponseEntity.ok(prestamoService.obtenerPrestamos());
    }
 
    @PostMapping
    public ResponseEntity<PrestamoModel> guardarPrestamo(@Valid @RequestBody PrestamoModel prestamo){
        PrestamoModel guardado = prestamoService.guardarPrestamo(prestamo);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardado.getId())
                .toUri();
        return ResponseEntity.created(ubicacion).body(guardado);
    }
 
    @GetMapping(path = "/{id}")
    public ResponseEntity<PrestamoModel> obtenerPorId(@PathVariable("id") Long id){
        return prestamoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
 
    @PutMapping(path = "/{id}")
    public ResponseEntity<PrestamoModel> actualizarPrestamo(@Valid @RequestBody PrestamoModel prestamo, @PathVariable("id") Long id){
        return ResponseEntity.ok(prestamoService.actualizarPrestamo(prestamo, id));
    }
 
    @PutMapping(path = "/{id}/devolver")
    public ResponseEntity<PrestamoModel> devolverPrestamo(@PathVariable("id") Long id){
        return ResponseEntity.ok(prestamoService.marcarDevolucion(id));
    }
 
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable("id") Long id){
        prestamoService.eliminarPrestamo(id);
        return ResponseEntity.noContent().build();
    }
}