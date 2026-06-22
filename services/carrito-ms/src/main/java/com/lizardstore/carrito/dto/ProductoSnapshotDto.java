package com.lizardstore.carrito.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoSnapshotDto {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer idCategoria;
    private BigDecimal precio;
    private Integer stock;
    private Boolean activo;
}
