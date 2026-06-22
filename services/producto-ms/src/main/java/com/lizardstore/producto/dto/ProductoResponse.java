package com.lizardstore.producto.dto;

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
public class ProductoResponse {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer idCategoria;
    private String sku;
    private BigDecimal precio;
    private Integer stock;
    private Boolean activo;
    private Boolean hasImagen;
    private CategoriaDto categoria;
}
