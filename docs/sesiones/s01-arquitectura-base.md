# S1 - Construccion de un servicio base para un sistema distribuido

## Ubicacion en el curso

- Unidad: U1 - Sistema distribuido base orientado a produccion.
- Producto de unidad: sistema distribuido base funcional, configurable y preparado para multiples instancias.
- Avance del producto en esta sesion: primer microservicio REST funcional, persistente, observable y ejecutable fuera del IDE.

## Proposito

Construir el primer servicio de negocio del sistema y dejarlo preparado para crecer hacia una arquitectura distribuida.

## Resultado de aprendizaje

El estudiante implementa un microservicio stateless con API REST, persistencia, validacion, documentacion de endpoints, health check y ejecucion reproducible en desarrollo y produccion.

## Producto de sesion

`catalogo-ms` funcional con CRUD de categorias, PostgreSQL en Docker, Swagger, Actuator, README operativo y pruebas por shell.

## Concepto distribuido clave

Un microservicio debe tener responsabilidad clara, persistencia propia, configuracion por ambiente y capacidad de ejecutarse de forma independiente.

## Implementacion en el proyecto

En `ecom`, el docente guia `catalogo-ms` y el estudiante replica el patron en `producto-ms` como trabajo aplicado. La version actual usa monorepo, nombres con sufijo `-ms`, PostgreSQL y puertos dinamicos para los microservicios.

Stack de la sesion:

- Java 17, Maven y Spring Boot 3.5.x.
- Spring Web, Spring Data JPA, Validation, Lombok, PostgreSQL Driver.
- Flyway, Actuator, SpringDoc OpenAPI.
- Docker, Docker Compose y VS Code.

## Distribucion de carga

Laboratorio 4h:

- Crear o revisar el microservicio base.
- Implementar entidad, repositorio, servicio y controlador.
- Levantar PostgreSQL DEV y probar endpoints.
- Revisar Swagger, health y README.
- Cerrar con una ejecucion breve en produccion local con Docker.

Trabajo fuera del aula 4h:

- Replicar el patron en otro servicio del dominio.
- Completar pruebas PowerShell/bash.
- Documentar evidencias individuales.
- Preparar explicacion tecnica del flujo REST y BD.

## Pasos para construir el producto de sesion

1. Preparar ambiente local: Java 17, Maven, Docker y VS Code.
2. Crear el proyecto Spring Boot o revisar `services/catalogo-ms`.
3. Agregar dependencias base.
4. Modelar la entidad `Categoria`.
5. Crear repositorio JPA.
6. Crear servicio de aplicacion.
7. Crear controlador REST.
8. Agregar validaciones.
9. Configurar PostgreSQL DEV con `compose-dev.yml`.
10. Ejecutar en DEV con `mvn spring-boot:run`.
11. Probar endpoints con PowerShell o bash.
12. Revisar Swagger directo al puerto asignado.
13. Verificar `/actuator/health`.
14. Ejecutar produccion local con Docker.
15. Explicar diferencia entre DEV Maven y PROD Docker.
16. Documentar README y evidencias.

## Archivos involucrados

| Archivo | Proposito |
|---|---|
| `services/catalogo-ms/pom.xml` | Dependencias del microservicio |
| `services/catalogo-ms/compose-dev.yml` | PostgreSQL DEV |
| `services/catalogo-ms/Dockerfile` | Imagen de aplicacion |
| `services/catalogo-ms/src/main/java/...` | Codigo del servicio |
| `services/catalogo-ms/src/main/resources/db/migration` | Migraciones Flyway |
| `services/catalogo-ms/README.md` | Operacion y pruebas |

## Comandos de ejecucion

PowerShell / bash macOS/Linux:

```bash
cd services/catalogo-ms
docker compose -f compose-dev.yml up -d
mvn spring-boot:run
```

## Cierre en produccion local con Docker

```bash
cd services/catalogo-ms
docker compose up -d --build
```

En produccion local, Docker construye la imagen del microservicio y levanta el contenedor junto con sus dependencias declaradas en `compose.yml`. El microservicio no publica puerto host fijo; se accede por Gateway cuando el sistema distribuido ya esta integrado.

## Verificacion funcional

- Crear categoria por API.
- Listar categorias.
- Consultar categoria por id.
- Actualizar y eliminar una categoria.
- Abrir Swagger directo: `http://localhost:<puerto-asignado>/swagger-ui/index.html`.
- Ver health directo: `http://localhost:<puerto-asignado>/actuator/health`.

## Observabilidad y diagnostico

- Revisar logs de arranque.
- Confirmar puerto dinamico asignado.
- Confirmar conexion a PostgreSQL.
- Revisar errores de validacion HTTP 400.

## Verificacion de base de datos

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

## Evidencia esperada

- Microservicio ejecutando.
- Ejecucion DEV con Maven y cierre PROD local con Docker.
- CRUD probado por shell.
- Swagger y health funcionando.
- Tabla `categorias` con registros.
- README operativo actualizado.
- Evidencia individual del aporte.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| No conecta a BD | PostgreSQL apagado o puerto incorrecto | Revisar compose y variables |
| Swagger no abre | Puerto dinamico no identificado | Revisar consola o Eureka cuando aplique |
| Validacion no responde | Falta `@Valid` o anotaciones | Revisar controlador y DTO |

## Preguntas de defensa

1. Por que un microservicio debe ser stateless?
2. Que responsabilidad tiene `catalogo-ms`?
3. Como se prueba el servicio sin usar Postman?
4. Que evidencia demuestra que la BD fue usada?
5. Que parte implementaste o replicaste individualmente?

## Checklist de cierre

- [ ] Servicio ejecuta con Maven.
- [ ] PostgreSQL DEV activo.
- [ ] CRUD probado por shell.
- [ ] Health y Swagger verificados.
- [ ] Evidencia individual registrada.
