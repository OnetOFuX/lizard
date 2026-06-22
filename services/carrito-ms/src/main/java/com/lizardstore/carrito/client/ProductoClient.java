package com.lizardstore.carrito.client;

import com.lizardstore.carrito.dto.ProductoSnapshotDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-ms")
public interface ProductoClient {

    @GetMapping("/api/v1/productos/{id}")
    ProductoSnapshotDto obtenerProducto(@PathVariable("id") Integer id);
}
