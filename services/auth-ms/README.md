# auth-ms

Microservicio de autenticación. Registra usuarios, realiza login y emite JWT.

## Puertos

| Recurso | DEV | PROD Docker |
|---|---:|---:|
| App | dinámico (`server.port: 0`) | sin puerto host (vía Gateway) |
| PostgreSQL | 15431 | 25431 -> 5432 |

## DEV (Maven)

```bash
cd services/auth-ms
docker compose -f compose-dev.yml up -d
mvn spring-boot:run
```

Para levantar una segunda instancia, abre otra terminal en `services/auth-ms` y ejecuta:

```bash
mvn spring-boot:run
```

Links DEV:
- Config DEV: http://localhost:18888/auth-ms/dev
- Eureka DEV: http://localhost:18761
- Gateway DEV health: http://localhost:18080/actuator/health
- Base de datos DEV: `localhost:15431`

## PROD (Docker)

```bash
cd services/auth-ms
docker compose up -d --build
```

Links PROD:
- Config PROD: http://localhost:28888/auth-ms/prod
- Eureka PROD: http://localhost:28761
- Gateway PROD health: http://localhost:28082/actuator/health
- Base de datos PROD: `localhost:25431`

## Ver la BD desde un IDE

| Campo | Valor |
|---|---|
| Motor | PostgreSQL |
| Host | `localhost` |
| Puerto | `25431` |
| Database | `lizard_auth_db` |
| User | `ecom` |
| Password | `ecom` |

## Ver la BD desde PowerShell

Comandos para inspeccionar la BD DEV sin abrir un IDE:

```powershell
docker exec -it lizard-postgres-auth-dev psql -U ecom -d lizard_auth_db

docker exec -it lizard-postgres-auth-dev psql -U ecom -d lizard_auth_db -c "\dt"
docker exec -it lizard-postgres-auth-dev psql -U ecom -d lizard_auth_db -c "\d users"
docker exec -it lizard-postgres-auth-dev psql -U ecom -d lizard_auth_db -c "SELECT id, username, enabled, created_at FROM users;"
docker exec -it lizard-postgres-auth-dev psql -U ecom -d lizard_auth_db -c "SELECT * FROM roles;"
docker exec -it lizard-postgres-auth-dev psql -U ecom -d lizard_auth_db -c "SELECT * FROM user_roles;"
```

En PROD usa el contenedor `lizard-postgres-auth`.

## Endpoints

- `POST /auth/login`
- `GET /actuator/health`

## Prueba rapida con PowerShell

Este flujo permite iniciar sesion con el usuario inicial `admin` y obtener un JWT.

```powershell
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
$token
```

## Prueba rapida con bash macOS/Linux

Este flujo usa `curl` y `jq` para iniciar sesion y extraer el JWT.

```bash
response=$(curl -s -X POST "http://localhost:18080/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}')

echo "$response"

token=$(echo "$response" | jq -r '.accessToken')
echo "$token"
```

## JWT

`JWT_SECRET` debe coincidir con `infra/.env` para que Gateway valide los tokens emitidos por Auth.
