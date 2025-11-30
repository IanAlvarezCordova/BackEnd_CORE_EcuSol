//ubi: src/main/java/com/ecusol/core/dto/TitularCuentaDTO.java
package com.ecusol.core.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TitularCuentaDTO {
    private String numeroCuenta;
    private String nombreCompleto;
    private String identificacionParcial;
    private String tipoCuenta; // <--- Campo Nuevo
}