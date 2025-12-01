package com.ecusol.core.controller;

import com.ecusol.core.dto.TransferenciaRequestDTO;
import com.ecusol.core.service.CoreTransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/core/transacciones")
public class CoreTransaccionController {

    @Autowired 
    private CoreTransaccionService service;

    @PostMapping("/transferencia")
    public ResponseEntity<String> realizarTransferencia(@RequestBody TransferenciaRequestDTO req) {
        try {
            String referencia = service.ejecutarTransferencia(
                    req.getCuentaOrigen(),
                    req.getCuentaDestino(),
                    req.getMonto(),
                    req.getDescripcion()
            );

            return ResponseEntity.ok(referencia);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error procesando la transferencia",
                    e
            );
        }
    }
}