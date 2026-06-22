# producto-ms

Microservicio de productos. Consume `catalogo-ms` vía OpenFeign con Circuit Breaker.

## Puertos

| Recurso | DEV | PROD Docker |
|---|---:|---:|
| App | dinámico (`server.port: 0`) | sin puerto host (vía Gateway) |
| PostgreSQL | 15433 | 25433 -> 5432 |

## DEV (Maven)

```bash
cd services/producto-ms
docker compose -f compose-dev.yml up -d
mvn spring-boot:run
```

Para levantar una segunda instancia, abre otra terminal en `services/producto-ms` y ejecuta:

```bash
mvn spring-boot:run
```

Links DEV:
- Config DEV: http://localhost:18888/producto-ms/dev
- Eureka DEV: http://localhost:18761
- Gateway DEV health: http://localhost:18080/actuator/health
- Base de datos DEV: `localhost:15433`

## PROD (Docker)

```bash
cd services/producto-ms
docker compose up -d --build
```

Links PROD:
- Config PROD: http://localhost:28888/producto-ms/prod
- Eureka PROD: http://localhost:28761
- Gateway PROD health: http://localhost:28082/actuator/health
- Base de datos PROD: `localhost:25433`

## Ver la BD desde un IDE

| Campo | Valor |
|---|---|
| Motor | PostgreSQL |
| Host | `localhost` |
| Puerto | `25433` |
| Database | `lizard_producto_db` |
| User | `ecom` |
| Password | `ecom` |

## Ver la BD desde PowerShell

Comandos para inspeccionar la BD DEV sin abrir un IDE:

```powershell
docker exec -it lizard-postgres-producto-dev psql -U ecom -d lizard_producto_db

docker exec -it lizard-postgres-producto-dev psql -U ecom -d lizard_producto_db -c "\dt"
docker exec -it lizard-postgres-producto-dev psql -U ecom -d lizard_producto_db -c "\d productos"
docker exec -it lizard-postgres-producto-dev psql -U ecom -d lizard_producto_db -c "SELECT * FROM productos;"
```

En PROD usa el contenedor `lizard-postgres-producto`.

## Endpoints

- `GET /api/v1/productos`
- `POST /api/v1/productos`
- `GET /api/v1/productos/{id}`
- `PUT /api/v1/productos/{id}`
- `DELETE /api/v1/productos/{id}`
- `GET /api/v1/producto/instancia`
- `GET /actuator/health`

## Prueba rapida con PowerShell

Este flujo permite crear una categoria, iniciar sesion, crear un producto con JWT y consultar el detalle del producto.

```powershell
Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:18080/api/v1/categorias" `
  -ContentType "application/json" `
  -Body '{"nombre":"Electro","descripcion":"domestico"}'

$body = @{
  username = "admin"
  password = "admin123"
} | ConvertTo-Json

$response = Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:18080/auth/login" `
  -ContentType "application/json" `
  -Body $body

$response
$token = $response.accessToken

$body = @{
  nombre = "Lavadora"
  descripcion = "Samsung"
  idCategoria = 1
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:18080/api/v1/productos" `
  -Headers @{
    Authorization = "Bearer $token"
    Accept = "application/json"
  } `
  -ContentType "application/json" `
  -Body $body

Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:18080/api/v1/productos/detalle/1" `
  -Headers @{ Authorization = "Bearer $token" }
```

## Prueba rapida con bash macOS/Linux

Este flujo crea una categoria, inicia sesion, crea un producto con JWT y consulta el detalle usando `curl` y `jq`.

```bash
curl -s -X POST "http://localhost:18080/api/v1/categorias" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Electro","descripcion":"domestico"}'

response=$(curl -s -X POST "http://localhost:18080/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}')

echo "$response"

token=$(echo "$response" | jq -r '.accessToken')

curl -s -X POST "http://localhost:18080/api/v1/productos" \
  -H "Authorization: Bearer $token" \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Lavadora","descripcion":"Samsung","idCategoria":1}'

curl -s "http://localhost:18080/api/v1/productos/detalle/1" \
  -H "Authorization: Bearer $token"
```
