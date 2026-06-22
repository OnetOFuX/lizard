@echo off
echo ====================================================
echo INICIANDO LIZARD STORE - ENTORNO DEV COMPLETO
echo ====================================================

echo [1/5] Creando red Docker...
docker network create lizard-dev-net >nul 2>&1

echo [2/5] Levantando Kafka y Observabilidad...
docker compose -f compose-dev.yml up -d

echo [3/5] Levantando Bases de Datos PostgreSQL...
cd services\auth-ms
docker compose -f compose-dev.yml up -d
cd ..\catalogo-ms
docker compose -f compose-dev.yml up -d
cd ..\producto-ms
docker compose -f compose-dev.yml up -d
cd ..\carrito-ms
docker compose -f compose-dev.yml up -d
cd ..\orden-ms
docker compose -f compose-dev.yml up -d
cd ..\pago-ms
docker compose -f compose-dev.yml up -d
cd ..\..

echo Esperando 5 segundos a que las bases de datos inicien...
timeout /t 5 >nul

echo [4/5] Levantando Infraestructura Spring Boot...
start "Config Server" cmd /k "cd infra\config && mvnw spring-boot:run"
start "Eureka" cmd /k "cd infra\eureka && mvnw spring-boot:run"

echo Esperando 15 segundos a que Config y Eureka esten listos...
timeout /t 15 >nul

start "Gateway" cmd /k "cd infra\gateway && mvnw spring-boot:run"

echo [5/5] Levantando Microservicios
start "Auth-MS" cmd /k "cd services\auth-ms && mvnw spring-boot:run"
start "Catalogo-MS" cmd /k "cd services\catalogo-ms && mvnw spring-boot:run"
start "Producto-MS" cmd /k "cd services\producto-ms && mvnw spring-boot:run"
start "Carrito-MS" cmd /k "cd services\carrito-ms && mvnw spring-boot:run"
start "Orden-MS" cmd /k "cd services\orden-ms && mvnw spring-boot:run"
start "Pago-MS" cmd /k "cd services\pago-ms && mvnw spring-boot:run"


echo.
echo ====================================================
echo Todos los servicios han sido iniciados en ventanas separadas.
echo Angular estara disponible en http://localhost:4200
echo Eureka estara disponible en http://localhost:18761
echo ====================================================
pause
