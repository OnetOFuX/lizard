# S3 - Registro, descubrimiento y ejecucion concurrente de servicios

## Ubicacion en el curso

- Unidad: U1 - Sistema distribuido base orientado a produccion.
- Producto de unidad: servicios configurables, registrados y preparados para multiples instancias.
- Avance del producto en esta sesion: servicios registrados dinamicamente y ejecutados en paralelo.

## Proposito

Resolver el problema de ubicar servicios que cambian de instancia, puerto o ubicacion durante la ejecucion.

## Resultado de aprendizaje

El estudiante registra microservicios en un servidor de descubrimiento, levanta multiples instancias y verifica su presencia dinamica.

## Producto de sesion

Eureka Server operativo con `catalogo-ms`, `producto-ms` y otros servicios registrados con puertos dinamicos.

## Concepto distribuido clave

El descubrimiento de servicios permite que los componentes se encuentren por nombre logico y no por host/puerto fijo.

## Implementacion en el proyecto

En `ecom`, se usa Spring Cloud Netflix Eureka en `infra/eureka`. Los microservicios usan `server.port: 0` y se registran con su nombre `*-ms`.

## Distribucion de carga

Laboratorio 4h:

- Levantar Config Server y Eureka.
- Registrar un microservicio.
- Levantar segunda instancia.
- Verificar dashboard y logs.
- Cerrar con infraestructura en produccion local con Docker.

Trabajo fuera del aula 4h:

- Registrar otro microservicio.
- Documentar evidencia de multiples instancias.
- Explicar por que no se usa puerto host fijo.
- Preparar preguntas de defensa.

## Pasos para construir el producto de sesion

1. Levantar Config Server.
2. Crear o revisar `infra/eureka`.
3. Habilitar Eureka Server.
4. Configurar URL de Eureka desde Config Server.
5. Agregar cliente Eureka a microservicios.
6. Configurar nombre logico del servicio.
7. Ejecutar `catalogo-ms`.
8. Ejecutar una segunda instancia de `catalogo-ms`.
9. Verificar ambas instancias en Eureka.
10. Ejecutar infraestructura en produccion local con Docker.
11. Revisar logs de registro y renovacion.

## Archivos involucrados

| Archivo | Proposito |
|---|---|
| `infra/eureka/pom.xml` | Dependencias Eureka Server |
| `infra/eureka/src/main/resources/application.yml` | Configuracion base |
| `infra/config/config-repo/eureka-*.yml` | Configuracion por ambiente |
| `infra/config/config-repo/*-ms-dev.yml` | Registro de clientes |
| `services/*/pom.xml` | Dependencias cliente |

## Comandos de ejecucion

PowerShell / bash macOS/Linux:

```bash
cd infra/config
mvn spring-boot:run

cd ../eureka
mvn spring-boot:run

cd ../../services/catalogo-ms
docker compose -f compose-dev.yml up -d
mvn spring-boot:run
```

Segunda instancia, PowerShell / bash macOS/Linux:

```bash
cd services/catalogo-ms
mvn spring-boot:run
```

## Cierre en produccion local con Docker

```bash
cd infra
docker compose up -d --build
```

En produccion local, Config Server, Eureka y Gateway comparten una red Docker. Eureka publica el dashboard en `http://localhost:28761` y recibe registros de servicios ejecutados como contenedores.

## Verificacion funcional

- Eureka DEV: `http://localhost:18761`.
- Eureka PROD: `http://localhost:28761`.
- Ver `CATALOGO-MS` con mas de una instancia.
- Ver puertos dinamicos distintos.

## Observabilidad y diagnostico

- Revisar dashboard de Eureka.
- Revisar logs de `registration`.
- Verificar heartbeats.
- Confirmar que Config Server este activo antes de Eureka.

## Verificacion de base de datos

No es el foco. Solo se confirma que la BD del microservicio sigue disponible mientras se levantan instancias.

## Evidencia esperada

- Captura o salida de Eureka con servicios registrados.
- Dos instancias del mismo servicio.
- Explicacion de nombre logico vs puerto fisico.
- Evidencia individual del servicio registrado.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| Servicio no aparece | Eureka apagado o URL incorrecta | Revisar Config Server |
| Solo aparece una instancia | Segunda terminal no levantada | Ejecutar otra instancia Maven |
| Nombre raro en Eureka | `spring.application.name` incorrecto | Revisar config externa |

## Preguntas de defensa

1. Por que un servicio usa puerto dinamico?
2. Que ventaja tiene registrar por nombre logico?
3. Como demuestras que hay dos instancias?
4. Que pasa si Eureka no esta disponible al arrancar?

## Checklist de cierre

- [ ] Config Server activo.
- [ ] Eureka activo.
- [ ] Servicio registrado.
- [ ] Segunda instancia registrada.
- [ ] Evidencia individual registrada.
