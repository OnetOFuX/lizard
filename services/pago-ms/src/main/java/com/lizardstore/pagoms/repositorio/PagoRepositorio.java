package com.lizardstore.pagoms.repositorio;

import com.lizardstore.pagoms.entidad.Pago;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepositorio extends JpaRepository<Pago, Long> {

    Optional<Pago> findTopByOrdenIdOrderByIdDesc(Long ordenId);
}
