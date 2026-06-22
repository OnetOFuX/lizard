# Rubrica del producto

## Criterios

| Criterio | Logro esperado |
|---|---|
| Arquitectura | Componentes separados y responsabilidades claras |
| Configuracion | Configuracion externa con perfiles `dev` y `prod` |
| Discovery | Servicios registrados dinamicamente |
| Gateway | Rutas, punto unico de acceso y distribucion de trafico |
| Escalado | Multiples instancias demostradas |
| Comunicacion | Comunicacion sincronica entre servicios |
| Resiliencia | Respuesta controlada ante fallos |
| Seguridad | Login, token y rutas protegidas |
| Eventos | Mensajeria asincrona con producer y consumer |
| Consistencia | Consistencia eventual, compensacion o idempotencia demostrada |
| Observabilidad | Health, logs, metricas o dashboards |
| Frontend | Cliente integrado por Gateway |
| Evidencias | Comandos, capturas, BD y logs |
| Defensa | Explicacion tecnica grupal con evidencia individual |

## Niveles

| Nivel | Descripcion |
|---|---|
| Excelente | Funciona, se diagnostica y se defiende con claridad |
| Logrado | Funciona y se explica correctamente |
| En proceso | Funciona parcialmente o falta evidencia |
| Insuficiente | No se puede ejecutar o no se comprende |

## Evidencias minimas

- Sistema levantado.
- Config Server consultable.
- Eureka con servicios.
- Gateway funcionando.
- CRUD probado.
- JWT probado.
- Evento probado.
- Escenario de consistencia distribuida probado.
- Registros en BD.
- Logs o metricas.
- Defensa tecnica individual.
