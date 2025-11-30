//ubi: src/main/java/com/ecusol/core/dto/TransaccionRequestDTO.java
package com.ecusol.core.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransaccionRequestDTO {
    private String cuentaOrigen;
    private String cuentaDestino;
    private BigDecimal monto;
    private String descripcion;
}