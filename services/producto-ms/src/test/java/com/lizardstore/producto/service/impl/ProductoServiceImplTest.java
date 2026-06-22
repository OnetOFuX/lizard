package com.lizardstore.producto.service.impl;

import com.lizardstore.producto.dto.ProductoRequest;
import com.lizardstore.producto.dto.ProductoResponse;
import com.lizardstore.producto.entity.Producto;
import com.lizardstore.producto.event.EventoProducto;
import com.lizardstore.producto.exception.ResourceNotFoundException;
import com.lizardstore.producto.mapper.ProductoMapper;
import com.lizardstore.producto.messaging.ProductoEventProducer;
import com.lizardstore.producto.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoEventProducer eventProducer;

    @Spy
    private ProductoMapper productoMapper = new ProductoMapper();

    @InjectMocks
    private ProductoServiceImpl productoService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(productoService, "applicationName", "producto-ms");
    }

    @Test
    void shouldCreateProductoAndPublishEvent() {
        ProductoRequest request = ProductoRequest.builder()
                .nombre("Laptop")
                .descripcion("Portatil de oficina")
                .idCategoria(3)
                .sku("LZ-LAP-001")
                .precio(new BigDecimal("999.99"))
                .stock(10)
                .build();
        Producto savedEntity = Producto.builder()
                .id(1)
                .nombre("Laptop")
                .descripcion("Portatil de oficina")
                .idCategoria(3)
                .sku("LZ-LAP-001")
                .precio(new BigDecimal("999.99"))
                .stock(10)
                .activo(true)
                .build();

        when(productoRepository.save(any(Producto.class))).thenReturn(savedEntity);

        ProductoResponse response = productoService.create(request);

        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getSku()).isEqualTo("LZ-LAP-001");

        ArgumentCaptor<EventoProducto> captor = ArgumentCaptor.forClass(EventoProducto.class);
        verify(eventProducer).publicar(captor.capture());
        assertThat(captor.getValue().getTipoEvento()).isEqualTo("producto.creado");
        assertThat(captor.getValue().getProductoId()).isEqualTo(1);
    }

    @Test
    void shouldThrowWhenProductoNotFound() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.findById(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}
