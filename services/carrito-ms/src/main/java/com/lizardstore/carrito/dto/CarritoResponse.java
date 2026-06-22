package com.lizardstore.carrito.dto;

import java.math.BigDecimal;
import java.util.List;
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
public class CarritoResponse {

    private Long id;
    private Long usuarioId;
    private String estado;
    private BigDecimal subtotal;
    private List<CarritoItemResponse> items;
}
