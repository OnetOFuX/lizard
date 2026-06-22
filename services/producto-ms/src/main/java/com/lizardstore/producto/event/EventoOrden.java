package com.lizardstore.producto.event;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoOrden {

    private String tipoEvento;
    private Long ordenId;
    private Long usuarioId;
    private Long carritoId;
    private Double total;
    private String estado;
    private String origen;
    private Long timestamp;
    private List<ItemEvento> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemEvento {
        private Long productoId;
        private Integer cantidad;
    }
}
