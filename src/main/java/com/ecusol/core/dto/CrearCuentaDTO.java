//ubi: src/main/java/com/ecusol/core/dto/CrearCuentaDTO.java
package com.ecusol.core.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CrearCuentaDTO {
    private Integer clienteId;
    private Integer tipoCuentaId; // 1=Ahorros, 2=Corriente
    private BigDecimal saldoInicial; // Usualmente 0.00 al abrir
}