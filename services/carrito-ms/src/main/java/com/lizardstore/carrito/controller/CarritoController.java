package com.lizardstore.carrito.controller;

import com.lizardstore.carrito.dto.ActualizarItemRequest;
import com.lizardstore.carrito.dto.AgregarItemRequest;
import com.lizardstore.carrito.dto.CarritoResponse;
import com.lizardstore.carrito.service.CarritoService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carritos/usuario")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<CarritoResponse> obtenerCarrito(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(usuarioId));
    }

    @PostMapping("/{usuarioId}/items")
    public ResponseEntity<CarritoResponse> agregarItem(
            @PathVariable Long usuarioId,
            @Valid @RequestBody AgregarItemRequest request) {
        return ResponseEntity.ok(carritoService.agregarItem(usuarioId, request));
    }

    @PutMapping("/{usuarioId}/items/{productoId}")
    public ResponseEntity<CarritoResponse> actualizarItem(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId,
            @Valid @RequestBody ActualizarItemRequest request) {
        return ResponseEntity.ok(carritoService.actualizarItem(usuarioId, productoId, request));
    }

    @DeleteMapping("/{usuarioId}/items/{productoId}")
    public ResponseEntity<CarritoResponse> eliminarItem(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {
        return ResponseEntity.ok(carritoService.eliminarItem(usuarioId, productoId));
    }

    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
    }

    @GetMapping("/{usuarioId}/subtotal")
    public ResponseEntity<Map<String, BigDecimal>> calcularSubtotal(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(Map.of("subtotal", carritoService.calcularSubtotal(usuarioId)));
    }

    @PostMapping("/{usuarioId}/checkout")
    public ResponseEntity<CarritoResponse> checkout(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.checkout(usuarioId));
    }
}
