# S12 - Evaluacion integradora de sistema robusto

## Ubicacion en el curso

- Unidad: U2 - Sistema distribuido robusto.
- Producto de unidad: sistema seguro, resiliente, consistente, observable e integrado.
- Avance del producto en esta sesion: evaluacion del producto U2.

## Proposito

Validar que el sistema soporta seguridad, resiliencia, mensajeria, consistencia, observabilidad e integracion frontend.

## Resultado de aprendizaje

El estudiante demuestra competencias tecnicas en condiciones reales de uso y fallo, con defensa individual de su aporte.

## Producto de sesion

Producto U2 robusto validado con evidencias grupales e individuales.

## Concepto distribuido clave

La robustez se demuestra cuando el sistema sigue siendo entendible, diagnosticable y recuperable ante fallos.

## Implementacion en el proyecto

Se integra lo construido en S6-S11 sobre `ecom`.

## Distribucion de carga

Laboratorio 4h:

- Ejecutar demostracion integrada.
- Simular fallos.
- Mostrar evidencias.
- Defender aportes individuales.

Trabajo fuera del aula 4h:

- Corregir observaciones.
- Completar bitacora de evidencias.
- Preparar integracion final de U3.
- Reforzar competencias pendientes.

## Pasos para construir el producto de sesion

1. Preparar orden de arranque completo.
2. Crear datos iniciales.
3. Ejecutar login y rutas protegidas.
4. Probar comunicacion sincronica resiliente.
5. Probar mensajeria asincrona.
6. Probar consistencia distribuida.
7. Probar observabilidad.
8. Probar frontend.
9. Simular al menos un fallo.
10. Registrar evidencias.
11. Ejecutar cierre en produccion local con Docker.
12. Sustentar aportes individuales.

## Archivos involucrados

Todo el sistema `ecom`.

## Comandos de ejecucion

Usar README de infraestructura, servicios, Kafka, observabilidad y cliente frontend.

## Cierre en produccion local con Docker

```bash
cd infra
docker compose up -d --build

cd ../kafka
docker compose up -d --build
```

Luego se levantan los servicios necesarios con sus `docker compose up -d --build`. La validacion PROD local comprueba que el producto U2 funciona como contenedores, por Gateway PROD y con red Docker interna.

## Verificacion funcional

- Login.
- CRUD.
- Comunicacion entre servicios.
- Respuesta controlada ante fallo.
- Evento publicado y consumido.
- Estado consistente.
- Observabilidad.
- Frontend.

## Observabilidad y diagnostico

El estudiante debe explicar que revisa ante:

- 401.
- 503.
- Evento no consumido.
- Servicio no registrado.
- Duplicado de mensaje.
- Error de base de datos.

## Verificacion de base de datos

Debe verificar registros en al menos tres bases: auth, catalogo/producto y orden/pago.

## Evidencia esperada

- Salidas de comandos.
- Capturas de Eureka, Gateway, Angular, Kafka UI y observabilidad.
- Evidencia de BD.
- Bitacora individual de aporte.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| Sistema incompleto | Servicio faltante | Levantar por orden |
| Demo no reproducible | Falta documentacion | Usar README y checklist |
| Nota individual baja | Aporte no evidenciado | Sustentar tarea concreta |

## Preguntas de defensa

1. Cual fue tu aporte tecnico en U2?
2. Como fluye una peticion desde frontend hasta BD?
3. Como se evidencia seguridad?
4. Como se evidencia consistencia distribuida?
5. Como diagnosticas un fallo real?

## Checklist de cierre

- [ ] Producto U2 levantado.
- [ ] Evidencias grupales presentadas.
- [ ] Evidencias individuales presentadas.
- [ ] Fallo simulado.
- [ ] Respuestas tecnicas sustentadas.
