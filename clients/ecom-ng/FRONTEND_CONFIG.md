# Lizard Store - Configuracion del Frontend

## Acceso al Panel de Administracion

### Ruta

El panel de administracion se encuentra en una ruta oculta que no aparece en la
navegacion principal de la tienda.

```
http://localhost:4200/zona-gestion
```

### Credenciales

| Campo    | Valor      |
|----------|------------|
| Usuario  | `admin`    |
| Password | `admin123` |

El usuario debe tener el rol `ROLE_ADMIN` o `ADMIN` en el microservicio
`auth-ms` para poder acceder al panel. Si las credenciales no funcionan,
verificar que el usuario exista en la base de datos del servicio de
autenticacion con el rol correspondiente.

### Funcionalidades del panel

- **Productos**: Crear, editar, eliminar y listar todos los productos
- **Categorias**: Crear, editar, eliminar y listar todas las categorias
- **Ordenes**: Visualizar todas las ordenes del sistema con su estado

---

## Configuracion del Entorno

### API Backend

La URL base de la API se configura en:

```
src/environments/environment.ts
```

Valor actual:

```typescript
export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:18080',
};
```

Si el backend corre en otro puerto, modificar `apiBaseUrl` acorde.

### Endpoints utilizados

| Servicio       | Endpoint                                        | Metodos         |
|----------------|------------------------------------------------|-----------------|
| Autenticacion  | `/auth/login`                                  | POST            |
| Productos      | `/api/v1/productos`                            | GET, POST       |
| Productos      | `/api/v1/productos/{id}`                       | PUT, DELETE      |
| Categorias     | `/api/v1/categorias`                           | GET, POST       |
| Categorias     | `/api/v1/categorias/{id}`                      | PUT, DELETE      |
| Carrito        | `/api/v1/carritos/usuario/{userId}`            | GET, DELETE      |
| Carrito Items  | `/api/v1/carritos/usuario/{userId}/items`      | POST            |
| Carrito Items  | `/api/v1/carritos/usuario/{userId}/items/{id}` | PUT, DELETE      |
| Checkout       | `/api/v1/carritos/usuario/{userId}/checkout`   | POST            |
| Ordenes        | `/api/v1/ordenes`                              | GET             |

---

## Rutas de la Aplicacion

| Ruta             | Descripcion                | Acceso          |
|------------------|----------------------------|-----------------|
| `/`              | Pagina de inicio           | Publica         |
| `/productos`     | Catalogo de productos      | Publica         |
| `/categorias`    | Listado de categorias      | Publica         |
| `/auth`          | Inicio de sesion           | Publica         |
| `/carrito`       | Carrito de compras         | Requiere login  |
| `/ordenes`       | Historial de ordenes       | Requiere login  |
| `/zona-gestion`  | Panel de administracion    | Requiere ADMIN  |

---

## Comandos de Desarrollo

```bash
# Instalar dependencias
npm install

# Servidor de desarrollo
npm start
# o
npx ng serve

# Build de produccion
npx ng build

# Tests
npx ng test
```

La aplicacion se sirve por defecto en `http://localhost:4200`.

---

## Stack Tecnologico

| Tecnologia     | Version |
|----------------|---------|
| Angular        | 21.2.0  |
| TypeScript     | 5.9.2   |
| RxJS           | 7.8.x   |
| SCSS           | Nativo  |
| Fuente         | Inter (Google Fonts) |
