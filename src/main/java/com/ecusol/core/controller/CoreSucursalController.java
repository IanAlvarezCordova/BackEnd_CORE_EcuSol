package com.ecusol.core.controller;

import com.ecusol.core.dto.SucursalDTO;
import com.ecusol.core.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/core/sucursales")
public class CoreSucursalController {

    @Autowired private SucursalRepository sucursalRepo;

    @GetMapping
    public List<SucursalDTO> listarTodo() {
        return sucursalRepo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // --- NUEVO ENDPOINT: BUSCAR POR ID ---
    @GetMapping("/{id}")
    public ResponseEntity<SucursalDTO> obtenerPorId(@PathVariable Integer id) {
        return sucursalRepo.findById(id)
                .map(s -> ResponseEntity.ok(mapToDTO(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    private SucursalDTO mapToDTO(com.ecusol.core.model.Sucursal s) {
        SucursalDTO dto = new SucursalDTO();
        dto.setId(s.getSucursalId());
        dto.setNombre(s.getNombre());
        dto.setDireccion(s.getDireccion());
        dto.setTelefono(s.getTelefono());
        dto.setLat(s.getLatitud());
        dto.setLng(s.getLongitud());
        return dto;
    }
}