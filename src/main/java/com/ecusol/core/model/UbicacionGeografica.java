package com.ecusol.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ubicaciongeografica")
@Getter
@Setter
public class UbicacionGeografica implements Serializable {

    @Id
    @Column(name = "ubicacionid")
    private Integer ubicacionId;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "tipoubicacion", nullable = false, length = 15)
    private String tipoUbicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacionpadreid")
    private UbicacionGeografica ubicacionPadre;

    public UbicacionGeografica() {}
    public UbicacionGeografica(Integer ubicacionId) { this.ubicacionId = ubicacionId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UbicacionGeografica that = (UbicacionGeografica) o;
        return Objects.equals(ubicacionId, that.ubicacionId);
    }

    @Override
    public int hashCode() { return Objects.hash(ubicacionId); }

    @Override
    public String toString() {
        return "UbicacionGeografica{" +
                "ubicacionId=" + ubicacionId +
                ", nombre='" + nombre + '\'' +
                ", tipoUbicacion='" + tipoUbicacion + '\'' +
                ", ubicacionPadre=" + (ubicacionPadre != null ? ubicacionPadre.getUbicacionId() : "null") +
                '}';
    }
}