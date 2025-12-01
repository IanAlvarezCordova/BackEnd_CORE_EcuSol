package com.ecusol.core.repository;

import com.ecusol.core.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {
    
    List<Transaccion> findByCuenta_NumeroCuentaOrderByFechaEjecucionDesc(String numeroCuenta);
    
    void deleteByCuenta_CuentaId(Integer cuentaId);
}