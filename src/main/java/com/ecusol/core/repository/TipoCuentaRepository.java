package com.ecusol.core.repository;

import com.ecusol.core.model.TipoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoCuentaRepository extends JpaRepository<TipoCuenta, Integer> {

}