# S11 - Integracion con cliente frontend

## Ubicacion en el curso

- Unidad: U2 - Sistema distribuido robusto.
- Producto de unidad: sistema seguro, resiliente, consistente, observable e integrado.
- Avance del producto en esta sesion: cliente web consumiendo el sistema por el punto unico de acceso.

## Proposito

Integrar el frontend con el sistema distribuido sin consumir microservicios directamente.

## Resultado de aprendizaje

El estudiante configura el cliente, resuelve CORS, ejecuta login, usa token y consume operaciones protegidas por Gateway.

## Producto de sesion

`clients/ecom-ng` integrado con Gateway DEV, login JWT, interceptor, guardas y CRUD basico.

## Concepto distribuido clave

El cliente frontend debe interactuar con el borde del sistema. El Gateway centraliza rutas, seguridad y politicas de acceso.

## Implementacion en el proyecto

En `ecom`, el cliente es Angular en `clients/ecom-ng` y apunta a `http://localhost:18080` en DEV.

## Distribucion de carga

Laboratorio 4h:

- Configurar URL del Gateway.
- Ejecutar login desde Angular.
- Agregar token a peticiones.
- Probar CRUD desde UI.
- Cerrar con build local del cliente y backend en produccion local.

Trabajo fuera del aula 4h:

- Mejorar evidencias de UI.
- Documentar errores CORS/401.
- Preparar explicacion del flujo cliente-Gateway.
- Registrar aporte individual.

## Pasos para construir el producto de sesion

1. Revisar `clients/ecom-ng`.
2. Configurar `environment` con Gateway DEV.
3. Crear o revisar servicio de autenticacion.
4. Consumir `/auth/login`.
5. Guardar token.
6. Crear interceptor HTTP.
7. Crear guardas de rutas.
8. Configurar CORS en Gateway si aplica.
9. Consumir CRUD de categorias/productos.
10. Verificar peticiones en DevTools.
11. Ejecutar cierre de produccion local del backend.
12. Documentar errores y solucion.

## Archivos involucrados

| Archivo | Proposito |
|---|---|
| `clients/ecom-ng` | Cliente Angular |
| `infra/config/config-repo/gateway-dev.yml` | CORS y rutas |
| `services/auth-ms` | Login |
| `services/catalogo-ms` | CRUD categorias |
| `services/producto-ms` | CRUD productos |

## Comandos de ejecucion

PowerShell / bash macOS/Linux:

```bash
cd clients/ecom-ng
npm install
npm start
```

## Cierre en produccion local con Docker

```bash
cd infra
docker compose up -d --build
```

El cliente Angular se trabaja en DEV contra `http://localhost:18080`. Para produccion local se valida el backend en Docker por Gateway PROD; el empaquetado del frontend puede revisarse despues con `npm run build`.

## Verificacion funcional

- Angular abre en `http://localhost:4200`.
- Frontend apunta a `http://localhost:18080`.
- Login obtiene token.
- Peticiones protegidas llevan `Authorization: Bearer`.
- CRUD funciona desde UI.

## Observabilidad y diagnostico

- DevTools Network.
- Logs de Gateway.
- Logs de `auth-ms`.
- Respuestas 401, 403, 404 o CORS.

## Verificacion de base de datos

Verificar que operaciones realizadas desde UI llegan a BD:

```powershell
docker exec -it lizard-postgres-catalogo-dev psql -U ecom -d lizard_catalogo_db -c "SELECT * FROM categorias;"
docker exec -it lizard-postgres-producto-dev psql -U ecom -d lizard_producto_db -c "SELECT * FROM productos;"
```

## Evidencia esperada

- Pantalla de login.
- Token usado en peticiones.
- CRUD desde UI.
- Registro en BD.
- Evidencia individual.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| CORS | Gateway no permite origen | Revisar configuracion CORS |
| 401 | Token no enviado | Revisar interceptor |
| Frontend usa puerto incorrecto | Environment mal configurado | Usar Gateway DEV `18080` |

## Preguntas de defensa

1. Por que Angular consume Gateway y no microservicios?
2. Que hace el interceptor?
3. Como diagnosticas un error CORS?
4. Como evidencias que el CRUD llego a BD?

## Checklist de cierre

- [ ] Angular ejecutando.
- [ ] Login probado.
- [ ] Token agregado.
- [ ] CRUD probado.
- [ ] Evidencia individual registrada.
