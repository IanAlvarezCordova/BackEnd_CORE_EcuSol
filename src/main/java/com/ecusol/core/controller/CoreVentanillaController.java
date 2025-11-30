// src/main/java/com/ecusol/core/controller/CoreVentanillaController.java
package com.ecusol.core.controller;

import com.ecusol.core.dto.*;
import com.ecusol.core.repository.ClienteRepository;
import com.ecusol.core.repository.CuentaRepository;
import com.ecusol.core.repository.PersonaRepository;
import com.ecusol.core.repository.TransaccionRepository;
import com.ecusol.core.service.CoreBancarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/core/ventanilla")
public class CoreVentanillaController {

    @Autowired private PersonaRepository personaRepo;
    @Autowired private CuentaRepository cuentaRepo;
    @Autowired private ClienteRepository clienteRepo;
    @Autowired private TransaccionRepository transaccionRepo;
    @Autowired private CoreBancarioService service;

    // --- BÚSQUEDA CLIENTE ---
    @GetMapping("/buscar-cliente/{cedula}")
    public ResponseEntity<ResumenClienteDTO> buscarPorCedula(@PathVariable String cedula) {
        return personaRepo.findByNumeroIdentificacion(cedula).map(p -> {
            ResumenClienteDTO dto = new ResumenClienteDTO();
            dto.setClienteId(p.getClienteId());
            dto.setNombres(p.getNombres() + " " + p.getApellidos());
            dto.setCedula(p.getNumeroIdentificacion());
            // Obtenemos estado del cliente padre
            dto.setEstado(clienteRepo.findById(p.getClienteId()).get().getEstado());
            dto.setCuentas(cuentaRepo.findResumenByClienteId(p.getClienteId()));
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }

    // --- OPERACIONES (DEPÓSITO, RETIRO, TRANSFERENCIA) ---
    @PostMapping("/operar")
    public ResponseEntity<String> operarCaja(@RequestBody TransaccionCajaRequest req) {
        try {
            String ref = "";
            if ("DEPOSITO".equals(req.getTipoOperacion())) {
                ref = service.procesarDeposito(req.getCuentaOrigen(), req.getMonto(), req.getDescripcion());
            } else if ("RETIRO".equals(req.getTipoOperacion())) {
                ref = service.procesarRetiro(req.getCuentaOrigen(), req.getMonto(), req.getDescripcion());
            } else if ("TRANSFERENCIA".equals(req.getTipoOperacion())) {
                 TransaccionRequestDTO trf = new TransaccionRequestDTO();
                 trf.setCuentaOrigen(req.getCuentaOrigen());
                 trf.setCuentaDestino(req.getCuentaDestino());
                 trf.setMonto(req.getMonto());
                 trf.setDescripcion(req.getDescripcion());
                 ref = service.procesarTransferencia(trf);
            }
            return ResponseEntity.ok(ref);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // --- VALIDACIÓN CUENTA DESTINO ---
    @GetMapping("/info-cuenta/{numero}")
    public ResponseEntity<TitularCuentaDTO> infoCuenta(@PathVariable String numero) {
        try {
            return ResponseEntity.ok(service.buscarTitularPorCuenta(numero));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- ADMINISTRATIVO (ACTIVAR/INACTIVAR) ---

    @PutMapping("/cuentas/{numero}/estado")
    public ResponseEntity<String> cambiarEstadoCuenta(@PathVariable String numero, @RequestParam String estado) {
        return cuentaRepo.findByNumeroCuenta(numero).map(c -> {
            c.setEstado(estado);
            cuentaRepo.save(c);
            return ResponseEntity.ok("Estado actualizado a " + estado);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/clientes/estado")
    public ResponseEntity<String> cambiarEstadoCliente(@RequestParam String cedula, @RequestParam String estado) {
        return personaRepo.findByNumeroIdentificacion(cedula).map(p -> {
            return clienteRepo.findById(p.getClienteId()).map(c -> {
                c.setEstado(estado);
                clienteRepo.save(c);
                return ResponseEntity.ok("Cliente actualizado a " + estado);
            }).orElse(ResponseEntity.notFound().build());
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/cuentas/{numero}")
    @Transactional 
    public ResponseEntity<String> eliminarCuenta(@PathVariable String numero) {
        return cuentaRepo.findByNumeroCuenta(numero).map(c -> {
            transaccionRepo.deleteByCuenta_CuentaId(c.getCuentaId());
            cuentaRepo.delete(c); 
            return ResponseEntity.ok("Cuenta y su historial eliminados permanentemente");
        }).orElse(ResponseEntity.notFound().build());
    }
}