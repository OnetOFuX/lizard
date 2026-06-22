# catalogo-ms

Microservicio de catálogo. Gestiona categorías y es consumido por `producto-ms` vía Feign.

## Puertos

| Recurso | DEV | PROD Docker |
|---|---:|---:|
| App | dinámico (`server.port: 0`) | sin puerto host (vía Gateway) |
| PostgreSQL | 15432 | 25432 -> 5432 |

## DEV (Maven)

```bash
cd services/catalogo-ms
docker compose -f compose-dev.yml up -d
mvn spring-boot:run
```

Para levantar una segunda instancia, abre otra terminal en `services/catalogo-ms` y ejecuta:

```bash
mvn spring-boot:run
```

Links DEV:
- Config DEV: http://localhost:18888/catalogo-ms/dev
- Eureka DEV: http://localhost:18761
- Gateway DEV health: http://localhost:18080/actuator/health
- Base de datos DEV: `localhost:15432`

## PROD (Docker)

```bash
cd services/catalogo-ms
docker compose up -d --build --scale catalogo-ms=3
```

Para cambiar la cantidad de réplicas sin reconstruir:

```bash
docker compose up -d --scale catalogo-ms=2
docker compose up -d --scale catalogo-ms=3
```

Links PROD:
- Config PROD: http://localhost:28888/catalogo-ms/prod
- Eureka PROD: http://localhost:28761
- Gateway PROD health: http://localhost:28082/actuator/health
- Base de datos PROD: `localhost:25432`

## Ver la BD desde un IDE

Conéctate desde DBeaver, DataGrip, pgAdmin o IntelliJ Database con:

| Campo | Valor |
|---|---|
| Motor | PostgreSQL |
| Host | `localhost` |
| Puerto | `25432` |
| Database | `lizard_catalogo_db` |
| User | `ecom` |
| Password | `ecom` |

La BD no se escala. Las réplicas de `catalogo-ms` comparten la misma BD `lizard-postgres-catalogo` dentro de Docker.

## Ver la BD desde PowerShell

Comandos para inspeccionar la BD DEV sin abrir un IDE:

```powershell
docker exec -it lizard-postgres-catalogo-dev psql -U ecom -d lizard_catalogo_db
```

Dentro de `psql`:

```sql
\l
\c lizard_catalogo_db
\dt
\d categorias
SELECT * FROM categorias;
SELECT id, nombre, descripcion FROM categorias ORDER BY id;
SELECT COUNT(*) FROM categorias;
\x
\q
```

Tambien puedes ejecutar consultas puntuales desde PowerShell sin quedarte dentro de `psql`:

```powershell
docker exec -it lizard-postgres-catalogo-dev psql -U ecom -d lizard_catalogo_db -c "\dt"
docker exec -it lizard-postgres-catalogo-dev psql -U ecom -d lizard_catalogo_db -c "\d categorias"
docker exec -it lizard-postgres-catalogo-dev psql -U ecom -d lizard_catalogo_db -c "SELECT * FROM categorias;"
```

En PROD usa el contenedor `lizard-postgres-catalogo`.

## Endpoints

- `GET /api/v1/categorias`
- `POST /api/v1/categorias`
- `GET /api/v1/categorias/{id}`
- `PUT /api/v1/categorias/{id}`
- `DELETE /api/v1/categorias/{id}`
- `GET /api/v1/catalogo/instancia`
- `GET /actuator/health`

## Swagger directo del microservicio

En DEV Maven, `catalogo-ms` usa puerto dinamico (`server.port: 0`). Revisa el puerto asignado en la consola del microservicio o en Eureka y abre Swagger directo al microservicio:

```text
http://localhost:<puerto-asignado>/swagger-ui/index.html
```

## Prueba rapida con PowerShell

Este flujo permite crear, listar, consultar, actualizar y eliminar una categoria por Gateway DEV usando solo PowerShell.

```powershell
$body = @{
  nombre = "Electro"
  descripcion = "domestico"
} | ConvertTo-Json

$categoria = Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:18080/api/v1/categorias" `
  -ContentType "application/json" `
  -Body $body

$categoria
$idCategoria = $categoria.id

Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:18080/api/v1/categorias"

Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:18080/api/v1/categorias/$idCategoria"

$body = @{
  nombre = "Electro Hogar"
  descripcion = "domestico actualizado"
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Put `
  -Uri "http://localhost:18080/api/v1/categorias/$idCategoria" `
  -ContentType "application/json" `
  -Body $body

Invoke-RestMethod `
  -Method Delete `
  -Uri "http://localhost:18080/api/v1/categorias/$idCategoria"
```

## Prueba rapida con bash macOS/Linux

Este flujo permite crear, listar, consultar, actualizar y eliminar una categoria por Gateway DEV usando `curl`.

```bash
categoria=$(curl -s -X POST "http://localhost:18080/api/v1/categorias" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Electro","descripcion":"domestico"}')

echo "$categoria"

idCategoria=$(echo "$categoria" | jq -r '.id')

curl -s "http://localhost:18080/api/v1/categorias"

curl -s "http://localhost:18080/api/v1/categorias/$idCategoria"

curl -s -X PUT "http://localhost:18080/api/v1/categorias/$idCategoria" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Electro Hogar","descripcion":"domestico actualizado"}'

curl -i -X DELETE "http://localhost:18080/api/v1/categorias/$idCategoria"
```
