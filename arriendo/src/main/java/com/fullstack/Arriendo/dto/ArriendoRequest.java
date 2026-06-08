package com.fullstack.Arriendo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ArriendoRequest {

    @NotNull(message = "La id del producto es obligatoria")
    @Column(nullable = false)
    private Integer productoId;

    @NotNull(message = "La id de cliente es obligatoria")
    @Column(nullable = false)
    private Integer clienteId;

    @NotBlank(message = "La direccion es obligatoria")
    @Column(nullable = false)
    private String direccion;

    @NotNull(message = "El precio no puede estar vacio")
    @Column(nullable = false)
    private Integer precioEnvio;

    @NotBlank(message = "La sucursal es obligatoria")
    @Column(nullable = false)
    private String direccionSucursal;

    @NotNull(message = "La fecha de la prestación es obligatoria")
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaPrestacion;

    @NotNull(message = "La fecha de regreso es obligatoria")
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaRegreso;
}
