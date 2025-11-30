//ubi: src/main/java/com/ecusol/core/dto/RegistroClienteCoreDTO.java
package com.ecusol.core.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RegistroClienteCoreDTO {
    // Datos Personales
    private String cedula;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono; // Opcional si lo guardas en persona
    private LocalDate fechaNacimiento;
}