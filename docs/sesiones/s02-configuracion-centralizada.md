# S2 - Gestion centralizada de configuracion y ambientes

## Ubicacion en el curso

- Unidad: U1 - Sistema distribuido base orientado a produccion.
- Producto de unidad: sistema configurable, consistente entre ambientes y observable desde etapas tempranas.
- Avance del producto en esta sesion: configuracion externa para `dev` y `prod`.

## Proposito

Evitar configuracion duplicada o embebida en cada servicio, centralizando valores por ambiente y preparando la operacion del sistema.

## Resultado de aprendizaje

El estudiante externaliza configuracion, consulta perfiles por HTTP y verifica que los microservicios arrancan con valores provistos por un servidor central.

## Producto de sesion

Config Server operativo con perfiles `dev` y `prod` para infraestructura y microservicios.

## Concepto distribuido clave

La configuracion centralizada permite consistencia, trazabilidad y cambios por ambiente sin recompilar servicios.

## Implementacion en el proyecto

En `ecom`, se usa Spring Cloud Config Server en `infra/config`. El repositorio local de configuracion vive dentro de `infra/config/config-repo` para reducir dispersion de archivos.

## Distribucion de carga

Laboratorio 4h:

- Levantar Config Server.
- Revisar archivos `*-dev.yml` y `*-prod.yml`.
- Conectar un microservicio al servidor de configuracion.
- Verificar health y logs.
- Cerrar con Config Server en produccion local con Docker.

Trabajo fuera del aula 4h:

- Completar configuracion de otro microservicio.
- Documentar diferencias DEV/PROD.
- Registrar evidencias de consultas Config Server.
- Explicar que valores no deben quedar hardcodeados.

## Pasos para construir el producto de sesion

1. Crear o revisar `infra/config`.
2. Habilitar Spring Cloud Config Server.
3. Configurar repositorio nativo en `infra/config/config-repo`.
4. Crear archivos por servicio y perfil.
5. Mover puertos, credenciales, Eureka, Actuator y rutas al repositorio de configuracion.
6. Configurar `spring.config.import` en microservicios.
7. Levantar Config Server.
8. Consultar perfiles por HTTP.
9. Levantar un microservicio y verificar que lee configuracion externa.
10. Ejecutar Config Server en produccion local con Docker.
11. Registrar evidencia de health y logs.

## Archivos involucrados

| Archivo | Proposito |
|---|---|
| `infra/config/pom.xml` | Dependencias de Config Server |
| `infra/config/src/main/resources/application.yml` | Configuracion del servidor |
| `infra/config/config-repo/*-dev.yml` | Configuracion DEV |
| `infra/config/config-repo/*-prod.yml` | Configuracion PROD |
| `services/*/src/main/resources/application.yml` | Importacion de configuracion |

## Comandos de ejecucion

PowerShell / bash macOS/Linux:

```bash
cd infra/config
mvn spring-boot:run
```

## Cierre en produccion local con Docker

```bash
cd infra
docker compose up -d --build
```

En produccion local, el Config Server corre como contenedor y publica el puerto PROD `28888`. Los microservicios Docker consultan perfiles `prod` desde este servidor.

## Verificacion funcional

DEV:

```text
http://localhost:18888/catalogo-ms/dev
http://localhost:18888/producto-ms/dev
```

PROD:

```text
http://localhost:28888/catalogo-ms/prod
http://localhost:28888/producto-ms/prod
```

## Observabilidad y diagnostico

- Health DEV: `http://localhost:18888/actuator/health`.
- Revisar logs de lectura del repositorio local.
- Confirmar perfil activo y nombre de aplicacion.

## Verificacion de base de datos

No se modifica la BD en esta sesion. Se verifica que las credenciales y URL de BD provienen de configuracion externa.

## Evidencia esperada

- Config Server levantado.
- Perfil `dev` consultado.
- Perfil `prod` consultado.
- Microservicio arrancando con configuracion externa.
- Explicacion individual de un valor externalizado.

## Errores frecuentes

| Problema | Causa probable | Solucion |
|---|---|---|
| 404 al consultar perfil | Nombre de archivo incorrecto | Revisar `application-name-profile.yml` |
| Servicio no arranca | Config Server apagado | Levantar `infra/config` primero |
| Valores no cambian | Perfil incorrecto | Revisar `spring.profiles.active` |

## Preguntas de defensa

1. Que problema resuelve la configuracion centralizada?
2. Que diferencia hay entre `dev` y `prod`?
3. Donde vive `config-repo` en `ecom`?
4. Como diagnosticas un perfil no encontrado?

## Checklist de cierre

- [ ] Config Server activo.
- [ ] Perfiles consultados.
- [ ] Microservicio conectado.
- [ ] Health revisado.
- [ ] Evidencia individual registrada.
