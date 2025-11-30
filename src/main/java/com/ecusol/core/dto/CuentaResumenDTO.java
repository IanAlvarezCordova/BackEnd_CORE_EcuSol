// src/main/java/com/ecusol/core/dto/CuentaResumenDTO.java
package com.ecusol.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CuentaResumenDTO {
    private Integer cuentaId;
    private String numeroCuenta;
    private BigDecimal saldo;
    private String estado;
    private Integer tipoCuentaId; // ID plano, sin objeto anidado
}