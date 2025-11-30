//ubi: src/main/java/com/ecusol/core/model/Cliente.java
package com.ecusol.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "cliente")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "clienteid") // Minúscula
    private Integer clienteId;

    @Column(name = "tipocliente", nullable = false, length = 1) // Minúscula
    private String tipoCliente; 

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    @Column(name = "fecharegistro", nullable = false) // Minúscula
    private LocalDate fechaRegistro;

    public Cliente() {}
    public Cliente(Integer clienteId) { this.clienteId = clienteId; }
}