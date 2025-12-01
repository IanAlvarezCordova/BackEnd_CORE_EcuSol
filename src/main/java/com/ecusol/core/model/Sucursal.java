package com.ecusol.core.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "sucursal")
@Data
public class Sucursal {
    @Id 
    @Column(name = "sucursalid")
    private Integer sucursalId;
    
    @Column(name = "entidadId") 
    private Integer entidadId; 
    
    @Column(name = "ubicacionid")
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
    
    @Column(name = "codigosucursal")
    private String codigoSucursal;
    
    @Column(name = "estado")
    private String estado;
}