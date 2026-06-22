# Comandos PowerShell

## Health del Gateway

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:18080/actuator/health"
```

## Login

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

$token = $response.accessToken
```

## Consumir ruta protegida

```powershell
Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:18080/api/v1/productos" `
  -Headers @{ Authorization = "Bearer $token" }
```

## PostgreSQL dentro del contenedor

```powershell
docker exec -it lizard-postgres-catalogo-dev psql -U ecom -d lizard_catalogo_db
```

Dentro de `psql`:

```sql
\dt
\d categorias
SELECT * FROM categorias;
\q
```

## Kafka topics

```powershell
docker exec -it lizard-kafka-dev /opt/kafka/bin/kafka-topics.sh --list --bootstrap-server kafka:9092
docker exec -it lizard-kafka-dev /opt/kafka/bin/kafka-topics.sh --create --topic lizard.orden.eventos --bootstrap-server kafka:9092 --partitions 1 --replication-factor 1
docker exec -it lizard-kafka-dev /opt/kafka/bin/kafka-topics.sh --create --topic lizard.pago.eventos --bootstrap-server kafka:9092 --partitions 1 --replication-factor 1
```
