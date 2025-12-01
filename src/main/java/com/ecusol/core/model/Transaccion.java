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
    @JoinColumn(name = "cuentaId", nullable = false) 
    private Cuenta cuenta;

    @ManyToOne
    @JoinColumn(name = "cuentaContraparteId") 
    private Cuenta cuentaContraparte;

    @Column(name = "referencia", unique = true, nullable = false, length = 50)
    private String referencia;

    @Column(name = "rolTransaccion", nullable = false, length = 10) 
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