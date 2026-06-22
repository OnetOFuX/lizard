# Troubleshooting

## Config Server no responde

Verificar:

```text
http://localhost:18888/actuator/health
```

Si no responde:

- Revisar que `infra/config` este levantado.
- Revisar puerto `18888`.
- Revisar logs de Maven.

## Servicio no aparece en Eureka

Verificar:

```text
http://localhost:18761
```

Posibles causas:

- Eureka no esta levantado.
- Config Server no responde.
- El microservicio no cargo `eureka.client.service-url.defaultZone`.
- El microservicio fallo al conectar a BD.

## Gateway devuelve 503

Posibles causas:

- Servicio destino no registrado en Eureka.
- Ruta mal definida en `gateway-dev.yml`.
- Servicio destino apagado.

## Gateway devuelve 401

Posibles causas:

- Ruta protegida sin token.
- Token vencido o invalido.
- `JWT_SECRET` o `issuer` no coinciden entre `auth-ms` y Gateway.

## Kafka no genera pagos

Verificar:

- Kafka esta levantado.
- Topics existen.
- `orden-ms` publica evento.
- `pago-ms` esta levantado y consume.
- Revisar Kafka UI en `http://localhost:41085`.

## Angular no consume API

Verificar:

- `environment.ts` apunta a `http://localhost:18080`.
- Gateway esta activo.
- CORS configurado.
- Token enviado por interceptor si la ruta esta protegida.
