package com.lizardstore.ordenms.repositorio;

import com.lizardstore.ordenms.entidad.Orden;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepositorio extends JpaRepository<Orden, Long> {

    java.util.Optional<Orden> findByCarritoId(Long carritoId);

    List<Orden> findByUsuarioIdOrderByIdDesc(Long usuarioId);
}
