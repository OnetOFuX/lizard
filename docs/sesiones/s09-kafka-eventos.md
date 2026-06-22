# S9 - Consistencia distribuida en procesos de negocio

## Ubicacion en el curso

- Unidad: U2 - Sistema distribuido robusto.
- Producto de unidad: sistema seguro, resiliente, consistente, observable e integrado.
- Avance del producto en esta sesion: proceso de negocio distribuido con consistencia eventual.

## Proposito

Abordar el problema central de los microservicios: cada servicio tiene su propia base de datos y no existe una transaccion global simple.

## Resultado de aprendizaje

El estudiante modela un flujo de negocio distribuido con estados, eventos, compensacion, idempotencia y correlacion.

## Producto de sesion

Flujo de orden y pago con consistencia eventual: la orden cambia de estado segun el resultado del pago y evita duplicados ante reintentos.

## Concepto distribuido clave

La consistencia distribuida se logra coordinando transacciones locales mediante eventos, estados y acciones compensatorias.

## Implementacion en el proyecto

En `ecom`, el flujo se implementa con `orden-ms`, `pago-ms` y Kafka. Puede evolucionar hacia una pasarela externa simulada o real, como Stripe, Mercado Pago o Culqi, sin cambiar el concepto de la sesion.

## Distribucion de carga

Laboratorio 4h:

- Identificar estados del proceso.
- Definir eventos de exito y rechazo.
- Implementar actualizacion de estado.
- Probar duplicados o reintentos.
- Cerrar el flujo de consistencia en produccion local con Docker.

Trabajo fuera del aula 4h:

- Documentar diagrama de saga.
- Agregar evidencia de compensacion.
- Explicar idempotencia y correlacion.
- Registrar aporte individual.

## Pasos para construir el producto de sesion

1. Definir estados de orden: creada, pagada, rechazada o equivalente.
2. Definir eventos del proceso.
3. Agregar identificador de correlacion.
4. Publicar evento al crear orden.
5. Procesar pago en `pago-ms`.
6. Publicar evento de pago aprobado o rechazado.
7. Consumir respuesta en `orden-ms`.
8. Actualizar estado de orden.
9. Evitar pagos duplicados por evento repetido.
10. Simular fallo de pago.
11. Verificar estado final en BD.
12. Ejecutar cierre en produccion local con Docker.
13. Documentar el flujo como saga coreografica.

## Archivos involucrados

| Archivo | Proposito |
|---|---|
| `services/orden-ms` | Estado de orden y eventos |
| `services/pago-ms` | Procesamiento de pago |
| `kafka/compose-dev.yml` | Broker de eventos |
| `docs/` | Evidencia y diagrama del flujo |

## Comandos de ejecucion

PowerShell:

```powershell
Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:18080/api/v1/ordenes" `
  -ContentType "application/json" `
  -Body '{"usuarioId":1,"total":159.90}'

Invoke-RestMethod -Method Get -Uri "http://localhost:18080/api/v1/ordenes"
Invoke-RestMethod -Method Get -Uri "http://localhost:18080/api/v1/pagos"
```

bash macOS/Linux:

```bash
curl -s -X POST "http://localhost:18080/api/v1/ordenes" \
  -H "Content-Type: application/json" \
  -d '{"usuarioId":1,"total":159.90}'

curl -s "http://localhost:18080/api/v1/ordenes"
curl -s "http://localhost:18080/api/v1/pagos"
```

## Cierre en produccion local con Docker

```bash
cd kafka
docker compose up -d --build

cd ../services/orden-ms
docker compose up -d --build

cd ../pago-ms
docker compose up -d --build
```

En produccion local se valida que el proceso distribuido funciona entre contenedores y mantiene consistencia eventual aun cuando cada microservicio conserva su propia base de datos.

## Verificacion funcional

- Orden creada con estado inicial.
- Pago procesado asincronicamente.
- Orden actualizada segun evento de pago.
- Evento repetido no duplica pago.
- Fallo simulado deja estado compensado o rechazado.

## Observabilidad y diagnostico

- Kafka UI.
- Logs con `ordenId` o identificador de correlacion.
- Tablas `ordenes` y `pagos`.
- Evento de exito y evento de rechazo.

## Verificacion de base de datos

```powershell
docker exec -it lizard-postgres-orden-dev psql -U ecom -d lizard_orden_db -c "SELECT * FROM ordenes;"
docker exec -it lizard-postgres-pago-dev psql -U ecom -d lizard_pago_db -c "SELECT * FROM pagos;"
```

## Evidencia esperada

- Diagrama simple de saga.
- Estados antes y despues.
- Evento de pago procesado.
- Caso de duplicado controlado.
- Caso de fallo o compensacion.
- Evidencia individual.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| Pago duplicado | Falta idempotencia | Validar evento/proceso existente |
| Orden queda pendiente | No consume evento de respuesta | Revisar consumer de `orden-ms` |
| No se entiende el flujo | Falta correlacion | Usar `ordenId` en logs/eventos |

## Preguntas de defensa

1. Por que no se usa una transaccion global?
2. Que es consistencia eventual?
3. Que es una accion compensatoria?
4. Como evitas duplicar pagos?
5. Como integrarias una pasarela externa?

## Checklist de cierre

- [ ] Flujo de estados definido.
- [ ] Evento de respuesta procesado.
- [ ] Idempotencia evidenciada.
- [ ] Compensacion o rechazo probado.
- [ ] Evidencia individual registrada.
