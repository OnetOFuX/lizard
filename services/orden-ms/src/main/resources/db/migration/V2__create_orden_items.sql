CREATE TABLE orden_items (
    id              BIGSERIAL PRIMARY KEY,
    orden_id        BIGINT NOT NULL REFERENCES ordenes(id) ON DELETE CASCADE,
    producto_id     BIGINT NOT NULL,
    nombre_producto VARCHAR(100) NOT NULL,
    precio_unitario NUMERIC(12, 2) NOT NULL,
    cantidad        INT NOT NULL,
    subtotal_linea  NUMERIC(12, 2) NOT NULL
);

ALTER TABLE ordenes ADD COLUMN IF NOT EXISTS carrito_id BIGINT;
