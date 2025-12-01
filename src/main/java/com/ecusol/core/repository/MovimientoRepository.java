package com.ecusol.core.repository;

import com.ecusol.core.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuentaIdOrderByFechaDesc(Long cuentaId);

    void deleteByCuentaId(Long cuentaId);

    void deleteByTransaccionId(Long transaccionId);
}