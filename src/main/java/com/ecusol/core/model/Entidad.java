//ubi: src/main/java/com/ecusol/core/model/Entidad.java
package com.ecusol.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "entidad")
@Getter
@Setter
public class Entidad implements Serializable {

    @Id
    @Column(name = "entidadId")
    private Integer entidadId;

    @Column(name = "ruc", unique = true, nullable = false, length = 13)
    private String ruc;

    @Column(name = "razonSocial", nullable = false, length = 100)
    private String razonSocial;

    @Column(name = "nombreComercial", nullable = false, length = 100)
    private String nombreComercial;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    public Entidad() {}
    public Entidad(Integer entidadId) { this.entidadId = entidadId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entidad entidad = (Entidad) o;
        return Objects.equals(entidadId, entidad.entidadId);
    }

    @Override
    public int hashCode() { return Objects.hash(entidadId); }

    @Override
    public String toString() {
        return "Entidad{" +
                "entidadId=" + entidadId +
                ", ruc='" + ruc + '\'' +
                ", nombreComercial='" + nombreComercial + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}