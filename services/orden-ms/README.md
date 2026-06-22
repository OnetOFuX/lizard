# orden-ms

Microservicio de órdenes. Publica eventos `orden.creada` en Kafka.

## Puertos

| Recurso | DEV | PROD Docker |
|---|---:|---:|
| App | dinámico (`server.port: 0`) | sin puerto host (vía Gateway) |
| PostgreSQL | 15434 | 25434 -> 5432 |
| Kafka | 41092 | 29092 |

## DEV (Maven)

```bash
cd kafka && docker compose -f compose-dev.yml up -d
cd ../services/orden-ms
docker compose -f compose-dev.yml up -d
mvn spring-boot:run
```

Para levantar una segunda instancia, abre otra terminal en `services/orden-ms` y ejecuta:

```bash
mvn spring-boot:run
```

Links DEV:
- Config DEV: http://localhost:18888/orden-ms/dev
- Eureka DEV: http://localhost:18761
- Gateway DEV health: http://localhost:18080/actuator/health
- Base de datos DEV: `localhost:15434`

## PROD (Docker)

```bash
cd services/orden-ms
docker compose up -d --build
```

Links PROD:
- Config PROD: http://localhost:28888/orden-ms/prod
- Eureka PROD: http://localhost:28761
- Gateway PROD health: http://localhost:28082/actuator/health
- Base de datos PROD: `localhost:25434`

## Ver la BD desde un IDE

| Campo | Valor |
|---|---|
| Motor | PostgreSQL |
| Host | `localhost` |
| Puerto | `25434` |
| Database | `lizard_orden_db` |
| User | `ecom` |
| Password | `ecom` |

## Ver la BD desde PowerShell

Comandos para inspeccionar la BD DEV sin abrir un IDE:

```powershell
docker exec -it lizard-postgres-orden-dev psql -U ecom -d lizard_orden_db

docker exec -it lizard-postgres-orden-dev psql -U ecom -d lizard_orden_db -c "\dt"
docker exec -it lizard-postgres-orden-dev psql -U ecom -d lizard_orden_db -c "\d ordenes"
docker exec -it lizard-postgres-orden-dev psql -U ecom -d lizard_orden_db -c "SELECT * FROM ordenes;"
```

En PROD usa el contenedor `lizard-postgres-orden`.

## Eventos

- Publica: `lizard.orden.eventos`
- Consume: `lizard.pago.eventos`

## Prueba rapida con PowerShell

Este flujo permite crear una orden, publicar el evento `orden.creada` y listar las ordenes registradas.

```powershell
$body = @{
  usuarioId = 1
  total = 159.90
} | ConvertTo-Json

$orden = Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:18080/api/v1/ordenes" `
  -ContentType "application/json" `
  -Body $body

$orden

Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:18080/api/v1/ordenes"
```

## Prueba rapida con bash macOS/Linux

Este flujo permite crear una orden, publicar el evento `orden.creada` y listar las ordenes registradas usando `curl`.

```bash
curl -s -X POST "http://localhost:18080/api/v1/ordenes" \
  -H "Content-Type: application/json" \
  -d '{"usuarioId":1,"total":159.90}'

curl -s "http://localhost:18080/api/v1/ordenes"
```
