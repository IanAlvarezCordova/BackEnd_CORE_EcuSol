// src/main/java/com/ecusol/core/controller/CoreTransaccionController.java USA VENTANILLA
package com.ecusol.core.controller;

import com.ecusol.core.dto.TransferenciaRequestDTO;
import com.ecusol.core.service.CoreTransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/core/transacciones")
public class CoreTransaccionController {

    @Autowired private CoreTransaccionService service;

    @PostMapping("/transferencia")
    public ResponseEntity<String> realizarTransferencia(@RequestBody TransferenciaRequestDTO req) {
        String ref = service.ejecutarTransferencia(req.getCuentaOrigen(), req.getCuentaDestino(), req.getMonto(), req.getDescripcion());
        return ResponseEntity.ok(ref);
    }
}