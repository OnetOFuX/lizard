package com.lizardstore.carrito.repository;

import com.lizardstore.carrito.entity.Carrito;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    @Query("SELECT c FROM Carrito c LEFT JOIN FETCH c.items WHERE c.usuarioId = :usuarioId AND c.estado = :estado")
    Optional<Carrito> findByUsuarioIdAndEstadoWithItems(
            @Param("usuarioId") Long usuarioId,
            @Param("estado") String estado);

    @Query("SELECT DISTINCT c FROM Carrito c LEFT JOIN FETCH c.items WHERE c.estado = :estado")
    List<Carrito> findAllByEstado(@Param("estado") String estado);
}
