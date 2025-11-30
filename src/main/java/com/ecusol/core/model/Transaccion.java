//ubi: src/main/java/com/ecusol/core/model/Transaccion.java
package com.ecusol.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaccion")
@Getter
@Setter
public class Transaccion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaccionid")
    private Integer transaccionId;

    @ManyToOne
    @JoinColumn(name = "cuentaid", nullable = false) // FK a la cuenta principal
    private Cuenta cuenta;

    @ManyToOne
    @JoinColumn(name = "cuentacontraparteid") // FK a la otra cuenta (puede ser null)
    private Cuenta cuentaContraparte;

    @Column(name = "referencia", unique = true, nullable = false, length = 50)
    private String referencia;

    @Column(name = "roltransaccion", nullable = false, length = 10) // EMISOR o RECEPTOR
    private String rolTransaccion;

    @Column(name = "monto", nullable = false, precision = 18, scale = 2)
    private BigDecimal monto;

    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    @Column(name = "fechaejecucion", nullable = false)
    private LocalDateTime fechaEjecucion;
}