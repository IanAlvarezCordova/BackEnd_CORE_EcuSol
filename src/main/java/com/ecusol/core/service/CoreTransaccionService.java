//ubi: src/main/java/com/ecusol/core/service/CoreTransaccionService.java
package com.ecusol.core.service;

import com.ecusol.core.model.Cuenta;
import com.ecusol.core.model.Transaccion;
import com.ecusol.core.repository.CuentaRepository;
import com.ecusol.core.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CoreTransaccionService {

    @Autowired private CuentaRepository cuentaRepo;
    @Autowired private TransaccionRepository transaccionRepo;

    @Transactional
    public String ejecutarTransferencia(String cuentaOrigen, String cuentaDestino, BigDecimal monto, String descripcion) {
        // 1. Validar Cuentas
        Cuenta origen = cuentaRepo.findByNumeroCuenta(cuentaOrigen)
                .orElseThrow(() -> new RuntimeException("Cuenta origen no encontrada"));
        Cuenta destino = cuentaRepo.findByNumeroCuenta(cuentaDestino)
                .orElseThrow(() -> new RuntimeException("Cuenta destino no encontrada"));

        // 2. Validar Estados y Saldos
        // Nota: Usamos "ACTIVO" como estándar unificado (o ajusta según tu BD si usas "ACTIVA")
        // Para ser robustos, aceptamos ambos si es necesario, o nos ceñimos al estándar nuevo.
        if (!"ACTIVO".equals(origen.getEstado()) && !"ACTIVA".equals(origen.getEstado()))
            throw new RuntimeException("Cuenta origen inactiva");

        if (!"ACTIVO".equals(destino.getEstado()) && !"ACTIVA".equals(destino.getEstado()))
            throw new RuntimeException("Cuenta destino inactiva");

        if (origen.getSaldo().compareTo(monto) < 0)
            throw new RuntimeException("Saldo insuficiente");

        // 3. Ejecutar Débito (Origen)
        origen.setSaldo(origen.getSaldo().subtract(monto));
        cuentaRepo.save(origen);

        // 4. Ejecutar Crédito (Destino)
        destino.setSaldo(destino.getSaldo().add(monto));
        cuentaRepo.save(destino);

        // 5. Registrar Transacciones
        String referencia = UUID.randomUUID().toString();
        registrarTransaccion(origen, destino, monto, "EMISOR", referencia, descripcion);
        registrarTransaccion(destino, origen, monto, "RECEPTOR", referencia, descripcion);

        return referencia;
    }

    private void registrarTransaccion(Cuenta principal, Cuenta contraparte, BigDecimal monto, String rol, String ref, String desc) {
        Transaccion t = new Transaccion();
        t.setCuenta(principal);
        t.setCuentaContraparte(contraparte);
        t.setMonto(monto);
        t.setRolTransaccion(rol);
        t.setReferencia(ref + "-" + rol.substring(0,1));
        t.setDescripcion(desc);
        t.setEstado("COMPLETADA");
        t.setFechaEjecucion(LocalDateTime.now());
        transaccionRepo.save(t);
    }

    
}