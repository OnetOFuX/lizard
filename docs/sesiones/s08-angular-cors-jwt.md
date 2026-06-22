# S8 - Mensajeria asincrona entre servicios

## Ubicacion en el curso

- Unidad: U2 - Sistema distribuido robusto.
- Producto de unidad: sistema seguro, resiliente, consistente, observable e integrado.
- Avance del producto en esta sesion: comunicacion por eventos entre servicios desacoplados.

## Proposito

Incorporar mensajeria asincrona para que un servicio publique hechos de negocio y otro los procese sin depender de una respuesta inmediata.

## Resultado de aprendizaje

El estudiante crea topics, publica eventos, consume mensajes y verifica el resultado en logs y base de datos.

## Producto de sesion

Flujo asincrono donde `orden-ms` publica eventos de orden y `pago-ms` los consume para generar pagos.

## Concepto distribuido clave

La mensajeria asincrona desacopla productor y consumidor. El productor comunica que algo ocurrio; el consumidor procesa cuando puede.

## Implementacion en el proyecto

En `ecom`, la mensajeria se implementa con Kafka. En el silabo se mantiene como concepto generico, porque podria implementarse con otro broker.

## Distribucion de carga

Laboratorio 4h:

- Levantar broker y UI.
- Crear topics.
- Implementar o revisar producer.
- Implementar o revisar consumer.
- Probar evento y persistencia.
- Cerrar con broker y servicios de eventos en produccion local con Docker.

Trabajo fuera del aula 4h:

- Documentar topics y payload.
- Registrar evidencia de logs y BD.
- Explicar productor/consumidor.
- Preparar defensa individual.

## Pasos para construir el producto de sesion

1. Levantar infraestructura de mensajeria.
2. Crear topics `lizard.orden.eventos` y `lizard.pago.eventos`.
3. Definir evento de negocio.
4. Implementar producer en `orden-ms`.
5. Publicar evento al crear una orden.
6. Implementar consumer en `pago-ms`.
7. Persistir pago generado por evento.
8. Verificar logs del producer.
9. Verificar logs del consumer.
10. Verificar registros en BD.
11. Ejecutar cierre en produccion local con Docker.
12. Revisar mensajes desde la UI del broker.

## Archivos involucrados

| Archivo | Proposito |
|---|---|
| `kafka/compose-dev.yml` | Broker y UI DEV |
| `services/orden-ms` | Productor de eventos |
| `services/pago-ms` | Consumidor de eventos |
| `infra/config/config-repo/orden-ms-dev.yml` | Configuracion del producer |
| `infra/config/config-repo/pago-ms-dev.yml` | Configuracion del consumer |

## Comandos de ejecucion

PowerShell / bash macOS/Linux:

```bash
cd kafka
docker compose -f compose-dev.yml up -d
docker compose -f compose-dev.yml exec kafka bash
```

Dentro del broker:

```bash
/opt/kafka/bin/kafka-topics.sh --create --topic lizard.orden.eventos --bootstrap-server kafka:9092 --partitions 1 --replication-factor 1
/opt/kafka/bin/kafka-topics.sh --create --topic lizard.pago.eventos --bootstrap-server kafka:9092 --partitions 1 --replication-factor 1
/opt/kafka/bin/kafka-topics.sh --list --bootstrap-server kafka:9092
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

En produccion local, el broker y los servicios de eventos corren como contenedores. Los microservicios se comunican por la red Docker y las pruebas externas se realizan por Gateway PROD.

## Verificacion funcional

Crear una orden:

```powershell
$body = @{
  usuarioId = 1
  total = 159.90
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:18080/api/v1/ordenes" `
  -ContentType "application/json" `
  -Body $body
```

Ver pagos:

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:18080/api/v1/pagos"
```

## Observabilidad y diagnostico

- Kafka UI DEV: `http://localhost:41085`.
- Logs de `orden-ms`.
- Logs de `pago-ms`.
- Health del Gateway: `http://localhost:18080/actuator/health`.

## Verificacion de base de datos

```powershell
docker exec -it lizard-postgres-orden-dev psql -U ecom -d lizard_orden_db -c "SELECT * FROM ordenes;"
docker exec -it lizard-postgres-pago-dev psql -U ecom -d lizard_pago_db -c "SELECT * FROM pagos;"
```

## Evidencia esperada

- Topics creados.
- Orden creada.
- Evento publicado.
- Pago generado por consumo.
- Registros en BD.
- Evidencia individual.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| Topic no existe | No se crearon topics | Ejecutar comandos de creacion |
| Consumer no procesa | `pago-ms` apagado o mal configurado | Revisar logs |
| No hay pago | Evento no llego o fallo persistencia | Revisar Kafka UI y BD |

## Preguntas de defensa

1. Que problema resuelve la mensajeria asincrona?
2. Que diferencia hay entre producer y consumer?
3. Como demuestras que el evento fue procesado?
4. Que pasa si el consumidor esta apagado temporalmente?

## Checklist de cierre

- [ ] Broker activo.
- [ ] Topics creados.
- [ ] Evento publicado.
- [ ] Evento consumido.
- [ ] Evidencia individual registrada.
