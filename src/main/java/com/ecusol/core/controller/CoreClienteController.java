package com.ecusol.core.controller;

import com.ecusol.core.dto.RegistroClienteCoreDTO;
import com.ecusol.core.dto.CrearCuentaDTO;
import com.ecusol.core.repository.ClienteRepository;
import com.ecusol.core.service.CoreBancarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/core/clientes")
public class CoreClienteController {

    @Autowired private CoreBancarioService service;
    @Autowired private ClienteRepository clienteRepo; 

    @PostMapping("/crear-persona")
    public ResponseEntity<Integer> crearPersona(@RequestBody RegistroClienteCoreDTO dto) {
        Integer id = service.crearClientePersona(dto);
        return ResponseEntity.ok(id);
    }
    
    @PostMapping("/crear-cuenta")
    public ResponseEntity<String> crearCuenta(@RequestBody CrearCuentaDTO dto) {
        String numero = service.crearCuenta(dto);
        return ResponseEntity.ok(numero);
    }

    // --- EL ENDPOINT QUE FALTABA ---
    @GetMapping("/{id}/estado-simple")
    public ResponseEntity<String> getEstadoSimple(@PathVariable Integer id) {
        return clienteRepo.findById(id)
                .map(c -> ResponseEntity.ok(c.getEstado())) // Devuelve "ACTIVO"
                .orElse(ResponseEntity.notFound().build());
    }
}