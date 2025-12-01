package com.ecusol.core.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RegistroClienteCoreDTO {
    private String cedula;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono; 
    private LocalDate fechaNacimiento;
}