package com.lizardstore.carrito.messaging;

import com.lizardstore.carrito.entity.Carrito;
import com.lizardstore.carrito.entity.CarritoItem;
import com.lizardstore.carrito.event.EventoProducto;
import com.lizardstore.carrito.repository.CarritoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductoEventConsumer {

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String EVENTO_ACTUALIZADO = "producto.actualizado";

    private final CarritoRepository carritoRepository;

    @KafkaListener(
            topics = "${app.kafka.topic.productos}",
            groupId = "${app.kafka.group-id.carrito-productos}",
            containerFactory = "productoListenerContainerFactory"
    )
    @Transactional
    public void consumirEventoProducto(EventoProducto evento) {
        if (evento == null || !EVENTO_ACTUALIZADO.equals(evento.getTipoEvento()) || evento.getProductoId() == null) {
            return;
        }

        if (Boolean.FALSE.equals(evento.getActivo()) || evento.getPrecio() == null) {
            log.info("service=carrito-ms productoId={} status=skipped-inactive-or-no-price", evento.getProductoId());
            return;
        }

        List<Carrito> carritos = carritoRepository.findAllByEstado(ESTADO_ACTIVO);
        int actualizados = 0;

        for (Carrito carrito : carritos) {
            boolean modificado = false;
            for (CarritoItem item : carrito.getItems()) {
                if (item.getProductoId().equals(evento.getProductoId().longValue())) {
                    item.setPrecioUnitario(evento.getPrecio());
                    item.setNombreProducto(evento.getNombre());
                    item.setSubtotalLinea(evento.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
                    modificado = true;
                }
            }
            if (modificado) {
                BigDecimal subtotal = carrito.getItems().stream()
                        .map(CarritoItem::getSubtotalLinea)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                carrito.setSubtotal(subtotal);
                carrito.setUpdatedAt(LocalDateTime.now());
                carritoRepository.save(carrito);
                actualizados++;
            }
        }

        log.info("service=carrito-ms productoId={} carritosActualizados={} status=price-synced",
                evento.getProductoId(), actualizados);
    }
}
