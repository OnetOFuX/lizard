# S14 - Revision tecnica y estabilizacion del producto

## Ubicacion en el curso

- Unidad: U3 - Validacion y consolidacion del producto del curso.
- Producto de unidad: producto final del curso validado, documentado, estabilizado y defendido.
- Avance del producto en esta sesion: producto listo para defensa tecnica.

## Proposito

Cerrar brechas tecnicas, ordenar documentacion y asegurar que el producto sea reproducible por el docente.

## Resultado de aprendizaje

El estudiante estabiliza el producto, corrige fallos, documenta ejecucion y prepara defensa tecnica.

## Producto de sesion

Producto documentado, reproducible, depurado y con evidencias organizadas.

## Concepto distribuido clave

La calidad de un sistema distribuido tambien depende de su operabilidad: debe poder levantarse, probarse, diagnosticarse y explicarse.

## Implementacion en el proyecto

Se revisan README, comandos, variables, perfiles, puertos, rutas, evidencias, scripts y criterios de evaluacion de `ecom`.

## Distribucion de carga

Laboratorio 4h:

- Revisar checklist tecnico.
- Corregir errores de ejecucion.
- Ordenar evidencias.
- Ensayar defensa por roles.
- Revisar produccion local con Docker y, si el curso lo permite, preparacion para Kubernetes local.

Trabajo fuera del aula 4h:

- Completar README.
- Grabar o preparar demo.
- Cerrar issues.
- Preparar defensa individual.

## Pasos para construir el producto de sesion

1. Revisar estado del repositorio.
2. Revisar README principal.
3. Revisar README por modulo.
4. Verificar comandos PowerShell y bash.
5. Verificar variables de entorno.
6. Verificar perfiles DEV/PROD.
7. Verificar puertos y health.
8. Ejecutar prueba end-to-end.
9. Registrar errores pendientes.
10. Corregir fallos prioritarios.
11. Preparar guion de defensa.
12. Validar produccion local con Docker.
13. Opcionalmente preparar manifiestos o checklist para Kubernetes local.
14. Asignar evidencias por integrante.

## Archivos involucrados

- `README.md`
- `infra/README.md`
- `services/*/README.md`
- `clients/ecom-ng/README.md`
- `docs/`
- `.env.example`

## Comandos de ejecucion

Usar los comandos documentados. La sesion verifica que otra persona pueda seguirlos sin explicaciones externas.

## Cierre en produccion local con Docker

```bash
cd infra
docker compose up -d --build
```

La revision tecnica confirma que los README permiten levantar el producto en DEV y en produccion local con Docker. Si se incorpora Kubernetes en U3, se trabaja solo en local y como extension de orquestacion, no como despliegue en nube.

## Verificacion funcional

- Sistema levanta desde cero.
- Pruebas principales funcionan.
- README coincide con el codigo.
- Evidencias estan ordenadas.

## Observabilidad y diagnostico

La revision debe incluir al menos un caso de fallo documentado:

- Servicio caido.
- Token invalido.
- Evento no procesado.
- BD no disponible.

## Verificacion de base de datos

Verificar datos finales del flujo principal en las bases relevantes.

## Evidencia esperada

- README corregidos.
- Checklist tecnico.
- Lista de incidencias cerradas.
- Evidencias por integrante.
- Guion de defensa.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| README desactualizado | Cambios no documentados | Probar comandos reales |
| Demo fragil | Falta orden de arranque | Crear checklist |
| Aporte individual difuso | Roles no evidenciados | Asociar evidencia a integrante |

## Preguntas de defensa

1. Que cambio tecnico estabilizaste?
2. Como sabe el docente que el proyecto es reproducible?
3. Que evidencia individual presentaras?
4. Que riesgo tecnico queda y como lo mitigarias?

## Checklist de cierre

- [ ] README revisados.
- [ ] Pruebas reproducibles.
- [ ] Evidencias ordenadas.
- [ ] Defensa ensayada.
- [ ] Aportes individuales claros.
