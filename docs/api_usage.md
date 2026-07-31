# Manual de uso de la API — Agro Vivero Backend

## Base URL

```
http://localhost:3000/api
```

El puerto viene de la variable de entorno `PORT` (por defecto `3000`). Todas las rutas están montadas bajo el prefijo `/api`.

## Autenticación

La API usa JWT (JSON Web Token).

1. Inicia sesión en `POST /api/auth/login` con tu correo y contraseña.
2. Guarda el `token` que devuelve la respuesta.
3. Envíalo en cada petición a una ruta protegida con el header:

```
Authorization: Bearer <token>
```

Algunas rutas, además del token, requieren que el usuario tenga un rol específico (`SUPER_ADMIN` o `EDITOR`), verificado a partir del payload del token.

### Ejemplo de login

```bash
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"correo":"admin@example.com","password":"secret"}'
```

### Ejemplo de petición autenticada

```bash
curl http://localhost:3000/api/usuarios-admin \
  -H "Authorization: Bearer <token>"
```

## Endpoints

### Health check

| Método | Ruta | Auth | Rol |
|---|---|---|---|
| GET | `/api/health` | No | - |

Respuesta: `{ "status": "OK", "message": "Backend funcionando" }`

### Auth

| Método | Ruta | Auth | Rol | Body |
|---|---|---|---|---|
| POST | `/api/auth/login` | No | - | `{ "correo": string, "password": string }` |

### Configuración

| Método | Ruta | Auth | Rol |
|---|---|---|---|
| GET | `/api/configuracion/` | No | - |
| PUT | `/api/configuracion/` | Sí | `SUPER_ADMIN` |

### Fuentes

| Método | Ruta | Auth | Rol |
|---|---|---|---|
| GET | `/api/fuentes/` | No | - |
| POST | `/api/fuentes/` | No | - |

### Auditoría

| Método | Ruta | Auth | Rol |
|---|---|---|---|
| GET | `/api/auditoria/` | Sí | `SUPER_ADMIN` |
| POST | `/api/auditoria/` | No | - |

### Plantas

| Método | Ruta | Auth | Rol |
|---|---|---|---|
| GET | `/api/plantas/` | No | - |
| GET | `/api/plantas/:slug` | No | - |
| POST | `/api/plantas/` | Sí | `SUPER_ADMIN` o `EDITOR` |
| PUT | `/api/plantas/:id` | Sí | `SUPER_ADMIN` o `EDITOR` |
| DELETE | `/api/plantas/:id` | Sí | `SUPER_ADMIN` o `EDITOR` |

### Usuarios Admin

| Método | Ruta | Auth | Rol |
|---|---|---|---|
| GET | `/api/usuarios-admin/` | Sí | cualquiera autenticado |
| GET | `/api/usuarios-admin/:id` | Sí | cualquiera autenticado |
| POST | `/api/usuarios-admin/` | Sí | `SUPER_ADMIN` |
| PUT | `/api/usuarios-admin/:id` | Sí | `SUPER_ADMIN` |
| DELETE | `/api/usuarios-admin/:id` | Sí | `SUPER_ADMIN` |

### Multimedia

| Método | Ruta | Auth | Rol |
|---|---|---|---|
| GET | `/api/multimedia/` | No | - |
| GET | `/api/multimedia/:id` | No | - |
| POST | `/api/multimedia/` | Sí | `SUPER_ADMIN` o `EDITOR` |
| PUT | `/api/multimedia/:id` | Sí | `SUPER_ADMIN` o `EDITOR` |
| DELETE | `/api/multimedia/:id` | Sí | `SUPER_ADMIN` o `EDITOR` |

### Noticias

| Método | Ruta | Auth | Rol |
|---|---|---|---|
| GET | `/api/noticias/` | No | - |
| GET | `/api/noticias/:id` | No | - |
| POST | `/api/noticias/` | Sí | `SUPER_ADMIN` o `EDITOR` |
| PUT | `/api/noticias/:id` | Sí | `SUPER_ADMIN` o `EDITOR` |
| DELETE | `/api/noticias/:id` | Sí | `SUPER_ADMIN` o `EDITOR` |

### Comtrade

| Método | Ruta | Auth | Rol |
|---|---|---|---|
| GET | `/api/comtrade/catalogo` | No | - |
| GET | `/api/comtrade/:plantaSlug` | No | - |
| GET | `/api/comtrade/consulta/:plantaSlug` | No | - |

## Notas

- Las rutas `POST /api/fuentes/` y `POST /api/auditoria/` no exigen token, a diferencia del resto de módulos donde crear/editar/borrar sí requiere autenticación y rol. Confirmar si es intencional antes de exponer la API en producción.
- Los errores de validación (`express-validator`) y otros errores pasan por un middleware de manejo de errores central (`errorHandler`), que responde con el detalle del error correspondiente.
- El middleware de rate limiting (`apiLimiter`) aplica a toda la API bajo `/api`.
