package com.lizardstore.ordenms.evento;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoCarrito {

    private String tipoEvento;
    private Long carritoId;
    private Long usuarioId;
    private BigDecimal subtotal;
    private List<ItemEvento> items;
    private String origen;
    private Long timestamp;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemEvento {
        private Long productoId;
        private String nombreProducto;
        private BigDecimal precioUnitario;
        private Integer cantidad;
        private BigDecimal subtotalLinea;
    }
}
