package com.lizardstore.carrito.event;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoProducto {

    private String tipoEvento;
    private Integer productoId;
    private String sku;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private Integer categoriaId;
    private Boolean activo;
    private String origen;
    private Long timestamp;
}
