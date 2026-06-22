# S4 - Punto unico de acceso y distribucion de trafico

## Ubicacion en el curso

- Unidad: U1 - Sistema distribuido base orientado a produccion.
- Producto de unidad: sistema accesible por un punto unico y preparado para distribuir carga.
- Avance del producto en esta sesion: rutas centralizadas y balanceo entre instancias.

## Proposito

Evitar que el cliente consuma microservicios directamente y distribuir peticiones entre instancias disponibles.

## Resultado de aprendizaje

El estudiante configura rutas por punto unico de entrada, prueba APIs por Gateway y evidencia distribucion de trafico.

## Producto de sesion

Gateway operativo con rutas a microservicios registrados y balanceo de carga hacia multiples instancias.

## Concepto distribuido clave

El punto unico de acceso concentra rutas, politicas transversales y distribucion de trafico hacia servicios internos.

## Implementacion en el proyecto

En `ecom`, se usa Spring Cloud Gateway en `infra/gateway`, integrado con Eureka y rutas `lb://NOMBRE-SERVICIO`.

## Distribucion de carga

Laboratorio 4h:

- Levantar Config Server, Eureka y Gateway.
- Configurar rutas.
- Probar endpoints por Gateway.
- Levantar multiples instancias y observar balanceo.
- Cerrar con infraestructura en produccion local con Docker.

Trabajo fuera del aula 4h:

- Agregar ruta de otro microservicio.
- Documentar pruebas PowerShell/bash.
- Registrar evidencia de balanceo.
- Explicar diferencia entre Gateway y microservicio.

## Pasos para construir el producto de sesion

1. Levantar Config Server.
2. Levantar Eureka.
3. Crear o revisar `infra/gateway`.
4. Configurar rutas por nombre logico.
5. Levantar `catalogo-ms`.
6. Levantar dos instancias de `catalogo-ms`.
7. Probar endpoint por Gateway.
8. Repetir llamadas a endpoint de instancia.
9. Verificar respuestas desde instancias distintas.
10. Ejecutar Gateway en produccion local con Docker.
11. Documentar rutas y comandos.

## Archivos involucrados

| Archivo | Proposito |
|---|---|
| `infra/gateway/pom.xml` | Dependencias Gateway |
| `infra/config/config-repo/gateway-dev.yml` | Rutas DEV |
| `infra/config/config-repo/gateway-prod.yml` | Rutas PROD |
| `services/*/README.md` | Pruebas por Gateway |

## Comandos de ejecucion

PowerShell:

```powershell
cd infra/gateway
mvn spring-boot:run

Invoke-RestMethod -Method Get -Uri "http://localhost:18080/actuator/health"
Invoke-RestMethod -Method Get -Uri "http://localhost:18080/api/v1/categorias"
Invoke-RestMethod -Method Get -Uri "http://localhost:18080/api/v1/catalogo/instancia"
```

bash macOS/Linux:

```bash
cd infra/gateway
mvn spring-boot:run

curl -i "http://localhost:18080/actuator/health"
curl -s "http://localhost:18080/api/v1/categorias"
curl -s "http://localhost:18080/api/v1/catalogo/instancia"
```

## Cierre en produccion local con Docker

```bash
cd infra
docker compose up -d --build
```

En produccion local, Gateway publica el health en `http://localhost:28082/actuator/health` y enruta hacia servicios dentro de la red Docker. Las aplicaciones `*-ms` no exponen puerto host fijo; el acceso funcional pasa por Gateway.

## Verificacion funcional

- Gateway DEV health: `http://localhost:18080/actuator/health`.
- Gateway PROD health: `http://localhost:28082/actuator/health`.
- CRUD de categorias por Gateway.
- Endpoint de instancia responde desde replicas distintas.

## Observabilidad y diagnostico

- Gateway health.
- Logs de rutas.
- Eureka con instancias disponibles.
- Respuestas repetidas al endpoint `/instancia`.

## Verificacion de base de datos

La prueba CRUD por Gateway debe reflejar registros en la BD del microservicio correspondiente.

```powershell
docker exec -it lizard-postgres-catalogo-dev psql -U ecom -d lizard_catalogo_db -c "SELECT * FROM categorias;"
```

## Evidencia esperada

- Gateway operativo.
- Ruta funcionando por Gateway.
- Balanceo demostrado.
- Registro en BD.
- Evidencia individual del aporte.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| 503 | Servicio no registrado | Revisar Eureka |
| 404 | Ruta mal configurada | Revisar predicates del Gateway |
| No balancea | Solo hay una instancia | Levantar segunda instancia |

## Preguntas de defensa

1. Por que el cliente no debe conocer todos los microservicios?
2. Como funciona `lb://`?
3. Como evidencias distribucion de carga?
4. Que diagnosticas ante un 503 del Gateway?

## Checklist de cierre

- [ ] Gateway activo.
- [ ] Ruta probada.
- [ ] Dos instancias disponibles.
- [ ] Balanceo evidenciado.
- [ ] Evidencia individual registrada.
