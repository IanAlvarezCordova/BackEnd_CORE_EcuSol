package com.ecusol.core.controller;

import com.ecusol.core.dto.CuentaResumenDTO;
import com.ecusol.core.dto.TitularCuentaDTO;
import com.ecusol.core.dto.TransaccionDTO;
import com.ecusol.core.model.Cuenta;
import com.ecusol.core.repository.CuentaRepository;
import com.ecusol.core.repository.TransaccionRepository;
import com.ecusol.core.service.CoreBancarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/core/cuentas")
public class CoreCuentaController {

    @Autowired 
    private CuentaRepository cuentaRepo;
    @Autowired 
    private TransaccionRepository transaccionRepo;
    @Autowired 
    private CoreBancarioService coreBancarioService;

    @GetMapping("/por-cliente/{clienteId}")
    public ResponseEntity<List<CuentaResumenDTO>> getCuentasPorCliente(@PathVariable Integer clienteId) {
        List<CuentaResumenDTO> cuentas = cuentaRepo.findResumenByClienteId(clienteId);
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/por-numero/{numeroCuenta}")
    public ResponseEntity<Cuenta> getCuentaPorNumero(@PathVariable String numeroCuenta) {
        return cuentaRepo.findByNumeroCuenta(numeroCuenta)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cuenta no encontrada con número " + numeroCuenta
                ));
    }

    @GetMapping("/{numeroCuenta}/movimientos")
    public ResponseEntity<List<TransaccionDTO>> getMovimientos(@PathVariable String numeroCuenta) {
        List<TransaccionDTO> dtos = transaccionRepo
                .findByCuenta_NumeroCuentaOrderByFechaEjecucionDesc(numeroCuenta)
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
        try {
            TitularCuentaDTO titular = coreBancarioService.buscarTitularPorCuenta(numeroCuenta);
            return ResponseEntity.ok(titular);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cuenta no encontrada o sin titular para el número " + numeroCuenta,
                    ex
            );
        }
}