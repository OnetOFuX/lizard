package com.lizardstore.carrito.mapper;

import com.lizardstore.carrito.dto.CarritoItemResponse;
import com.lizardstore.carrito.dto.CarritoResponse;
import com.lizardstore.carrito.entity.Carrito;
import com.lizardstore.carrito.entity.CarritoItem;
import com.lizardstore.carrito.event.EventoCarrito;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CarritoMapper {

    public CarritoResponse toResponse(Carrito carrito) {
        List<CarritoItemResponse> items = carrito.getItems().stream()
                .map(this::toItemResponse)
                .toList();

        return CarritoResponse.builder()
                .id(carrito.getId())
                .usuarioId(carrito.getUsuarioId())
                .estado(carrito.getEstado())
                .subtotal(carrito.getSubtotal())
                .items(items)
                .build();
    }

    public CarritoItemResponse toItemResponse(CarritoItem item) {
        return CarritoItemResponse.builder()
                .productoId(item.getProductoId())
                .nombreProducto(item.getNombreProducto())
                .precioUnitario(item.getPrecioUnitario())
                .cantidad(item.getCantidad())
                .subtotalLinea(item.getSubtotalLinea())
                .build();
    }

    public List<EventoCarrito.ItemEvento> toItemEventos(Carrito carrito) {
        return carrito.getItems().stream()
                .map(item -> EventoCarrito.ItemEvento.builder()
                        .productoId(item.getProductoId())
                        .nombreProducto(item.getNombreProducto())
                        .precioUnitario(item.getPrecioUnitario())
                        .cantidad(item.getCantidad())
                        .subtotalLinea(item.getSubtotalLinea())
                        .build())
                .toList();
    }
}
