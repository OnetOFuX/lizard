package com.lizardstore.producto.mapper;

import com.lizardstore.producto.dto.ProductoRequest;
import com.lizardstore.producto.dto.ProductoResponse;
import com.lizardstore.producto.entity.Producto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductoMapperTest {

    private final ProductoMapper productoMapper = new ProductoMapper();

    @Test
    void shouldMapRequestToEntity() {
        ProductoRequest request = ProductoRequest.builder()
            .nombre("Laptop")
            .descripcion("Portatil de oficina")
            .idCategoria(3)
            .sku("LZ-LAP-001")
            .precio(new BigDecimal("999.99"))
            .stock(10)
            .activo(true)
                .build();

        Producto entity = productoMapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getNombre()).isEqualTo("Laptop");
        assertThat(entity.getSku()).isEqualTo("LZ-LAP-001");
        assertThat(entity.getPrecio()).isEqualByComparingTo("999.99");
        assertThat(entity.getStock()).isEqualTo(10);
        assertThat(entity.getActivo()).isTrue();
    }

    @Test
    void shouldMapEntityToResponse() {
        Producto entity = Producto.builder()
            .id(1)
            .nombre("Mouse")
            .descripcion("Periferico")
            .idCategoria(4)
            .sku("LZ-MOU-001")
            .precio(new BigDecimal("29.99"))
            .stock(50)
            .activo(true)
                .build();

        ProductoResponse response = productoMapper.toResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getSku()).isEqualTo("LZ-MOU-001");
        assertThat(response.getPrecio()).isEqualByComparingTo("29.99");
        assertThat(response.getStock()).isEqualTo(50);
    }

    @Test
    void shouldUpdateEntityFromRequest() {
        Producto entity = Producto.builder()
            .id(1)
                .nombre("Anterior")
                .descripcion("Anterior descripcion")
            .idCategoria(1)
            .sku("OLD-SKU")
            .precio(new BigDecimal("10.00"))
            .stock(1)
            .activo(true)
                .build();
        ProductoRequest request = ProductoRequest.builder()
                .nombre("Nueva")
                .descripcion("Nueva descripcion")
            .idCategoria(2)
            .sku("NEW-SKU")
            .precio(new BigDecimal("15.50"))
            .stock(5)
            .activo(false)
                .build();

        productoMapper.updateEntityFromRequest(entity, request);

        assertThat(entity.getNombre()).isEqualTo("Nueva");
        assertThat(entity.getSku()).isEqualTo("NEW-SKU");
        assertThat(entity.getPrecio()).isEqualByComparingTo("15.50");
        assertThat(entity.getActivo()).isFalse();
    }
}
