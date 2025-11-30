//ubi: src/main/java/com/ecusol/core/model/Persona.java
package com.ecusol.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "persona")
@PrimaryKeyJoinColumn(name = "clienteid") // Minúscula
@Getter
@Setter
public class Persona extends Cliente {

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    // CORRECCIÓN CRÍTICA: Todo a minúsculas para que coincida con PostgreSQL
    @Column(name = "numeroidentificacion", unique = true, nullable = false, length = 13)
    private String numeroIdentificacion;

    @Column(name = "tipoidentificacion", nullable = false, length = 15)
    private String tipoIdentificacion;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Column(name = "fechanacimiento", nullable = false)
    private LocalDate fechaNacimiento;
}