CREATE TABLE carritos (
    id          BIGSERIAL PRIMARY KEY,
    usuario_id  BIGINT NOT NULL,
    estado      VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    subtotal    NUMERIC(12, 2) NOT NULL DEFAULT 0,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_carritos_usuario_estado ON carritos (usuario_id, estado);

CREATE TABLE carrito_items (
    id                BIGSERIAL PRIMARY KEY,
    carrito_id        BIGINT NOT NULL REFERENCES carritos(id) ON DELETE CASCADE,
    producto_id       BIGINT NOT NULL,
    nombre_producto   VARCHAR(100) NOT NULL,
    precio_unitario   NUMERIC(12, 2) NOT NULL,
    cantidad          INT NOT NULL CHECK (cantidad > 0),
    subtotal_linea    NUMERIC(12, 2) NOT NULL,
    UNIQUE (carrito_id, producto_id)
);
