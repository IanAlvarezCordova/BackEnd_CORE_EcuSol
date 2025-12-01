package com.ecusol.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tipocuenta")
@Getter
@Setter
public class TipoCuenta implements Serializable {

    @Id
    @Column(name = "tipocuentaid")
    private Integer tipoCuentaId;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    @Column(name = "tipoamortizacion", nullable = false, length = 20)
    private String tipoAmortizacion;

    public TipoCuenta() {}
    public TipoCuenta(Integer tipoCuentaId) { this.tipoCuentaId = tipoCuentaId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoCuenta that = (TipoCuenta) o;
        return Objects.equals(tipoCuentaId, that.tipoCuentaId);
    }

    @Override
    public int hashCode() { return Objects.hash(tipoCuentaId); }

    @Override
    public String toString() {
        return "TipoCuenta{tipoCuentaId=" + tipoCuentaId + ", nombre='" + nombre + "'}";
    }
}