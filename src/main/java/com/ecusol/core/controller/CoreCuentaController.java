package com.ecusol.core.controller;

import com.ecusol.core.dto.CuentaResumenDTO;
import com.ecusol.core.dto.TitularCuentaDTO;
import com.ecusol.core.dto.TransaccionDTO; // <--- DTO NUEVO
import com.ecusol.core.model.Cuenta;
import com.ecusol.core.repository.CuentaRepository;
import com.ecusol.core.repository.TransaccionRepository;
import com.ecusol.core.service.CoreBancarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/core/cuentas")
public class CoreCuentaController {

    @Autowired private CuentaRepository cuentaRepo;
    @Autowired private TransaccionRepository transaccionRepo;
    @Autowired private CoreBancarioService coreBancarioService;

    @GetMapping("/por-cliente/{clienteId}")
    public ResponseEntity<List<CuentaResumenDTO>> getCuentasPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(cuentaRepo.findResumenByClienteId(clienteId));
    }

    @GetMapping("/por-numero/{numeroCuenta}")
    public ResponseEntity<Cuenta> getCuentaPorNumero(@PathVariable String numeroCuenta) {
        return cuentaRepo.findByNumeroCuenta(numeroCuenta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- CORRECCIÓN MOVIMIENTOS (Uso de DTO) ---
    @GetMapping("/{numeroCuenta}/movimientos")
    public ResponseEntity<List<TransaccionDTO>> getMovimientos(@PathVariable String numeroCuenta) {
        // Mapeamos manualmente para cortar la cadena de Hibernate
        List<TransaccionDTO> dtos = transaccionRepo.findByCuenta_NumeroCuentaOrderByFechaEjecucionDesc(numeroCuenta)
            .stream()
            .map(t -> {
                TransaccionDTO dto = new TransaccionDTO();
                dto.setTransaccionId(t.getTransaccionId());
                dto.setReferencia(t.getReferencia());
                dto.setRolTransaccion(t.getRolTransaccion());
                dto.setMonto(t.getMonto());
                dto.setDescripcion(t.getDescripcion());
                dto.setFechaEjecucion(t.getFechaEjecucion());
                return dto;
            })
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/validar-titular/{numeroCuenta}")
    public ResponseEntity<TitularCuentaDTO> validarTitular(@PathVariable String numeroCuenta) {
        return ResponseEntity.ok(coreBancarioService.buscarTitularPorCuenta(numeroCuenta));
    }
}