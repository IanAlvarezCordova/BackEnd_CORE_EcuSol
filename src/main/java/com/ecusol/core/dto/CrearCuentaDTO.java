package com.ecusol.core.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CrearCuentaDTO {
    private Integer clienteId;
    private Integer tipoCuentaId;
    private BigDecimal saldoInicial; 
}