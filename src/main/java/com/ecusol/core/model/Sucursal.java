package com.ecusol.core.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "sucursal")
@Data
public class Sucursal {
    @Id 
    @Column(name = "sucursalId")
    private Integer sucursalId;
    
    // Evita traer la Entidad Bancaria completa cada vez
    @Column(name = "entidadId") 
    private Integer entidadId; 
    
    @Column(name = "ubicacionId")
    private Integer ubicacionId;
    
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "latitud")
    private BigDecimal latitud;

    @Column(name = "longitud")
    private BigDecimal longitud;
    
    @Column(name = "codigoSucursal")
    private String codigoSucursal;
    
    @Column(name = "estado")
    private String estado;
}