//ubi: src/main/java/com/ecusol/core/repository/ClienteRepository.java  
package com.ecusol.core.repository;

import com.ecusol.core.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}