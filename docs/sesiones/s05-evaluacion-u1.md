# S5 - Evaluacion integradora de sistema base

## Ubicacion en el curso

- Unidad: U1 - Sistema distribuido base orientado a produccion.
- Producto de unidad: sistema distribuido base funcional, configurable y preparado para multiples instancias.
- Avance del producto en esta sesion: evaluacion del producto U1.

## Proposito

Validar que los componentes base funcionan como sistema y que cada integrante puede sustentar su aporte.

## Resultado de aprendizaje

El estudiante demuestra ejecucion, prueba, diagnostico y defensa tecnica del sistema base.

## Producto de sesion

Producto U1 integrado: Config Server, Eureka, Gateway, microservicio de negocio, BD y multiples instancias.

## Concepto distribuido clave

Un sistema distribuido base no se valida por servicios aislados, sino por su comportamiento integrado.

## Implementacion en el proyecto

Se evalua `ecom` en modo DEV Maven y, si corresponde, una demostracion PROD Docker.

## Distribucion de carga

Laboratorio 4h:

- Levantar sistema base.
- Ejecutar pruebas por Gateway.
- Mostrar registro en Eureka y configuracion.
- Defender por equipo con preguntas individuales.

Trabajo fuera del aula 4h:

- Ordenar evidencias.
- Corregir errores detectados.
- Completar README del servicio asignado.
- Preparar defensa individual.

## Pasos para construir el producto de sesion

1. Preparar orden de arranque.
2. Levantar Config Server.
3. Levantar Eureka.
4. Levantar Gateway.
5. Levantar BD y microservicio.
6. Levantar segunda instancia.
7. Ejecutar CRUD por Gateway.
8. Verificar registro en Eureka.
9. Consultar configuracion por perfil.
10. Inspeccionar BD.
11. Registrar evidencias.
12. Ejecutar cierre en produccion local con Docker si aplica.
13. Defender aportes individuales.

## Archivos involucrados

Todo lo construido en U1:

- `infra/config`
- `infra/eureka`
- `infra/gateway`
- `services/catalogo-ms`
- `services/producto-ms` si fue usado como replica de aprendizaje

## Comandos de ejecucion

Usar los comandos documentados en los README de cada modulo y en las sesiones S1-S4.

## Cierre en produccion local con Docker

```bash
cd infra
docker compose up -d --build

cd ../services/catalogo-ms
docker compose up -d --build --scale catalogo-ms=2
```

En produccion local se valida que el sistema base tambien puede ejecutarse como contenedores: infraestructura en Docker, microservicios dentro de la red y acceso por Gateway PROD.

## Verificacion funcional

- Config consultable.
- Eureka con servicios.
- Gateway health.
- CRUD por Gateway.
- Balanceo evidenciado.
- BD con registros.

## Observabilidad y diagnostico

El estudiante debe explicar que revisa ante:

- Config Server apagado.
- Servicio no registrado.
- Gateway con 503.
- Error de conexion a BD.
- Puerto dinamico no identificado.

## Verificacion de base de datos

```powershell
docker exec -it lizard-postgres-catalogo-dev psql -U ecom -d lizard_catalogo_db -c "SELECT * FROM categorias;"
```

## Evidencia esperada

- Comandos ejecutados.
- Captura o salida de Config Server.
- Captura o salida de Eureka.
- Prueba por Gateway.
- Registro en BD.
- Evidencia individual de aporte.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| Demo no reproduce | Orden de arranque incompleto | Usar checklist |
| Un integrante no sustenta | Aporte no evidenciado | Pedir evidencia individual |
| Gateway falla | Servicio no registrado | Revisar Eureka |

## Preguntas de defensa

1. Cual fue tu aporte concreto en U1?
2. Como se levanta el sistema base?
3. Como se prueba sin Postman?
4. Como sabes que hay multiples instancias?
5. Que revisas si una ruta devuelve 503?

## Checklist de cierre

- [ ] Producto U1 levantado.
- [ ] Evidencias completas.
- [ ] Cada integrante sustento su aporte.
- [ ] Errores corregidos o documentados.
