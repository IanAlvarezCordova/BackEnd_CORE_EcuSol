//ubi: src/main/java/com/ecusol/core/model/Empresa.java
package com.ecusol.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "empresa")
@PrimaryKeyJoinColumn(name = "clienteId")
@Getter
@Setter
public class Empresa extends Cliente {

    @Column(name = "ruc", unique = true, nullable = false, length = 13)
    private String ruc;

    @Column(name = "razonSocial", nullable = false, length = 150)
    private String razonSocial;

    @Column(name = "correoElectronico", nullable = false, length = 100)
    private String correoElectronico;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;
}