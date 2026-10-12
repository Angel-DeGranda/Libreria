package com.api.libreria.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.libreria.models.PrestamoModel;
import com.api.libreria.services.PrestamoService;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @GetMapping
    public ArrayList<PrestamoModel> obtenerPrestamos(){
        return this.prestamoService.obtenerPrestamos();
    }

    @PostMapping
    public PrestamoModel guardarPrestamo(@RequestBody PrestamoModel prestamo){
        return this.prestamoService.guardarPrestamo(prestamo);
    }

    @GetMapping(path = "/{id}")
    public Optional<PrestamoModel> obtenerPorId(@PathVariable("id") Long id){
        return this.prestamoService.obtenerPorId(id);
    }

    @PutMapping(path = "/{id}")
    public PrestamoModel actualizarPrestamo(@RequestBody PrestamoModel prestamo, @PathVariable("id") Long id){
        return this.prestamoService.actualizarPrestamo(prestamo, id);
    }

    @PutMapping(path = "/{id}/devolver")
    public PrestamoModel devolverPrestamo(@PathVariable("id") Long id){
        return this.prestamoService.marcarDevolucion(id);
    }

    @DeleteMapping(path = "/{id}")
    public String eliminarPorId(@PathVariable("id") Long id){
        boolean ok = this.prestamoService.eliminarPrestamo(id);
        if(ok){
            return "Se eliminó el préstamo con id: " + id;
        }
        return "No se encontró un préstamo con id: " + id;
    }
}