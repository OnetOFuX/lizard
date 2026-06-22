package com.lizardstore.producto.service;

import com.lizardstore.producto.dto.ProductoRequest;
import com.lizardstore.producto.dto.ProductoResponse;

import java.util.List;

public interface ProductoService {

    ProductoResponse create(ProductoRequest request);

    List<ProductoResponse> findAll();

    ProductoResponse findById(Integer id);

    ProductoResponse update(Integer id, ProductoRequest request);

    void delete(Integer id);

    ProductoResponse findDetalleById(Integer id);

    void uploadImage(Integer id, org.springframework.web.multipart.MultipartFile file);

    com.lizardstore.producto.entity.Producto findByIdEntity(Integer id);

    void descontarStock(Integer productoId, int cantidad);
}
