package com.ecusol.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cuenta")
@Getter
@Setter
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuentaid")
    private Integer cuentaId;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "clienteid", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipocuentaid", nullable = false)
    private TipoCuenta tipoCuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursalidapertura", nullable = false)
    private Sucursal sucursalApertura;

    @Column(name = "numerocuenta", unique = true, nullable = false, length = 20)
    private String numeroCuenta;

    @Column(name = "saldo", nullable = false)
    private BigDecimal saldo;

    @Column(name = "fechaapertura", nullable = false)
    private LocalDate fechaApertura;
    
    @Column(name = "estado", nullable = false)
    private String estado;
}