# DISTribuidas 2026

Curso de sistemas distribuidos basado en la construccion incremental de un producto de microservicios.

El silabo usa nombres conceptuales. La implementacion del curso se desarrolla sobre `ecom`: una plataforma con Spring Cloud, microservicios Spring Boot, PostgreSQL, mensajeria asincrona, observabilidad y frontend Angular.

## Producto del curso

Sistema distribuido basado en microservicios, seguro, configurable, resiliente, observable, consistente, integrado con cliente frontend y defendido tecnicamente.

## Unidades

| Unidad | Producto |
|---|---|
| U1 | Sistema distribuido base funcional, configurable y preparado para multiples instancias |
| U2 | Sistema distribuido robusto, seguro, resiliente, consistente, observable e integrado con cliente |
| U3 | Producto final del curso validado, documentado y defendido tecnicamente |

## Sesiones

| Sesion | Tema | Producto de sesion |
|---|---|---|
| S1 | Construccion de un servicio base para un sistema distribuido | Servicio REST funcional, persistente, observable y preparado para ejecucion reproducible |
| S2 | Gestion centralizada de configuracion y ambientes | Configuracion externa por ambiente y evidencia inicial de observabilidad |
| S3 | Registro, descubrimiento y ejecucion concurrente de servicios | Servicios descubiertos dinamicamente y multiples instancias operativas |
| S4 | Punto unico de acceso y distribucion de trafico | Acceso centralizado con rutas y balanceo de carga |
| S5 | Evaluacion U1 | Sistema base integrado funcionando como un todo |
| S6 | Comunicacion sincronica resiliente entre servicios | Operacion distribuida con respuesta controlada ante fallos |
| S7 | Seguridad distribuida y control de acceso | Autenticacion, autorizacion y proteccion de rutas del sistema |
| S8 | Mensajeria asincrona entre servicios | Comunicacion por eventos entre servicios desacoplados |
| S9 | Consistencia distribuida en procesos de negocio | Proceso distribuido con consistencia eventual, compensacion e idempotencia |
| S10 | Observabilidad y diagnostico de sistemas distribuidos | Logs, health, metricas y paneles de diagnostico |
| S11 | Integracion con cliente frontend | Cliente integrado al sistema distribuido mediante Gateway |
| S12 | Evaluacion U2 | Sistema robusto validado en condiciones reales |
| S13 | Validacion end-to-end del producto del curso | Producto del curso probado integralmente |
| S14 | Revision tecnica y estabilizacion del producto | Documentacion, evidencias y estabilizacion |
| S15 | Defensa tecnica | Sustentacion grupal del producto |
| S16 | Evaluacion final | Demostracion individual de competencias pendientes |

## Regla del curso

Cada sesion produce un avance verificable del producto de unidad.

No basta con que compile. El estudiante debe poder levantar, probar, inspeccionar, diagnosticar y defender el sistema.

Cada sesion se disena para 4 horas de laboratorio y 4 horas de trabajo fuera del aula. La carga se distribuye para que el estudiante construya, pruebe, documente y defienda un avance real del producto.

La evaluacion del producto es grupal, pero la nota es individual: cada integrante debe evidenciar su aporte.

## Convencion de trabajo

- Windows: PowerShell.
- macOS/Linux: bash con `curl`.
- Pruebas de API: shell contra el Gateway, no Postman como dependencia obligatoria.
- Swagger: solo directo al puerto asignado de cada microservicio.
- Observabilidad: eje transversal desde S2, consolidado en S10.

## Primeros enlaces

- [Silabo](silabo.md)
- [Guia del curso](guia-curso.md)
- [Puertos y accesos](referencias/puertos.md)
- [Comandos PowerShell](referencias/comandos-powershell.md)
- [Comandos bash macOS/Linux](referencias/comandos-bash.md)
- [Troubleshooting](referencias/troubleshooting.md)
- [Rubrica](referencias/rubrica.md)
