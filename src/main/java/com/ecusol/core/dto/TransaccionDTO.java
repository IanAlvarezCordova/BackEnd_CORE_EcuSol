package com.ecusol.core.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransaccionDTO {
    private Integer transaccionId;
    private String referencia;
    private String rolTransaccion; 
    private BigDecimal monto;
    private String descripcion;
    private LocalDateTime fechaEjecucion;
}