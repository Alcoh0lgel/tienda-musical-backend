package com.fullstack.Arriendo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Table(name = "arriendo")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Arriendo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer productoId;

    @Column(nullable = false)
    private Integer clienteId;

    @Column(nullable = false)
    private String nombreCliente;

    @Column(nullable = false)
    private String nombreProducto;

    @Column(unique = true, length = 20, nullable = true)
    private String numeroSerie;

    @Column(nullable = false)
    private String tipoInstrumento;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Integer precioArriendo;

    @Column(nullable = false)
    private Integer precioGarantia;

    @Column(nullable = false)
    private Integer precioEnvio;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaPrestacion;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaRegreso;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String direccionSucursal;
}
