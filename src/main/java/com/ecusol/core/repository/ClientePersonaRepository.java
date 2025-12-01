package com.ecusol.core.repository;

import com.ecusol.core.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientePersonaRepository extends JpaRepository<Cliente, Integer> {

}