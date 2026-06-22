# S13 - Validacion end-to-end del producto del curso

## Ubicacion en el curso

- Unidad: U3 - Validacion y consolidacion del producto del curso.
- Producto de unidad: producto final del curso validado, documentado, estabilizado y defendido.
- Avance del producto en esta sesion: pruebas integrales desde cliente hasta servicios, eventos y base de datos.

## Proposito

Probar el producto del curso como sistema completo y no como piezas aisladas.

## Resultado de aprendizaje

El estudiante ejecuta flujos end-to-end, verifica datos en cada capa y produce evidencias reproducibles.

## Producto de sesion

Checklist end-to-end del producto final del curso.

## Concepto distribuido clave

La validacion integral confirma que los componentes cooperan correctamente bajo un flujo de negocio completo.

## Implementacion en el proyecto

En `ecom`, se validan flujos desde Angular o shell hacia Gateway, seguridad, servicios, mensajeria, consistencia y BD.

## Distribucion de carga

Laboratorio 4h:

- Ejecutar flujo completo.
- Verificar cada salto del sistema.
- Registrar evidencias.
- Identificar puntos debiles.

Trabajo fuera del aula 4h:

- Corregir fallos.
- Completar documentacion.
- Preparar demo final.
- Registrar aporte individual.

## Pasos para construir el producto de sesion

1. Definir flujo end-to-end principal.
2. Preparar datos base.
3. Ejecutar login.
4. Ejecutar operaciones CRUD.
5. Ejecutar flujo con comunicacion entre servicios.
6. Ejecutar flujo con mensajeria.
7. Validar consistencia final.
8. Verificar resultados en BD.
9. Revisar logs y metricas.
10. Guardar evidencias reproducibles.
11. Ejecutar cierre en produccion local con Docker.
12. Asignar responsables por evidencia.

## Archivos involucrados

Todo el proyecto `ecom`.

## Comandos de ejecucion

Usar README de:

- `infra/`
- `services/*`
- `kafka/`
- `obs/`
- `clients/ecom-ng`

## Cierre en produccion local con Docker

```bash
cd infra
docker compose up -d --build

cd ../kafka
docker compose up -d --build

cd ../obs
docker compose up -d --build
```

La validacion end-to-end se realiza primero en DEV. Al cierre se comprueba produccion local con Docker para verificar que el producto tambien opera con contenedores y perfiles `prod`.

## Verificacion funcional

Flujo minimo:

1. Login.
2. Crear categoria.
3. Crear producto.
4. Consultar detalle.
5. Crear orden.
6. Ver pago generado.
7. Revisar estado final.

## Observabilidad y diagnostico

- Eureka.
- Gateway.
- Kafka UI.
- Logs.
- Metricas.
- BD.
- Angular DevTools.

## Verificacion de base de datos

Debe existir evidencia en categorias, productos, ordenes y pagos.

## Evidencia esperada

- Checklist completado.
- Salidas de comandos.
- Capturas o registros de cada componente.
- Incidencias encontradas y solucionadas.
- Evidencia individual.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| Flujo parcial | Falta servicio o dato base | Revisar orden de arranque |
| Evidencia incompleta | No se verifico BD/logs | Usar checklist |
| Demo depende de memoria | Falta README | Documentar pasos |

## Preguntas de defensa

1. Cual es el flujo end-to-end principal?
2. Donde se valida seguridad?
3. Donde ocurre consistencia eventual?
4. Como demuestras que el flujo llego a BD?
5. Que aportaste individualmente a la validacion?

## Checklist de cierre

- [ ] Flujo completo probado.
- [ ] Evidencias por capa.
- [ ] Incidencias registradas.
- [ ] Aporte individual documentado.
