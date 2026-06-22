# S16 - Evaluacion final individual

## Ubicacion en el curso

- Unidad: U3 - Validacion y consolidacion del producto del curso.
- Producto de unidad: producto final del curso validado, documentado, estabilizado y defendido.
- Avance del producto en esta sesion: demostracion individual de competencias pendientes.

## Proposito

Brindar una instancia final para que estudiantes con competencias pendientes demuestren logro tecnico de forma individual.

## Resultado de aprendizaje

El estudiante demuestra que puede implementar, ejecutar, diagnosticar o defender una parte critica del sistema sin depender del grupo.

## Producto de sesion

Evidencia individual de logro de competencias pendientes.

## Concepto distribuido clave

La competencia profesional se demuestra cuando el estudiante puede explicar y operar una parte del sistema bajo condiciones controladas.

## Implementacion en el proyecto

El docente selecciona tareas sobre `ecom` o sobre el proyecto final del equipo: corregir una ruta, probar un endpoint, diagnosticar un 503, explicar un evento, revisar BD o defender un componente.

## Distribucion de carga

Laboratorio 4h:

- Evaluacion individual.
- Correccion o demostracion guiada por consigna.
- Sustentacion tecnica.

Trabajo fuera del aula 4h:

- Preparacion previa del estudiante.
- Revision de evidencias pendientes.
- Reentrega documentada si el docente lo permite.

## Pasos para construir el producto de sesion

1. Identificar competencia pendiente.
2. Asignar consigna individual.
3. Ejecutar sistema o modulo requerido.
4. Realizar prueba solicitada.
5. Diagnosticar si aparece un error.
6. Explicar decisiones y resultados.
7. Registrar evidencia final.

## Archivos involucrados

Segun competencia evaluada:

- `infra/*`
- `services/*`
- `kafka/*`
- `obs/*`
- `clients/ecom-ng`
- `docs/*`

## Comandos de ejecucion

El estudiante usa comandos documentados y explica que hace cada uno.

## Cierre en produccion local con Docker

Si la competencia evaluada incluye despliegue u operacion, el estudiante debe ejecutar el componente con Docker usando `docker compose up -d --build` y explicar la diferencia frente a DEV con Maven.

## Verificacion funcional

Segun consigna:

- Endpoint funcionando.
- Servicio registrado.
- Ruta por Gateway.
- Login/token.
- Evento procesado.
- Registro en BD.
- Log o metrica revisada.

## Observabilidad y diagnostico

La evaluacion puede incluir diagnosticar una falla provocada por el docente.

## Verificacion de base de datos

Si la consigna modifica datos, debe verificarse la tabla correspondiente.

## Evidencia esperada

- Ejecucion individual.
- Explicacion tecnica.
- Evidencia de comando, BD, log o UI.
- Correccion si aplica.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| Estudiante depende del equipo | No domina su aporte | Pedir tarea individual acotada |
| Respuesta teorica sin evidencia | Falta ejecucion | Exigir comando o log |
| No diagnostica | No sigue ruta de revision | Guiar por sintomas |

## Preguntas de defensa

1. Que competencia estas demostrando?
2. Que comando ejecutaste y por que?
3. Que evidencia confirma el resultado?
4. Como corregirias el fallo presentado?

## Checklist de cierre

- [ ] Competencia identificada.
- [ ] Consigna ejecutada.
- [ ] Evidencia presentada.
- [ ] Resultado individual registrado.
