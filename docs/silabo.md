# Silabo del curso

## Sumilla

Curso de programacion avanzada orientado a la construccion incremental de un sistema distribuido basado en microservicios. El estudiante diseña, implementa, ejecuta, diagnostica y defiende un producto compuesto por servicios independientes, configuracion por ambientes, descubrimiento dinamico, acceso unico, comunicacion entre servicios, seguridad, mensajeria asincrona, consistencia distribuida, observabilidad e integracion con cliente frontend.

En el silabo se nombran capacidades y conceptos generales. El stack concreto se desarrolla en las sesiones del proyecto `ecom`.

## Competencia general

Construye y sustenta un sistema distribuido funcional, robusto y observable, aplicando principios de microservicios, comunicacion distribuida, seguridad, consistencia eventual y validacion tecnica del producto.

## Productos por unidad

| Unidad | Producto |
|---|---|
| U1 | Sistema distribuido base funcional, configurable y preparado para multiples instancias |
| U2 | Sistema distribuido robusto, seguro, resiliente, consistente, observable e integrado con cliente |
| U3 | Producto final del curso validado, documentado y defendido tecnicamente |

## Secuencia de sesiones

| Sesion | Nombre conceptual | Producto de aprendizaje |
|---|---|---|
| S1 | Construccion de un servicio base para un sistema distribuido | Servicio REST funcional, persistente, observable y preparado para ejecucion reproducible |
| S2 | Gestion centralizada de configuracion y ambientes | Configuracion externa por ambiente y evidencia inicial de observabilidad |
| S3 | Registro, descubrimiento y ejecucion concurrente de servicios | Servicios descubiertos dinamicamente y multiples instancias operativas |
| S4 | Punto unico de acceso y distribucion de trafico | Acceso centralizado con rutas y balanceo de carga |
| S5 | Evaluacion integradora de sistema base | Producto U1 funcionando y sustentado por el equipo |
| S6 | Comunicacion sincronica resiliente entre servicios | Operacion distribuida con respuesta controlada ante fallos |
| S7 | Seguridad distribuida y control de acceso | Autenticacion, autorizacion y proteccion de rutas del sistema |
| S8 | Mensajeria asincrona entre servicios | Comunicacion por eventos entre servicios desacoplados |
| S9 | Consistencia distribuida en procesos de negocio | Proceso distribuido con consistencia eventual, compensacion e idempotencia |
| S10 | Observabilidad y diagnostico de sistemas distribuidos | Evidencias de health, logs, metricas y diagnostico de fallos |
| S11 | Integracion con cliente frontend | Cliente integrado al sistema distribuido mediante el punto unico de acceso |
| S12 | Evaluacion integradora de sistema robusto | Producto U2 validado bajo escenarios reales de uso y fallo |
| S13 | Validacion end-to-end del producto del curso | Flujos completos probados desde cliente hasta servicios, eventos y base de datos |
| S14 | Revision tecnica y estabilizacion del producto | Producto documentado, reproducible, depurado y listo para defensa; opcionalmente preparado para orquestacion local |
| S15 | Defensa tecnica grupal | Sustentacion grupal con evidencia individual de aporte |
| S16 | Evaluacion final individual | Demostracion individual de competencias pendientes |

## Metodologia

Cada sesion parte de una situacion problema, introduce un concepto distribuido, construye un avance del producto, verifica su comportamiento con comandos y exige evidencia tecnica individual.

La dedicacion esperada por sesion es:

- 4 horas de laboratorio guiado.
- 4 horas de trabajo fuera del aula.

## Evaluacion

La evaluacion del producto es grupal, pero la nota es individual. Cada integrante debe evidenciar su aporte mediante codigo, configuracion, comandos ejecutados, registros en base de datos, logs, eventos, capturas, commits, explicacion oral o correccion tecnica durante la defensa.

Principio de evaluacion:

> El producto se construye en equipo; la competencia se demuestra individualmente.

## Evidencias transversales

- Servicio ejecutando.
- Endpoint probado.
- Configuracion consultable.
- Registro dinamico del servicio.
- Ruta por punto unico de acceso.
- Prueba de escalado o distribucion de carga.
- Comunicacion entre servicios.
- Escenario de fallo diagnosticado.
- Token o regla de acceso verificada.
- Evento publicado y consumido.
- Estado consistente o compensado.
- Registro en base de datos.
- Log, health, metrica o dashboard.
- Participacion individual sustentada.

## Alcance de despliegue

El curso valida produccion local con Docker. Kubernetes puede trabajarse como extension de U3 en entorno local, por ejemplo con Minikube, Kind o Docker Desktop Kubernetes. El despliegue en nube queda fuera del alcance de este curso.
