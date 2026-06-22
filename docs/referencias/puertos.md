# Puertos y accesos

## DEV Maven

| Componente | URL |
|---|---|
| Config Server | `http://localhost:18888` |
| Eureka | `http://localhost:18761` |
| Gateway health | `http://localhost:18080/actuator/health` |
| Angular | `http://localhost:4200` |
| Kafka UI | `http://localhost:41085` |

## PROD Docker

| Componente | URL |
|---|---|
| Config Server | `http://localhost:28888` |
| Eureka | `http://localhost:28761` |
| Gateway health | `http://localhost:28082/actuator/health` |
| Kafka UI | `http://localhost:28085` |

## PostgreSQL DEV

| Servicio | Puerto | DB |
|---|---:|---|
| auth-ms | 15431 | `lizard_auth_db` |
| catalogo-ms | 15432 | `lizard_catalogo_db` |
| producto-ms | 15433 | `lizard_producto_db` |
| orden-ms | 15434 | `lizard_orden_db` |
| pago-ms | 15435 | `lizard_pago_db` |

Credenciales: `ecom` / `ecom`.

## PostgreSQL PROD

| Servicio | Puerto | DB |
|---|---:|---|
| auth-ms | 25431 | `lizard_auth_db` |
| catalogo-ms | 25432 | `lizard_catalogo_db` |
| producto-ms | 25433 | `lizard_producto_db` |
| orden-ms | 25434 | `lizard_orden_db` |
| pago-ms | 25435 | `lizard_pago_db` |

## Config por servicio

DEV:

```text
http://localhost:18888/producto-ms/dev
```

PROD:

```text
http://localhost:28888/producto-ms/prod
```
