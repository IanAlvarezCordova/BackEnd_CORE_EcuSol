//ubi: src/main/java/com/ecusol/core/service/CoreBancarioService.java
package com.ecusol.core.service;

import com.ecusol.core.dto.CrearCuentaDTO;
import com.ecusol.core.dto.RegistroClienteCoreDTO;
import com.ecusol.core.dto.TitularCuentaDTO;
import com.ecusol.core.dto.TransaccionRequestDTO;
import com.ecusol.core.model.Cliente;
import com.ecusol.core.model.Cuenta;
import com.ecusol.core.model.Persona;
import com.ecusol.core.model.Transaccion;
import com.ecusol.core.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoreBancarioService {

    @Autowired private CuentaRepository cuentaRepo;
    @Autowired private TransaccionRepository transaccionRepo;
    @Autowired private ClienteRepository clienteRepo;
    @Autowired private PersonaRepository personaRepo;
    @Autowired private SucursalRepository sucursalRepo;
    @Autowired private TipoCuentaRepository tipoCuentaRepo;

    // --- TRANSACCIONES ---
    @Transactional
    public String procesarTransferencia(TransaccionRequestDTO req) {
        Cuenta origen = cuentaRepo.findByNumeroCuenta(req.getCuentaOrigen())
                .orElseThrow(() -> new RuntimeException("Cuenta origen no existe"));
        Cuenta destino = cuentaRepo.findByNumeroCuenta(req.getCuentaDestino())
                .orElseThrow(() -> new RuntimeException("Cuenta destino no existe"));

        if (!"ACTIVA".equalsIgnoreCase(origen.getEstado()) && !"ACTIVO".equalsIgnoreCase(origen.getEstado()))
            throw new RuntimeException("Cuenta origen no activa");

        if (!"ACTIVA".equalsIgnoreCase(destino.getEstado()) && !"ACTIVO".equalsIgnoreCase(destino.getEstado()))
            throw new RuntimeException("Cuenta destino no activa");

        if (origen.getSaldo().compareTo(req.getMonto()) < 0)
            throw new RuntimeException("Saldo insuficiente");

        if (!"ACTIVO".equalsIgnoreCase(origen.getCliente().getEstado())) {
    throw new RuntimeException("CLIENTE BLOQUEADO: Comuníquese con el banco.");
}

        origen.setSaldo(origen.getSaldo().subtract(req.getMonto()));
        destino.setSaldo(destino.getSaldo().add(req.getMonto()));
        
        cuentaRepo.save(origen);
        cuentaRepo.save(destino);

        String ref = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        registrarTransaccion(origen, destino, req.getMonto(), "EMISOR", "TRF-" + ref + "-E", req.getDescripcion());
        registrarTransaccion(destino, origen, req.getMonto(), "RECEPTOR", "TRF-" + ref + "-R", req.getDescripcion());

        return ref;
    }

    @Transactional
    public String procesarDeposito(String cuentaDestino, BigDecimal monto, String desc) {
        Cuenta destino = cuentaRepo.findByNumeroCuenta(cuentaDestino)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (!"ACTIVA".equalsIgnoreCase(destino.getEstado())) throw new RuntimeException("Cuenta inactiva");

        destino.setSaldo(destino.getSaldo().add(monto));
        cuentaRepo.save(destino);

        String ref = "DEP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        registrarTransaccion(destino, null, monto, "RECEPTOR", ref, desc);
        return ref;
    }

    @Transactional
    public String procesarRetiro(String cuentaOrigen, BigDecimal monto, String desc) {
        Cuenta origen = cuentaRepo.findByNumeroCuenta(cuentaOrigen)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        if (!"ACTIVA".equalsIgnoreCase(origen.getEstado())) throw new RuntimeException("Cuenta inactiva");
        if (origen.getSaldo().compareTo(monto) < 0) throw new RuntimeException("Saldo insuficiente");
        origen.setSaldo(origen.getSaldo().subtract(monto));
        cuentaRepo.save(origen);
        String ref = "RET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        registrarTransaccion(origen, null, monto, "EMISOR", ref, desc);
        return ref;
    }

    private void registrarTransaccion(Cuenta principal, Cuenta contraparte, BigDecimal monto, String rol, String ref, String desc) {
        Transaccion t = new Transaccion();
        t.setCuenta(principal);
        t.setCuentaContraparte(contraparte);
        t.setMonto(monto);
        t.setRolTransaccion(rol);
        t.setReferencia(ref);
        t.setDescripcion(desc);
        t.setEstado("COMPLETADA");
        t.setFechaEjecucion(LocalDateTime.now());
        transaccionRepo.save(t);
    }

    // --- CLIENTES ---
    @Transactional
    public Integer crearClientePersona(RegistroClienteCoreDTO dto) {
        if (personaRepo.findByNumeroIdentificacion(dto.getCedula()).isPresent()) {
            throw new RuntimeException("Cédula ya registrada en el Core");
        }
        Persona persona = new Persona();
        persona.setTipoCliente("P");
        persona.setEstado("ACTIVO");
        persona.setFechaRegistro(java.time.LocalDate.now());
        persona.setNombres(dto.getNombres());
        persona.setApellidos(dto.getApellidos());
        persona.setNumeroIdentificacion(dto.getCedula());
        persona.setTipoIdentificacion("CEDULA");
        persona.setDireccion(dto.getDireccion());
        persona.setFechaNacimiento(dto.getFechaNacimiento() != null ? dto.getFechaNacimiento() : java.time.LocalDate.now());

        Persona personaGuardada = personaRepo.save(persona);
        return personaGuardada.getClienteId();
    }

    @Transactional
    public String crearCuenta(CrearCuentaDTO dto) {
        Cuenta c = new Cuenta();
        c.setCliente(clienteRepo.getReferenceById(dto.getClienteId()));
        c.setTipoCuenta(tipoCuentaRepo.getReferenceById(dto.getTipoCuentaId()));
        c.setSucursalApertura(sucursalRepo.getReferenceById(1)); 
        String num = "10" + String.format("%08d", new java.util.Random().nextInt(99999999));
        c.setNumeroCuenta(num);
        c.setSaldo(dto.getSaldoInicial() != null ? dto.getSaldoInicial() : BigDecimal.ZERO);
        c.setFechaApertura(java.time.LocalDate.now());
        c.setEstado("INACTIVA"); 
        cuentaRepo.save(c);
        return num;
    }

    // --- BÚSQUEDA DE TITULAR CORREGIDA ---
    @Transactional(readOnly = true) 
    public TitularCuentaDTO buscarTitularPorCuenta(String numeroCuenta) {
        Cuenta c = cuentaRepo.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        String nombre = "Nombre no disponible";
        String ident = "9999999999";

        // Obtenemos el ID directamente para evitar problemas con proxies
        Integer clienteId = c.getCliente().getClienteId();

        // Búsqueda explícita en Persona
        Optional<Persona> personaOpt = personaRepo.findById(clienteId);
        
        if (personaOpt.isPresent()) {
            Persona p = personaOpt.get();
            // Aseguramos que no sean nulos
            String nombres = p.getNombres() != null ? p.getNombres() : "";
            String apellidos = p.getApellidos() != null ? p.getApellidos() : "";
            nombre = (nombres + " " + apellidos).trim();
            ident = p.getNumeroIdentificacion();
        } else {
            // Si no es persona, podría ser empresa (lógica futura)
            nombre = "CLIENTE EMPRESARIAL";
        }

        // Enmascarar
        String identMask = ident != null && ident.length() > 4 ? "***" + ident.substring(ident.length() - 4) : "***";
        
        // Tipo de Cuenta
        String tipoCuenta = c.getTipoCuenta() != null ? c.getTipoCuenta().getNombre() : "Cuenta";

        return new TitularCuentaDTO(c.getNumeroCuenta(), nombre, identMask, tipoCuenta);
    }
}