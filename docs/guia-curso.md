# Guia del curso

## Enfoque

El curso se desarrolla como construccion incremental de un producto. Cada sesion entrega una parte verificable del sistema distribuido y prepara al estudiante para replicar el mismo patron en su proyecto final.

La evaluacion se concentra en cuatro capacidades:

- Implementar.
- Ejecutar.
- Diagnosticar.
- Defender tecnicamente.

## Carga de trabajo por sesion

Cada sesion tiene una dedicacion esperada de 8 horas:

- 4 horas de laboratorio guiado.
- 4 horas de trabajo fuera del aula.

El laboratorio se usa para construir el avance principal del producto de sesion. El trabajo fuera del aula se usa para terminar, documentar, probar, inspeccionar evidencias y preparar respuestas de defensa.

La distribucion de temas evita sesiones artificialmente livianas o demasiado cargadas. En el silabo los nombres son conceptuales; en cada sesion se declara el stack usado por `ecom`.

## Productos por unidad

| Unidad | Producto |
|---|---|
| U1 | Sistema distribuido base configurable, registrado, accesible por Gateway y preparado para multiples instancias |
| U2 | Sistema distribuido seguro, resiliente, observable, orientado a eventos e integrado con frontend |
| U3 | Producto final del curso validado, documentado, estabilizado y defendido |

## Distribucion de sesiones

| Sesion | Enfoque | Carga principal |
|---|---|---|
| S1 | Servicio base distribuido | Crear un microservicio REST funcional y reproducible |
| S2 | Configuracion y ambientes | Externalizar configuracion y comenzar observabilidad transversal |
| S3 | Registro y descubrimiento | Registrar servicios y levantar multiples instancias |
| S4 | Acceso unico y trafico | Enrutar y distribuir carga desde un punto unico |
| S5 | Evaluacion U1 | Validar el sistema base completo |
| S6 | Comunicacion sincronica resiliente | Comunicar servicios y controlar fallos en la misma operacion |
| S7 | Seguridad distribuida | Autenticar, autorizar y proteger rutas |
| S8 | Mensajeria asincrona | Publicar y consumir eventos de negocio |
| S9 | Consistencia distribuida | Coordinar un proceso de negocio con consistencia eventual |
| S10 | Observabilidad y diagnostico | Unificar health, logs, metricas y diagnostico |
| S11 | Integracion frontend | Consumir el sistema desde el cliente web |
| S12 | Evaluacion U2 | Validar robustez en condiciones reales |
| S13 | Validacion end-to-end | Validar el producto final del curso |
| S14 | Revision tecnica | Estabilizar documentacion, evidencias y despliegue; opcionalmente preparar orquestacion local |
| S15 | Defensa tecnica | Sustentar decisiones y funcionamiento grupalmente |
| S16 | Evaluacion final | Demostrar competencias pendientes individualmente |

## Observabilidad transversal

La observabilidad inicia en S2, no en S10.

En cada sesion se verifica, segun aplique:

- Health checks.
- Logs.
- Config Server.
- Eureka.
- Gateway.
- Base de datos.
- Kafka UI.
- Metricas.
- Evidencia de errores y recuperacion.

S10 consolida estos elementos en una vista operativa del sistema completo.

## Plantilla de cada sesion

Cada sesion usa la misma estructura:

```text
1. Ubicacion en el curso
2. Proposito
3. Resultado de aprendizaje
4. Producto de sesion
5. Concepto distribuido clave
6. Implementacion en el proyecto
7. Distribucion de carga: 4h laboratorio + 4h fuera del aula
8. Pasos para construir el producto de sesion
9. Archivos involucrados
10. Comandos de ejecucion
11. Cierre en produccion local con Docker
12. Verificacion funcional
13. Observabilidad y diagnostico
14. Verificacion de base de datos
15. Evidencia esperada
16. Errores frecuentes
17. Preguntas de defensa
18. Checklist de cierre
```

Cuando un comando es identico en Windows, macOS y Linux, se documenta en un solo bloque como `PowerShell / bash macOS/Linux`. Solo se separan bloques cuando cambia la sintaxis, por ejemplo `Invoke-RestMethod` frente a `curl`.

## Produccion local con Docker

Todas las sesiones se trabajan primero en DEV, normalmente con Maven y bases de datos en Docker. Al cierre de la sesion, cuando aplique, se ejecuta una validacion breve en produccion local con Docker:

- `docker compose up -d --build` para construir y levantar imagenes.
- Health por Gateway o componente correspondiente.
- Explicacion breve de que en Docker los servicios se ejecutan como contenedores, usan red interna, variables de entorno y perfiles `prod`.

Kubernetes queda como evolucion metodologica para U3, primero en local con Minikube, Kind o Docker Desktop Kubernetes. El despliegue en nube pertenece a otro curso.

## Criterio de cierre por sesion

Una sesion esta cerrada cuando el estudiante puede mostrar evidencias concretas:

- Salida de comandos.
- Servicio registrado.
- Endpoint funcionando.
- Registro en base de datos.
- Evento Kafka.
- Log o metrica.
- Pantalla Angular.
- Respuesta a preguntas de defensa.

## Evaluacion grupal con nota individual

El producto de unidad se presenta por equipo, pero la calificacion se asigna individualmente. Cada integrante debe demostrar participacion mediante al menos tres evidencias:

- Codigo, configuracion o documentacion propia.
- Comando de prueba ejecutado y explicado.
- Registro en base de datos, evento, log o metrica relacionado con su aporte.
- Commit, issue, tarea o bitacora de responsabilidad.
- Respuesta tecnica durante la defensa.
- Correccion o ajuste en vivo cuando el docente lo solicite.
