// src/main/java/com/ecusol/core/dto/ResumenClienteDTO.java USA VENTANILLA
package com.ecusol.core.dto;

import lombok.Data;
import java.util.List;

@Data
public class ResumenClienteDTO {
    private Integer clienteId;
    private String nombres;
    private String cedula;
    private String estado;
    private List<CuentaResumenDTO> cuentas;
}