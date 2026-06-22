# pago-ms

Microservicio de pagos. Consume `lizard.orden.eventos` y publica `lizard.pago.eventos`.

## Puertos

| Recurso | DEV | PROD Docker |
|---|---:|---:|
| App | dinámico (`server.port: 0`) | sin puerto host (vía Gateway) |
| PostgreSQL | 15435 | 25435 -> 5432 |
| Kafka | 41092 | 29092 |

## DEV (Maven)

```bash
cd kafka && docker compose -f compose-dev.yml up -d
cd ../services/pago-ms
docker compose -f compose-dev.yml up -d
mvn spring-boot:run
```

Para levantar una segunda instancia, abre otra terminal en `services/pago-ms` y ejecuta:

```bash
mvn spring-boot:run
```

Links DEV:
- Config DEV: http://localhost:18888/pago-ms/dev
- Eureka DEV: http://localhost:18761
- Gateway DEV health: http://localhost:18080/actuator/health
- Base de datos DEV: `localhost:15435`

## PROD (Docker)

```bash
cd services/pago-ms
docker compose up -d --build
```

Links PROD:
- Config PROD: http://localhost:28888/pago-ms/prod
- Eureka PROD: http://localhost:28761
- Gateway PROD health: http://localhost:28082/actuator/health
- Base de datos PROD: `localhost:25435`

## Ver la BD desde un IDE

| Campo | Valor |
|---|---|
| Motor | PostgreSQL |
| Host | `localhost` |
| Puerto | `25435` |
| Database | `lizard_pago_db` |
| User | `ecom` |
| Password | `ecom` |

## Ver la BD desde PowerShell

Comandos para inspeccionar la BD DEV sin abrir un IDE:

```powershell
docker exec -it lizard-postgres-pago-dev psql -U ecom -d lizard_pago_db

docker exec -it lizard-postgres-pago-dev psql -U ecom -d lizard_pago_db -c "\dt"
docker exec -it lizard-postgres-pago-dev psql -U ecom -d lizard_pago_db -c "\d pagos"
docker exec -it lizard-postgres-pago-dev psql -U ecom -d lizard_pago_db -c "SELECT * FROM pagos;"
```

En PROD usa el contenedor `lizard-postgres-pago`.

## Eventos

- Consume: `lizard.orden.eventos`
- Publica: `lizard.pago.eventos`

## Prueba rapida con PowerShell

`pago-ms` no expone un `POST` para crear pagos. Los pagos se generan al consumir eventos `orden.creada` publicados por `orden-ms`.

```powershell
Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:18080/api/v1/pagos/saludo"

Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:18080/api/v1/pagos"

Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:18080/api/v1/pagos/1"
```

## Prueba rapida con bash macOS/Linux

`pago-ms` no expone un `POST` para crear pagos. Los pagos se generan al consumir eventos `orden.creada` publicados por `orden-ms`.

```bash
curl -i "http://localhost:18080/api/v1/pagos/saludo"

curl -s "http://localhost:18080/api/v1/pagos"

curl -s "http://localhost:18080/api/v1/pagos/1"
```
