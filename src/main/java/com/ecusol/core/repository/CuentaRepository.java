//ubi: src/main/java/com/ecusol/core/repository/CuentaRepository.java
package com.ecusol.core.repository;

import com.ecusol.core.dto.CuentaResumenDTO;
import com.ecusol.core.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {
    
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    // CONSULTA MAESTRA: Evita el problema N+1 y los logs gigantes.
    // Selecciona datos puntuales y los mete en el DTO directamente.
    @Query("SELECT new com.ecusol.core.dto.CuentaResumenDTO(c.cuentaId, c.numeroCuenta, c.saldo, c.estado, c.tipoCuenta.tipoCuentaId) " +
           "FROM Cuenta c WHERE c.cliente.clienteId = :clienteId")
    List<CuentaResumenDTO> findResumenByClienteId(Integer clienteId);
}