//ubi: src/main/java/com/ecusol/core/repository/PersonaRepository.java
package com.ecusol.core.repository;

import com.ecusol.core.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    // El Core busca personas por su identificación oficial (Cédula), no por usuario web
    Optional<Persona> findByNumeroIdentificacion(String numeroIdentificacion);

    boolean existsByNumeroIdentificacion(String numeroIdentificacion);
}