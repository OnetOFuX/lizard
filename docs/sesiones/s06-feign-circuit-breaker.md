# S6 - Comunicacion sincronica resiliente entre servicios

## Ubicacion en el curso

- Unidad: U2 - Sistema distribuido robusto.
- Producto de unidad: sistema seguro, resiliente, consistente, observable e integrado.
- Avance del producto en esta sesion: operacion distribuida con comunicacion sincronica y respuesta controlada ante fallos.

## Proposito

Construir una operacion donde un microservicio requiere informacion de otro y controlar el impacto cuando el servicio remoto falla.

## Resultado de aprendizaje

El estudiante implementa comunicacion entre servicios, simula fallos y aplica una respuesta degradada defendible.

## Producto de sesion

`producto-ms` consulta `catalogo-ms` para mostrar detalle de producto con categoria y controla fallos con Circuit Breaker/fallback.

## Concepto distribuido clave

La comunicacion sincronica es simple de razonar, pero acopla temporalmente a los servicios. La resiliencia evita que una falla local se propague a todo el sistema.

## Implementacion en el proyecto

En `ecom`, la comunicacion se implementa con Spring Cloud OpenFeign y la resiliencia con Circuit Breaker sobre la llamada de `producto-ms` a `catalogo-ms`.

## Distribucion de carga

Laboratorio 4h:

- Crear cliente de comunicacion entre servicios.
- Integrar DTO de respuesta.
- Probar flujo con ambos servicios activos.
- Simular caida de `catalogo-ms` y validar fallback.
- Cerrar con los servicios en produccion local con Docker.

Trabajo fuera del aula 4h:

- Mejorar mensaje de respuesta degradada.
- Documentar pruebas y logs.
- Explicar trade-offs de comunicacion sincronica.
- Registrar evidencia individual.

## Pasos para construir el producto de sesion

1. Identificar la operacion que cruza servicios.
2. Agregar dependencia de comunicacion sincronica al consumidor.
3. Habilitar clientes declarativos.
4. Crear DTOs de categoria.
5. Crear cliente hacia `catalogo-ms` usando nombre logico.
6. Consumir el cliente desde el servicio de productos.
7. Exponer endpoint de detalle.
8. Agregar Circuit Breaker.
9. Implementar fallback.
10. Probar con `catalogo-ms` activo.
11. Apagar `catalogo-ms` y repetir la prueba.
12. Ejecutar cierre en produccion local con Docker.
13. Revisar logs y respuesta.

## Archivos involucrados

| Archivo | Proposito |
|---|---|
| `services/producto-ms/pom.xml` | Dependencias de comunicacion/resiliencia |
| `services/producto-ms/src/main/java/...` | Cliente, servicio y controlador |
| `infra/config/config-repo/producto-ms-dev.yml` | Configuracion de resiliencia |
| `services/catalogo-ms` | Servicio remoto |

## Comandos de ejecucion

PowerShell:

```powershell
cd services/producto-ms
mvn spring-boot:run

Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:18080/api/v1/productos/detalle/1" `
  -Headers @{ Authorization = "Bearer $token" }
```

bash macOS/Linux:

```bash
cd services/producto-ms
mvn spring-boot:run

curl -s "http://localhost:18080/api/v1/productos/detalle/1" \
  -H "Authorization: Bearer $token"
```

## Cierre en produccion local con Docker

```bash
cd services/catalogo-ms
docker compose up -d --build

cd ../producto-ms
docker compose up -d --build
```

En produccion local, `producto-ms` consume `catalogo-ms` dentro de la red Docker usando descubrimiento y Gateway. El acceso desde fuera se mantiene por Gateway PROD, no por puertos host de cada microservicio.

## Verificacion funcional

- Producto creado.
- Categoria creada.
- Detalle devuelve datos de producto y categoria.
- Al apagar `catalogo-ms`, la respuesta es controlada y no rompe todo el flujo.

## Observabilidad y diagnostico

- Logs de `producto-ms`.
- Logs de `catalogo-ms`.
- Estado de servicios en Eureka.
- Respuesta HTTP del endpoint de detalle.

## Verificacion de base de datos

```powershell
docker exec -it lizard-postgres-producto-dev psql -U ecom -d lizard_producto_db -c "SELECT * FROM productos;"
docker exec -it lizard-postgres-catalogo-dev psql -U ecom -d lizard_catalogo_db -c "SELECT * FROM categorias;"
```

## Evidencia esperada

- Endpoint de detalle funcionando.
- Fallback probado.
- Logs del fallo.
- Explicacion del impacto de la dependencia remota.
- Evidencia individual.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| 503 | Servicio remoto no registrado | Revisar Eureka |
| 401 | Falta token en ruta protegida | Iniciar sesion |
| Fallback no ejecuta | Circuit Breaker mal configurado | Revisar anotacion/config |

## Preguntas de defensa

1. Que diferencia hay entre comunicacion sincronica y asincrona?
2. Por que la llamada a `catalogo-ms` puede afectar a `producto-ms`?
3. Que aporta el Circuit Breaker?
4. Como demuestras que el fallback funciona?

## Checklist de cierre

- [ ] Comunicacion entre servicios implementada.
- [ ] Endpoint de detalle probado.
- [ ] Fallo simulado.
- [ ] Fallback evidenciado.
- [ ] Evidencia individual registrada.
