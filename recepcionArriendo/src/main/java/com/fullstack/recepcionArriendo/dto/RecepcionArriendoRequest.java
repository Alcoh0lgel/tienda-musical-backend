package com.fullstack.recepcionArriendo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecepcionArriendoRequest {

    @NotNull(message = "El ID del arriendo es obligatorio")
    private Integer arriendoId;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaRecepcion;

    @NotBlank(message = "Debe indicar una observación de la recepción")
    private String observacion;

    @NotBlank(message = "El estado (RECIBIDO | OBSERVACION | LISTO |) no puede estar vacio")
    @Pattern(regexp = "(?i)RECIBIDO|OBSERVACION|LISTO", message = "Solo se permite 'RECIBIDO', 'OBSERVACION' y 'LISTO' .")
    private String estado;

    private Integer multa;
}
