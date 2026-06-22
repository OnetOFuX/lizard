package com.lizardstore.carrito.service;

import com.lizardstore.carrito.dto.ActualizarItemRequest;
import com.lizardstore.carrito.dto.AgregarItemRequest;
import com.lizardstore.carrito.dto.CarritoResponse;
import java.math.BigDecimal;

public interface CarritoService {

    CarritoResponse obtenerCarrito(Long usuarioId);

    CarritoResponse agregarItem(Long usuarioId, AgregarItemRequest request);

    CarritoResponse actualizarItem(Long usuarioId, Long productoId, ActualizarItemRequest request);

    CarritoResponse eliminarItem(Long usuarioId, Long productoId);

    void vaciarCarrito(Long usuarioId);

    BigDecimal calcularSubtotal(Long usuarioId);

    CarritoResponse checkout(Long usuarioId);
}
