# S7 - Seguridad distribuida y control de acceso

## Ubicacion en el curso

- Unidad: U2 - Sistema distribuido robusto.
- Producto de unidad: sistema seguro, resiliente, consistente, observable e integrado.
- Avance del producto en esta sesion: autenticacion, token y proteccion de rutas.

## Proposito

Controlar quien puede acceder al sistema distribuido y evitar que las rutas internas queden expuestas sin autorizacion.

## Resultado de aprendizaje

El estudiante implementa login, emision de token, validacion en el punto de entrada y consumo de rutas protegidas.

## Producto de sesion

`auth-ms` emite JWT y Gateway valida tokens para permitir o rechazar rutas protegidas.

## Concepto distribuido clave

La seguridad distribuida debe aplicarse de forma consistente en el borde del sistema y propagarse hacia los servicios segun el nivel de confianza requerido.

## Implementacion en el proyecto

En `ecom`, la autenticacion se implementa en `auth-ms` y la validacion de JWT en `infra/gateway`. El secreto debe coincidir entre ambos componentes.

## Distribucion de carga

Laboratorio 4h:

- Levantar `auth-ms`.
- Ejecutar login.
- Proteger rutas.
- Probar 401 y acceso con token.
- Cerrar con `auth-ms` y Gateway en produccion local con Docker.

Trabajo fuera del aula 4h:

- Documentar pruebas con PowerShell/bash.
- Verificar tablas de usuarios y roles.
- Explicar flujo de token.
- Registrar evidencia individual.

## Pasos para construir el producto de sesion

1. Revisar modelo de usuarios y roles.
2. Configurar BD de `auth-ms`.
3. Implementar o revisar endpoint `/auth/login`.
4. Generar JWT al autenticar.
5. Configurar secreto compartido.
6. Configurar filtro de validacion en Gateway.
7. Definir rutas publicas y protegidas.
8. Probar ruta protegida sin token.
9. Probar login.
10. Probar ruta protegida con token.
11. Ejecutar cierre en produccion local con Docker.
12. Inspeccionar BD de usuarios.

## Archivos involucrados

| Archivo | Proposito |
|---|---|
| `services/auth-ms` | Autenticacion y emision de JWT |
| `infra/gateway` | Validacion de JWT |
| `services/auth-ms/.env` | Secreto de JWT |
| `infra/.env` | Secreto usado por Gateway |
| `infra/config/config-repo/gateway-*.yml` | Rutas publicas/protegidas |

## Comandos de ejecucion

PowerShell:

```powershell
$body = @{
  username = "admin"
  password = "admin123"
} | ConvertTo-Json

$response = Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:18080/auth/login" `
  -ContentType "application/json" `
  -Body $body

$token = $response.accessToken
$token
```

bash macOS/Linux:

```bash
response=$(curl -s -X POST "http://localhost:18080/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}')

token=$(echo "$response" | jq -r '.accessToken')
echo "$token"
```

## Cierre en produccion local con Docker

```bash
cd infra
docker compose up -d --build

cd ../services/auth-ms
docker compose up -d --build
```

En produccion local, `auth-ms` emite tokens y Gateway los valida desde contenedores. El `JWT_SECRET` de `infra/.env` y `services/auth-ms/.env` debe coincidir.

## Verificacion funcional

- Login devuelve `accessToken`.
- Ruta protegida sin token devuelve 401.
- Ruta protegida con token responde correctamente.
- Gateway health usa `http://localhost:18080/actuator/health` en DEV.

## Observabilidad y diagnostico

- Logs de `auth-ms`.
- Logs de Gateway.
- Respuesta 401 vs 200.
- Variables `JWT_SECRET`.

## Verificacion de base de datos

```powershell
docker exec -it lizard-postgres-auth-dev psql -U ecom -d lizard_auth_db -c "SELECT id, username, enabled, created_at FROM users;"
docker exec -it lizard-postgres-auth-dev psql -U ecom -d lizard_auth_db -c "SELECT * FROM roles;"
```

## Evidencia esperada

- Token emitido.
- Prueba 401 sin token.
- Prueba exitosa con token.
- BD de usuarios inspeccionada.
- Evidencia individual.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| Token invalido | Secreto distinto entre Auth y Gateway | Revisar `.env` |
| 401 permanente | Header Authorization mal enviado | Usar `Bearer $token` |
| Login falla | Usuario inicial no cargado | Revisar migraciones/seed |

## Preguntas de defensa

1. Que diferencia hay entre autenticacion y autorizacion?
2. Por que Gateway valida el token?
3. Que pasa si cambia `JWT_SECRET`?
4. Como evidencias que una ruta esta protegida?

## Checklist de cierre

- [ ] Login probado.
- [ ] JWT obtenido.
- [ ] Ruta protegida con 401.
- [ ] Ruta protegida con 200.
- [ ] Evidencia individual registrada.
