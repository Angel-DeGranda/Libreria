package com.api.libreria.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity 
@Table (name="personas")
public class PersonaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
 
    @NotBlank(message = "Los apellidos son obligatorios")
    @Column(nullable = false)
    private String apellidos;
 
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Column(nullable = false, unique = true)
    private String correo;
 
    // WRITE_ONLY: se puede recibir en el JSON, pero nunca se devuelve en las respuestas.
    // Que la contraseña sea obligatoria al crear se valida en PersonaService,
    // porque al actualizar una persona no se envía.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column
    private String contrasena;
 
    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "ADMIN|USUARIO", message = "El rol debe ser ADMIN o USUARIO")
    @Column(nullable = false)
    private String rol;
 
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "ACTIVO|INACTIVO", message = "El estado debe ser ACTIVO o INACTIVO")
    @Column(nullable = false)
    private String estado;
 
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
 
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    @Column
    private LocalDate fechaNacimiento;
 
    @Pattern(regexp = "^(\\d{10})?$", message = "El teléfono debe tener 10 dígitos")
    @Column
    private String telefono;
 
    @Size(max = 200, message = "La dirección no puede pasar de 200 caracteres")
    @Column
    private String direccion;
 
    @PrePersist
    private void asignarFechaCreacion() {
        fechaCreacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    
    
}
