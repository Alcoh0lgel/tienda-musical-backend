package com.fullstack.productoArriendo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoArriendoRequest {

    @Size(min = 5, max = 20, message = "El número de serie debe tener entre 5 a 20 digitos.")
    private String numeroSerie;

    @NotBlank(message = "El nombre del producto es obligatorio.")
    @Size(max = 100, message = "El nombre del producto no debe superar los 100 caracteres.")
    private String nombreProducto;

    @NotBlank(message = "El tipo de instrumento es obligatorio.")
    @Size(max = 100, message = "El tipo de instrumento no debe superar los 100 caracteres.")
    private String tipoInstrumento;

    @NotBlank(message = "La descripción es obligatoria.")
    @Size(max = 1000, message = "La descripción del producto no debe superar los 1000 caracteres.")
    private String descripcion;

    @NotNull(message = "El precio del arriendo es obligatorio.")
    private Integer precioArriendo;

    @NotNull(message = "El precio de garantía es obligatorio.")
    private Integer precioGarantia;

    @NotBlank(message = "Ingresar el estado es obligatorio.")
    @Pattern(regexp = "(?i)DISPONIBLE|ARRENDADO|TALLER", message = "Estado no valido, USE: DISPONIBLE, ARRENDADO o TALLER.")
    private String estado;

    @NotNull(message = "La fecha es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaRegistro;
}
