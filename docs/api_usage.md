# Manual de uso de la API — Backend de Ecotec Flora Médica

## URL base

```
http://localhost:3000/api
```

El puerto proviene de la variable de entorno `PORT` (por defecto `3000`). Todas las rutas están montadas bajo el prefijo `/api`.

## Autenticación

La API usa JWT (JSON Web Token).

1. Inicia sesión en `POST /api/auth/login` con tu correo y contraseña.
2. Guarda el `token` que devuelve la respuesta.
3. Envíalo en cada petición a una ruta protegida con el encabezado:

```
Authorization: Bearer <token>
```

Algunas rutas, además del token, requieren que el usuario tenga un rol específico (`SUPER_ADMIN` o `EDITOR`), verificado a partir del payload del token.

> ⚠️ El enum de `rol` en el modelo Mongoose (`usuario-admin.model.js`) es `SUPER_ADMIN | EDITOR | CONSULTOR`, pero el validador de esquema a nivel de MongoDB en la colección `usuarios_admin` solo permite `ADMIN | EDITOR | MULTIMEDIA | DOCUMENTACION | LECTOR`. El único valor en común es `EDITOR`. Ver [Hallazgos de pruebas](#hallazgos-de-pruebas-2026-07-31) para el detalle e impacto.

### Ejemplo de inicio de sesión

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

### Comprobación de salud

| Método | Ruta | Autenticación | Rol |
|---|---|---|---|
| GET | `/api/health` | No | - |

Respuesta: `{ "status": "OK", "message": "Backend funcionando" }`

### Autenticación

| Método | Ruta | Autenticación | Rol | Cuerpo |
|---|---|---|---|---|
| POST | `/api/auth/login` | No | - | `{ "correo": string, "password": string }` |

### Configuración

| Método | Ruta | Autenticación | Rol |
|---|---|---|---|
| GET | `/api/configuracion/` | No | - |
| PUT | `/api/configuracion/` | Sí | `SUPER_ADMIN` |

### Fuentes

| Método | Ruta | Autenticación | Rol |
|---|---|---|---|
| GET | `/api/fuentes/` | No | - |
| POST | `/api/fuentes/` | No | - |

### Auditoría

| Método | Ruta | Autenticación | Rol |
|---|---|---|---|
| GET | `/api/auditoria/` | Sí | `SUPER_ADMIN` |
| POST | `/api/auditoria/` | No | - |

### Plantas

| Método | Ruta | Autenticación | Rol |
|---|---|---|---|
| GET | `/api/plantas/` | No | - |
| GET | `/api/plantas/:slug` | No | - |
| POST | `/api/plantas/` | Sí | `SUPER_ADMIN` o `EDITOR` |
| PUT | `/api/plantas/:id` | Sí | `SUPER_ADMIN` o `EDITOR` |
| DELETE | `/api/plantas/:id` | Sí | `SUPER_ADMIN` o `EDITOR` |

### Usuarios administradores

| Método | Ruta | Autenticación | Rol |
|---|---|---|---|
| GET | `/api/usuarios-admin/` | Sí | cualquier usuario autenticado |
| GET | `/api/usuarios-admin/:id` | Sí | cualquier usuario autenticado |
| POST | `/api/usuarios-admin/` | Sí | `SUPER_ADMIN` |
| PUT | `/api/usuarios-admin/:id` | Sí | `SUPER_ADMIN` |
| DELETE | `/api/usuarios-admin/:id` | Sí | `SUPER_ADMIN` |

### Multimedia

| Método | Ruta | Autenticación | Rol |
|---|---|---|---|
| GET | `/api/multimedia/` | No | - |
| GET | `/api/multimedia/:id` | No | - |
| POST | `/api/multimedia/` | Sí | `SUPER_ADMIN` o `EDITOR` |
| PUT | `/api/multimedia/:id` | Sí | `SUPER_ADMIN` o `EDITOR` |
| DELETE | `/api/multimedia/:id` | Sí | `SUPER_ADMIN` o `EDITOR` |

### Noticias

| Método | Ruta | Autenticación | Rol |
|---|---|---|---|
| GET | `/api/noticias/` | No | - |
| GET | `/api/noticias/:id` | No | - |
| POST | `/api/noticias/` | Sí | `SUPER_ADMIN` o `EDITOR` |
| PUT | `/api/noticias/:id` | Sí | `SUPER_ADMIN` o `EDITOR` |
| DELETE | `/api/noticias/:id` | Sí | `SUPER_ADMIN` o `EDITOR` |

### Comtrade

| Método | Ruta | Autenticación | Rol |
|---|---|---|---|
| GET | `/api/comtrade/catalogo` | No | - |
| GET | `/api/comtrade/:plantaSlug` | No | - |
| GET | `/api/comtrade/consulta/:plantaSlug` | No | - |

## Notas

- Las rutas `POST /api/fuentes/` y `POST /api/auditoria/` no exigen token, a diferencia del resto de módulos donde crear, editar y borrar sí requiere autenticación y rol. Confirma si esto es intencional antes de exponer la API en producción.
- Los errores de validación (`express-validator`) y otros errores pasan por un middleware central de manejo de errores (`errorHandler`), que responde con el detalle del error correspondiente.
- El middleware de limitación de tasa (`apiLimiter`) aplica a toda la API bajo `/api`.

## Hallazgos de pruebas (2026-07-31)

Se levantó el backend localmente (`node src/server.js`, con MongoDB local ya poblado en `agro_vivero_medicinal_db`) y se probó cada endpoint con `curl`, incluyendo casos con y sin autenticación/rol. Todos los datos de prueba creados durante esta sesión fueron eliminados al finalizar; la base quedó igual que al inicio. A continuación, los resultados agrupados por severidad.

### 🔴 Errores que rompen funcionalidad documentada

1. **`GET/POST/PUT/DELETE /api/multimedia/*` apuntan a la colección equivocada de MongoDB.**
   El modelo `multimedia.model.js` no define `collection`, por lo que Mongoose usa el nombre pluralizado por defecto `multimedias`. Pero los 34 registros reales (y el validador de esquema de Mongo) viven en la colección `multimedia` (singular), con una estructura totalmente distinta (`cloudinary: { secureUrl, publicId, resourceType }`, `plantaSlug`, `noticiaSlug`, `tipo` con `AUDIO` incluido).
   - Comprobado: `GET /api/multimedia/` devuelve `[]` aunque existen 34 documentos reales en `multimedia`.
   - Comprobado: `GET /api/multimedia/:id` con un `_id` real devuelve 404 `{"mensaje":"Registro no encontrado"}`.
   - Comprobado: `POST /api/multimedia/` (rol `EDITOR`) sí devuelve 201, pero el documento se guarda en la colección huérfana `multimedias`, invisible para el resto de la app.
   - **Impacto:** el módulo de multimedia no funciona contra los datos reales. Requiere agregar `{ collection: "multimedia" }` al esquema y alinear los campos del modelo/validación con la estructura real (`cloudinary.*`, `plantaSlug`, `noticiaSlug`, tipo `AUDIO`).

2. **`POST /api/usuarios-admin/` no puede crear usuarios con los datos que la propia validación exige.**
   `express-validator` exige `correo`, `passwordHash` (contraseña en texto plano), `nombreCompleto` y opcionalmente `rol` en `SUPER_ADMIN|EDITOR|CONSULTOR`. Pero el validador de esquema de MongoDB en `usuarios_admin` exige el campo `nombre` (no `nombreCompleto`) y solo permite `rol` en `ADMIN|EDITOR|MULTIMEDIA|DOCUMENTACION|LECTOR`.
   - Comprobado: enviando el cuerpo documentado con `rol: "SUPER_ADMIN"` → `500 {"message":"Document failed validation"}`.
   - Comprobado: enviando el mismo cuerpo con `rol: "EDITOR"` (el único rol común a ambos esquemas) → **también falla** con `500 {"message":"Document failed validation"}`, porque sigue faltando `nombre`.
   - **Impacto:** actualmente no existe ninguna combinación de campos documentados que permita crear un usuario administrador vía API. Como consecuencia, tampoco es posible obtener legítimamente, vía login, un token con `rol: "SUPER_ADMIN"`, ya que ese valor nunca puede persistirse. Las rutas protegidas con `roleMiddleware("SUPER_ADMIN")` (`PUT /api/configuracion/`, todo `/api/usuarios-admin/` salvo GET, `GET /api/auditoria/`) son efectivamente inalcanzables en el estado actual de los datos/esquemas.
   - Los dos usuarios ya sembrados (`luis.aurea@proyecto.local`, `admin2@agrovivero.com`) tienen `rol: "ADMIN"` (no válido para el enum de Mongoose) y **no tienen `passwordHash`**, ver punto 3.

3. **El inicio de sesión falla con 500 en vez de 401 para usuarios sin `passwordHash`.**
   Los usuarios sembrados no tienen el campo `passwordHash`. `bcrypt.compare(password, undefined)` lanza `Error: data and hash arguments required`, que no tiene `.status`, así que el `errorHandler` responde `500` en vez de `401`.
   - Comprobado: `POST /api/auth/login` con `luis.aurea@proyecto.local` → `500 {"success":false,"message":"data and hash arguments required"}`.

4. **El inicio de sesión con contraseña incorrecta también responde 500, no 401.**
   `auth.service.js` lanza `new Error("Credenciales inválidas")` sin asignar `error.status`, así que cae en el 500 genérico del `errorHandler` en lugar de un `401`.
   - Comprobado con un usuario válido y contraseña incorrecta → `500 {"success":false,"message":"Credenciales inválidas"}`.

5. **`POST /api/plantas/` falla por defecto salvo que se envíe `etnobotanica` con al menos un campo.**
   El esquema Mongoose da `etnobotanica: { default: {} }`, pero como todos sus subcampos son opcionales y no quedan poblados, Mongoose (con `minimize` activado por defecto) **elimina** la clave `etnobotanica` vacía antes de guardar. El validador de esquema de MongoDB en `plantas` exige `etnobotanica` como propiedad requerida (no exige subcampos, solo que exista el objeto).
   - Comprobado: creando una planta solo con `slug`, `nombreComun`, `nombreCientifico`, `taxonomia` (los únicos campos que valida `planta.validation.js`) → `500 {"message":"Document failed validation"}` con `missingProperties: ["etnobotanica"]`.
   - Comprobado: agregando `etnobotanica: { usoTradicional: "..." }` (cualquier subcampo no vacío) → `201` correcto.
   - **Impacto:** cualquier integrador que siga solo la validación documentada (slug/nombreComun/nombreCientifico) recibirá 500 de forma consistente. Falta documentar u obligar `etnobotanica` con contenido, o quitar el default vacío y usar `minimize:false`.

6. **`POST /api/fuentes/` no puede tener éxito con los campos que exige su propia validación.**
   `fuente.validation.js` exige `titulo` y `url`. Pero el modelo no tiene ningún campo `url` (el real es `urlDocumento`, opcional) y sí exige `tipo` (enum, requerido) que la validación **no** comprueba.
   - Comprobado: `POST /api/fuentes/` con `{titulo, url}` → `500 {"message":"Fuente validation failed: tipo: Path \`tipo\` is required."}`.
   - Comprobado: con `{titulo, url, tipo}` → `201` (el campo `url` se ignora silenciosamente, no se persiste).

7. **`POST /api/auditoria/` no puede tener éxito con los campos que exige su propia validación.**
   `auditoria.validation.js` exige `accion` y `usuario`. El modelo exige `accion`, `coleccion` y `usuarioCorreo` (no exige `usuario`, que ni siquiera es un campo del esquema).
   - Comprobado: `{accion, usuario}` → `500 {"message":"...usuarioCorreo: Path \`usuarioCorreo\` is required., coleccion: Path \`coleccion\` is required."}`.
   - Comprobado: `{accion, coleccion, usuarioCorreo}` (sin `usuario`) → `400`, la validación exige `usuario` igualmente.
   - Comprobado: solo funciona enviando los 4 campos juntos (`accion`, `usuario` "de relleno", `coleccion`, `usuarioCorreo`) → `201`.

### 🟡 Observaciones menores

- `GET /api/usuarios-admin/` y `GET /api/usuarios-admin/:id` devuelven el documento completo, **incluyendo `passwordHash`** (el hash de bcrypt). Cualquier usuario autenticado, no solo `SUPER_ADMIN`, puede leer esta lista. Conviene proyectar los campos sensibles fuera de la respuesta.
- El cuerpo de creación de usuario usa el nombre `passwordHash` para la contraseña **en texto plano** que envía el cliente (`usuario-admin.service.js` la hashea después); el nombre del campo es confuso y fácil de interpretar como “ya viene hasheada”.

### ✅ Verificado sin problemas

- `GET /api/health`, `GET /api/configuracion/`, `PUT /api/configuracion/` (rol `SUPER_ADMIN`, cuerpo `{proyecto, baseDatos}`).
- `GET /api/plantas/`, `GET /api/plantas/:slug` (404 correcto si no existe), `PUT /api/plantas/:id`, `DELETE /api/plantas/:id` (soft-delete a `estado: INACTIVO`).
- `GET/POST/PUT/DELETE /api/noticias/*` — el modelo, la validación y la colección real coinciden completamente.
- `GET /api/comtrade/catalogo`, `GET /api/comtrade/:plantaSlug`, `GET /api/comtrade/consulta/:plantaSlug` — probado de extremo a extremo contra la API real de UN Comtrade (`mejorana`), responde en ~14 s (por los `esperar(1200ms)` entre llamadas) con datos de importación/exportación correctos.
- Autenticación y autorización: `401` sin token, `401` con token inválido, `403` con rol insuficiente, `200`/`201` con rol correcto. El middleware de roles funciona como se espera cuando el JWT ya tiene un `rol` válido.
- La limitación de tasa (`apiLimiter`) está montada globalmente bajo `/api` (`100 req / 15 min`); no se llegó a agotar el límite durante las pruebas.
