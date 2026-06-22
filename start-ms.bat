@echo off
start "Config Server" cmd /k "cd infra\config && mvnw spring-boot:run"
start "Eureka" cmd /k "cd infra\eureka && mvnw spring-boot:run"
start "Gateway" cmd /k "cd infra\gateway && mvnw spring-boot:run"
start "Auth-MS" cmd /k "cd services\auth-ms && mvnw spring-boot:run"
start "Catalogo-MS" cmd /k "cd services\catalogo-ms && mvnw spring-boot:run"
start "Producto-MS" cmd /k "cd services\producto-ms && mvnw spring-boot:run"
start "Carrito-MS" cmd /k "cd services\carrito-ms && mvnw spring-boot:run"
start "Orden-MS" cmd /k "cd services\orden-ms && mvnw spring-boot:run"
start "Pago-MS" cmd /k "cd services\pago-ms && mvnw spring-boot:run"
pause