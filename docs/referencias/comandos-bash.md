# Comandos bash macOS/Linux

## Health del Gateway

```bash
curl http://localhost:18080/actuator/health
```

## Login

```bash
response=$(curl -s -X POST "http://localhost:18080/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}')

token=$(echo "$response" | jq -r '.accessToken')
echo "$token"
```

## Consumir ruta protegida

```bash
curl -H "Authorization: Bearer $token" \
  http://localhost:18080/api/v1/productos
```

## PostgreSQL dentro del contenedor

```bash
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

```bash
docker exec -it lizard-kafka-dev /opt/kafka/bin/kafka-topics.sh --list --bootstrap-server kafka:9092
docker exec -it lizard-kafka-dev /opt/kafka/bin/kafka-topics.sh --create --topic lizard.orden.eventos --bootstrap-server kafka:9092 --partitions 1 --replication-factor 1
docker exec -it lizard-kafka-dev /opt/kafka/bin/kafka-topics.sh --create --topic lizard.pago.eventos --bootstrap-server kafka:9092 --partitions 1 --replication-factor 1
```
