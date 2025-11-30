// repositorio/SucursalRepository.java
package com.ecusol.core.repository;

import com.ecusol.core.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// CAMBIO CRÍTICO: <Sucursal, Integer> en vez de <Sucursal, Long>
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {
}