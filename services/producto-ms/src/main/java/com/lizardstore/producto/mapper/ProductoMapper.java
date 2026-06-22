package com.lizardstore.producto.mapper;

import com.lizardstore.producto.dto.ProductoRequest;
import com.lizardstore.producto.dto.ProductoResponse;
import com.lizardstore.producto.entity.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequest request) {
        if (request == null) {
            return null;
        }

        return Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .idCategoria(request.getIdCategoria())
                .sku(request.getSku())
                .precio(request.getPrecio())
                .stock(request.getStock())
                .activo(request.getActivo() != null ? request.getActivo() : Boolean.TRUE)
                .build();
    }

    public ProductoResponse toResponse(Producto entity) {
        if (entity == null) {
            return null;
        }

        return ProductoResponse.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .idCategoria(entity.getIdCategoria())
                .sku(entity.getSku())
                .precio(entity.getPrecio())
                .stock(entity.getStock())
                .activo(entity.getActivo())
                .hasImagen(entity.getImagen() != null)
                .build();
    }

    public void updateEntityFromRequest(Producto entity, ProductoRequest request) {
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setIdCategoria(request.getIdCategoria());
        entity.setSku(request.getSku());
        entity.setPrecio(request.getPrecio());
        entity.setStock(request.getStock());
        if (request.getActivo() != null) {
            entity.setActivo(request.getActivo());
        }
    }
}
