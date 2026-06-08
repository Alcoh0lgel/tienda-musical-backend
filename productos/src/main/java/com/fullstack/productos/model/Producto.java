package com.fullstack.productos.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Table(name = "productos")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 20, nullable = true)
    private String numeroSerie;

    @Column(nullable = false)
    private String nombreProducto;

    @Column(nullable = false)
    private String tipoInstrumento;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Integer precioInstrumento;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaRegistro;

}