# S10 - Observabilidad y diagnostico de sistemas distribuidos

## Ubicacion en el curso

- Unidad: U2 - Sistema distribuido robusto.
- Producto de unidad: sistema seguro, resiliente, consistente, observable e integrado.
- Avance del producto en esta sesion: consolidacion de health, logs, metricas y diagnostico.

## Proposito

Convertir la observabilidad trabajada desde S2 en una practica operativa para diagnosticar fallos reales del sistema.

## Resultado de aprendizaje

El estudiante identifica fallos usando evidencias observables y explica que componente revisa segun el sintoma.

## Producto de sesion

Sistema observable con health checks, logs, metricas, dashboards y evidencias de diagnostico.

## Concepto distribuido clave

En un sistema distribuido, el error puede estar en configuracion, red, Gateway, servicio, BD, broker o cliente. La observabilidad reduce incertidumbre.

## Implementacion en el proyecto

En `ecom`, se usa Actuator, logs, Prometheus, Loki, Grafana, Promtail y Kafka UI segun el componente.

## Distribucion de carga

Laboratorio 4h:

- Levantar stack de observabilidad.
- Revisar health y targets.
- Simular fallos.
- Diagnosticar con logs/metricas.
- Cerrar con stack de observabilidad en produccion local con Docker.

Trabajo fuera del aula 4h:

- Preparar bitacora de incidentes.
- Documentar capturas y comandos.
- Explicar ruta de diagnostico.
- Registrar aporte individual.

## Pasos para construir el producto de sesion

1. Revisar endpoints Actuator expuestos.
2. Levantar stack `obs`.
3. Configurar o revisar Prometheus.
4. Revisar targets.
5. Revisar logs por servicio.
6. Revisar dashboards.
7. Simular servicio apagado.
8. Simular error 401 o 503.
9. Simular evento no consumido.
10. Ejecutar cierre en produccion local con Docker.
11. Documentar diagnostico y causa probable.

## Archivos involucrados

| Archivo | Proposito |
|---|---|
| `obs/compose-dev.yml` | Stack observabilidad DEV |
| `obs/prometheus/prometheus-dev.yml` | Scrape de metricas |
| `obs/grafana` | Dashboards |
| `obs/loki` | Logs centralizados |
| `infra/config/config-repo/*-dev.yml` | Exposure de Actuator |

## Comandos de ejecucion

PowerShell / bash macOS/Linux:

```bash
cd obs
docker compose -f compose-dev.yml up -d
```

## Cierre en produccion local con Docker

```bash
cd obs
docker compose up -d --build
```

En produccion local, Prometheus, Loki, Promtail y Grafana se ejecutan como contenedores y observan los servicios publicados en la red Docker.

## Verificacion funcional

- Gateway health activo.
- Prometheus con targets.
- Grafana disponible.
- Logs visibles.
- Kafka UI disponible si se prueba mensajeria.

## Observabilidad y diagnostico

Revisar segun sintoma:

| Sintoma | Primera revision |
|---|---|
| 401 | Token, Gateway y reglas de seguridad |
| 503 | Eureka, Gateway y servicio destino |
| Evento no llega | Broker, topic, producer y consumer |
| BD falla | Compose, credenciales y logs JPA |
| Config incorrecta | Config Server y perfil |

## Verificacion de base de datos

No es el foco central, pero se correlaciona cada flujo diagnosticado con datos persistidos.

## Evidencia esperada

- Captura o salida de health.
- Target Prometheus o dashboard.
- Log de un fallo.
- Explicacion causa-raiz.
- Evidencia individual.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| Target caido | Servicio apagado | Levantar servicio o corregir scrape |
| No hay logs | Configuracion incompleta | Revisar Promtail/Loki |
| Diagnostico superficial | Solo se mira la UI | Correlacionar logs, health y BD |

## Preguntas de defensa

1. Que diferencia hay entre log, metrica y health?
2. Que revisas ante un 503?
3. Como diagnosticas un evento no consumido?
4. Como se relaciona observabilidad con defensa tecnica?

## Checklist de cierre

- [ ] Stack `obs` activo.
- [ ] Health revisado.
- [ ] Logs revisados.
- [ ] Fallo diagnosticado.
- [ ] Evidencia individual registrada.
