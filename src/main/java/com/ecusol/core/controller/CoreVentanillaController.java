// src/main/java/com/ecusol/core/controller/CoreVentanillaController.java
package com.ecusol.core.controller;

import com.ecusol.core.dto.*;
import com.ecusol.core.repository.ClienteRepository;
import com.ecusol.core.repository.CuentaRepository;
import com.ecusol.core.repository.PersonaRepository;
import com.ecusol.core.repository.TransaccionRepository;
import com.ecusol.core.service.CoreBancarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/core/v1/ventanilla")
public class CoreVentanillaController {

    @Autowired 
    private PersonaRepository personaRepo;
    @Autowired 
    private CuentaRepository cuentaRepo;
    @Autowired 
    private ClienteRepository clienteRepo;
    @Autowired 
    private TransaccionRepository transaccionRepo;
    @Autowired 
    private CoreBancarioService service;

    @GetMapping("/buscar-cliente/{cedula}")
    public ResponseEntity<ResumenClienteDTO> buscarPorCedula(@PathVariable String cedula) {
        var persona = personaRepo.findByNumeroIdentificacion(cedula)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con cédula " + cedula
                ));

        var cliente = clienteRepo.findById(persona.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente Core no encontrado para id " + persona.getClienteId()
                ));

        ResumenClienteDTO dto = new ResumenClienteDTO();
        dto.setClienteId(persona.getClienteId());
        dto.setNombres(persona.getNombres() + " " + persona.getApellidos());
        dto.setCedula(persona.getNumeroIdentificacion());
        dto.setEstado(cliente.getEstado());
        dto.setCuentas(cuentaRepo.findResumenByClienteId(persona.getClienteId()));

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/operar")
    public ResponseEntity<String> operarCaja(@RequestBody TransaccionCajaRequest req) {
        try {
            if (req.getTipoOperacion() == null) {
                throw new IllegalArgumentException("El tipo de operación es obligatorio");
            }

            String ref;

            switch (req.getTipoOperacion()) {
                case "DEPOSITO" -> ref = service.procesarDeposito(
                        req.getCuentaOrigen(),
                        req.getMonto(),
                        req.getDescripcion()
                );
                case "RETIRO" -> ref = service.procesarRetiro(
                        req.getCuentaOrigen(),
                        req.getMonto(),
                        req.getDescripcion()
                );
                case "TRANSFERENCIA" -> {
                    TransaccionRequestDTO trf = new TransaccionRequestDTO();
                    trf.setCuentaOrigen(req.getCuentaOrigen());
                    trf.setCuentaDestino(req.getCuentaDestino());
                    trf.setMonto(req.getMonto());
                    trf.setDescripcion(req.getDescripcion());
                    ref = service.procesarTransferencia(trf);
                }
                default -> throw new IllegalArgumentException(
                        "Tipo de operación no soportado: " + req.getTipoOperacion()
                );
            }

            return ResponseEntity.ok(ref);

        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error procesando operación de ventanilla",
                    ex
            );
        }
    }
    
    @GetMapping("/info-cuenta/{numero}")
    public ResponseEntity<TitularCuentaDTO> infoCuenta(@PathVariable String numero) {
        try {
            TitularCuentaDTO titular = service.buscarTitularPorCuenta(numero);
            return ResponseEntity.ok(titular);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cuenta no encontrada con número " + numero,
                    ex
            );
        }
    }

    @PutMapping("/cuentas/{numero}/estado")
    public ResponseEntity<String> cambiarEstadoCuenta(
            @PathVariable String numero,
            @RequestParam String estado
    ) {
        return cuentaRepo.findByNumeroCuenta(numero)
                .map(c -> {
                    c.setEstado(estado);
                    cuentaRepo.save(c);
                    return ResponseEntity.ok("Estado de la cuenta actualizado a " + estado);
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cuenta no encontrada con número " + numero
                ));
    }

    @PostMapping("/clientes/estado")
    public ResponseEntity<String> cambiarEstadoCliente(
            @RequestParam String cedula,
            @RequestParam String estado
    ) {
        var persona = personaRepo.findByNumeroIdentificacion(cedula)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Persona no encontrada con cédula " + cedula
                ));

        var cliente = clienteRepo.findById(persona.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente no encontrado para id " + persona.getClienteId()
                ));

        cliente.setEstado(estado);
        clienteRepo.save(cliente);

        return ResponseEntity.ok("Cliente actualizado a " + estado);
    }

    @DeleteMapping("/cuentas/{numero}")
    @Transactional
    public ResponseEntity<String> eliminarCuenta(@PathVariable String numero) {
        var cuenta = cuentaRepo.findByNumeroCuenta(numero)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cuenta no encontrada con número " + numero
                ));

        transaccionRepo.deleteByCuenta_CuentaId(cuenta.getCuentaId());
        cuentaRepo.delete(cuenta);

        return ResponseEntity.ok("Cuenta y su historial eliminados permanentemente");
    }
}