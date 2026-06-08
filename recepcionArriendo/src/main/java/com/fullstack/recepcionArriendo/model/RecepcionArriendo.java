package com.fullstack.recepcionArriendo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recepcionArriendo")
@Entity
public class RecepcionArriendo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer arriendoId;

    @Column(nullable = false)
    private String nombreCliente;

    @Column(nullable = false)
    private String nombreProducto;

    @Column(unique = true, length = 20, nullable = true)
    private String numeroSerie;

    @Column(nullable = false)
    private Integer precioGarantia;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaRecepcion;

    @Column(nullable = false)
    private String observacion;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = true)
    private Integer multa;
}
