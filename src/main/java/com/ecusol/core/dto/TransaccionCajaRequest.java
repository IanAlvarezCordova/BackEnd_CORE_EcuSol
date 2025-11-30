// src/main/java/com/ecusol/core/dto/TransaccionCajaRequest.java PARA USO DE VENTANILLA
package com.ecusol.core.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransaccionCajaRequest {
    private String tipoOperacion; // DEPOSITO, RETIRO
    private String cuentaOrigen;
    private String cuentaDestino;
    private BigDecimal monto;
    private String descripcion;
}