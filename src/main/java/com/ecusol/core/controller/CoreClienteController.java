package com.ecusol.core.controller;

import com.ecusol.core.dto.RegistroClienteCoreDTO;
import com.ecusol.core.dto.CrearCuentaDTO;
import com.ecusol.core.repository.ClienteRepository;
import com.ecusol.core.service.CoreBancarioService;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/core/clientes")
public class CoreClienteController {

    @Autowired 
    private CoreBancarioService service;
    @Autowired 
    private ClienteRepository clienteRepo; 

    @PostMapping
    public ResponseEntity<Integer> crearPersona(@RequestBody RegistroClienteCoreDTO dto) {
        try {
            Integer id = service.crearClientePersona(dto);

            URI location = URI.create(String.format("/api/v1/core/clientes/%d", id));
            return ResponseEntity.created(location).body(id);

        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se pudo crear el cliente en el Core",
                    ex
            );
        }
    }
    
    @PostMapping("/{clienteId}/cuentas")
    public ResponseEntity<String> crearCuenta(
            @PathVariable Integer clienteId,
            @RequestBody CrearCuentaDTO dto
    ) {
        try {
            dto.setClienteId(clienteId);

            String numeroCuenta = service.crearCuenta(dto);

            URI location = URI.create(String.format(
                    "/api/v1/core/clientes/%d/cuentas/%s", clienteId, numeroCuenta
            ));

            return ResponseEntity.created(location).body(numeroCuenta);

        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se pudo crear la cuenta para el cliente " + clienteId,
                    ex
            );
        }
    }

    @GetMapping("/{id}/estado")
    public ResponseEntity<String> getEstado(@PathVariable Integer id) {
        return clienteRepo.findById(id)
                .map(c -> ResponseEntity.ok(c.getEstado()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con id " + id
                ));
    }
}