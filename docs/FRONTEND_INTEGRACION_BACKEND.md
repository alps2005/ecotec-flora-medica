# Documentación Técnica Frontend — Guía de Integración con Backend

> **Audiencia:** Desarrolladores Backend que necesitan conectar la API existente con este Frontend.
> **Fecha de generación:** Julio 2026
> **Estado:** Frontend funcional con datos locales (Content Collections). Listo para migración a API.

---

## Tabla de contenidos

1. [Información general](#1-información-general)
2. [Estructura de carpetas](#2-estructura-de-carpetas)
3. [Flujo de navegación](#3-flujo-de-navegación)
4. [Gestión del estado](#4-gestión-del-estado)
5. [Consumo de API](#5-consumo-de-api)
6. [Variables de entorno](#6-variables-de-entorno)
7. [Formularios](#7-formularios)
8. [Integración necesaria con Backend](#8-integración-necesaria-con-backend)
9. [Componentes relacionados con Suscriptores](#9-componentes-relacionados-con-suscriptores)
10. [Servicios HTTP](#10-servicios-http)
11. [Flujo completo de Suscripción](#11-flujo-completo-de-suscripción)
12. [Recomendaciones para integrar el Backend](#12-recomendaciones-para-integrar-el-backend)

---

## 1. Información general

### Framework

**Astro 7** — framework web orientado a contenido, con renderizado estático por defecto (SSG). No es React ni Vue. Los componentes son archivos `.astro` que combinan frontmatter TypeScript y plantilla HTML. La interactividad del cliente se implementa con `<script>` vanilla dentro de los propios archivos `.astro`.

### Lenguaje

**TypeScript** (tipado estricto en frontmatter de componentes y pages) + HTML + CSS.

### Gestor de paquetes

**pnpm** (requiere Node.js ≥ 22.12.0).

```bash
pnpm install       # instalar dependencias
pnpm dev           # servidor de desarrollo en localhost:4321
pnpm build         # build de producción en ./dist/
pnpm preview       # previsualizar el build
```

### Librerías principales

| Paquete | Versión | Función |
|---|---|---|
| `astro` | ^7.0.0 | Framework principal, Content Collections, SSG/SSR |
| `tailwindcss` | ^4.3.1 | Sistema de estilos utilitarios |
| `@tailwindcss/vite` | ^4.3.1 | Plugin de integración Tailwind con Vite |
| `@astrojs/sitemap` | ^3.7.3 | Generación automática de sitemap XML |
| `gsap` | ^3.15.0 | Animaciones (underlines del header) |

**No hay:** React, Vue, Redux, Zustand, axios, fetch wrappers, React Query ni ninguna librería de gestión de estado o HTTP. Todo es Astro nativo + JS vanilla.

### Estructura general del proyecto

```
ecotec-flora-medica/
├── public/                   # Archivos estáticos servidos tal cual (favicons)
├── src/
│   ├── assets/               # Imágenes procesadas por Astro (optimización)
│   ├── components/           # Componentes reutilizables .astro
│   ├── content/              # Archivos de contenido Markdown (datos locales)
│   ├── layouts/              # Shell HTML base de todas las páginas
│   ├── pages/                # Rutas del sitio (cada archivo = una URL)
│   ├── styles/               # CSS global con tokens de diseño
│   ├── consts.ts             # Constantes globales (SITE_TITLE, SITE_DESCRIPTION)
│   └── content.config.ts     # Schemas de las colecciones de contenido
├── docs/                     # Documentación técnica del proyecto
├── astro.config.mjs          # Configuración de Astro (integraciones, site URL)
├── package.json
└── pnpm-lock.yaml
```

---

## 2. Estructura de carpetas

Solo se documentan las carpetas relevantes para la integración.

### `src/pages/`

El enrutador de Astro. Cada archivo es una ruta pública del sitio. Los archivos con `[slug]` generan rutas dinámicas mediante `getStaticPaths()`.

```
src/pages/
├── index.astro              → /
├── especies.astro           → /especies
├── etnobotanica.astro       → /etnobotanica
├── robots.txt.ts            → /robots.txt
├── especies/
│   └── [slug].astro         → /especies/:slug
└── blog/
    ├── index.astro          → /blog
    └── [slug].astro         → /blog/:slug
```

### `src/components/`

Componentes `.astro` sin estado propio (no tienen hooks, no manejan sesión). Reciben datos como props en tiempo de build.

```
src/components/
├── BaseHead.astro            # Meta tags SEO, OG, Twitter, canonical, RSS
├── Header.astro              # Navegación principal fija (glassmorphism + GSAP)
├── HeaderLink.astro          # Link de nav con underline animado y detección de ruta activa
├── Footer.astro              # Pie de página con copyright dinámico
├── species/                  # Componentes del catálogo de especies
│   ├── SpeciesCard.astro     # Tarjeta de especie individual
│   ├── SpeciesGrid.astro     # Contenedor del listado
│   ├── SearchBar.astro       # Input de búsqueda libre
│   ├── TaxonomyFilter.astro  # Select de filtro por familia taxonómica
│   └── Pagination.astro      # Paginación del catálogo
└── etnobotanicacomp/         # Componentes del atlas etnobotánico
    ├── Etnobotanicahero.astro     # Encabezado de sección
    ├── Etnobotanicafilters.astro  # Botones de filtro por categoría
    ├── Etnobotanicagrid.astro     # Grid de tarjetas etnobotánicas
    ├── Etnobotanicacard.astro     # Tarjeta etnobotánica individual
    └── Etnobotanicapagination.astro # Paginación del atlas
```

### `src/content/`

Datos locales en formato Markdown con frontmatter YAML. Actúan como sustituto temporal de la API. Esta es la carpeta que se eliminará/reemplazará al integrar el Backend real.

```
src/content/
├── blog/                    # 1 post de ejemplo
├── species/                 # 39 fichas completas de especies (schema complejo)
└── etnobotanicacont/        # 43 entradas simplificadas del atlas etnobotánico
```

### `src/layouts/`

Contiene únicamente `Layout.astro`, el shell HTML base que todas las páginas utilizan. Incluye `<BaseHead>`, `<Header>`, `<main><slot/></main>` y `<Footer>`.

### `src/styles/`

Contiene `global.css` con la configuración de Tailwind v4 (`@import "tailwindcss"`), las fuentes de Google Fonts (EB Garamond + Hanken Grotesk) y los tokens de diseño personalizados (`--color-primary`, `--color-secondary`, etc.).

### `src/assets/`

Imágenes locales procesadas por el pipeline de Astro (optimización automática). Actualmente solo contiene una imagen de placeholder para la home. Las imágenes de las especies son URLs externas de Cloudinary.

> **Nota:** No existen carpetas `services/`, `hooks/`, `contexts/`, `utils/` ni `stores/`. Toda la lógica de interacción está embebida como scripts `<script>` vanilla dentro de los archivos `.astro` de cada página.

---

## 3. Flujo de navegación

El sitio tiene 6 pantallas públicas. La navegación principal está en el `Header` con tres enlaces fijos.

```
Inicio (/)
├── [botón "Explorar especies"] → /especies
├── [botón "Leer blogs"]        → /blog
└── [botón "SUSCRIBIRME"]       → (sin ruta, solo abre modal/pendiente)

Catálogo de Especies (/especies)
└── [clic en tarjeta] → /especies/:slug

Detalle de Especie (/especies/:slug)
└── [← Volver al catálogo] → /especies

Atlas Etnobotánico (/etnobotanica)
└── [clic en tarjeta] → /especies/:slug
    (las tarjetas etnobotánicas enlazan al detalle de la colección species)

Blog (/blog)
└── [clic en artículo] → /blog/:slug

Detalle de Blog (/blog/:slug)
└── [navegación del header] → cualquier sección
```

### Detalle por pantalla

| Pantalla | Ruta | Archivo | Descripción |
|---|---|---|---|
| Inicio | `/` | `src/pages/index.astro` | Hero, estadísticas, formulario suscripción, marco de estudio |
| Catálogo | `/especies` | `src/pages/especies.astro` | Listado con búsqueda, filtro taxonómico y paginación |
| Detalle especie | `/especies/:slug` | `src/pages/especies/[slug].astro` | Ficha académica completa de una especie |
| Etnobotánica | `/etnobotanica` | `src/pages/etnobotanica.astro` | Atlas con filtros por categoría y paginación |
| Blog | `/blog` | `src/pages/blog/index.astro` | Listado de artículos del blog |
| Detalle blog | `/blog/:slug` | `src/pages/blog/[slug].astro` | Artículo individual renderizado desde Markdown |

---

## 4. Gestión del estado

### Tipo de gestión

Este Frontend **no utiliza ningún sistema de gestión de estado** (no hay Redux, Zustand, Context API, Recoil, ni React Query). Es un sitio Astro estático (SSG) con interactividad mínima implementada mediante JavaScript vanilla en bloques `<script>` dentro de los archivos `.astro`.

### Estado del catálogo de Especies (`/especies`)

El estado interactivo del catálogo (filtro activo, búsqueda, página actual) vive exclusivamente en variables locales de JavaScript dentro del `<script>` de `src/pages/especies.astro`. No se persiste en localStorage ni en ningún store global.

```javascript
// Variables locales de estado (en especies.astro)
let currentPage = 1;
// El query y la familia se leen directamente de los inputs del DOM
```

### Estado del Atlas Etnobotánico (`/etnobotanica`)

Idéntico al anterior. Variables locales en el `<script>` de `src/pages/etnobotanica.astro`:

```javascript
let currentPage = 1;
let currentCategory = 'GENERAL';
```

### Sesión de usuario

**No existe gestión de sesión.** El sitio no tiene login, autenticación ni área de usuario. El botón "SUSCRIBIRME" del header actualmente no tiene ruta asignada — es el único punto donde se necesitará integrar una sesión/estado de usuario en el futuro.

### Comunicación entre componentes

Astro no tiene un sistema de comunicación entre componentes en tiempo de ejecución. La comunicación es unidireccional: la página pasa props a los componentes en tiempo de build. La interactividad usa atributos `data-*` en el DOM como puente entre el HTML generado y el script vanilla.

Ejemplos de atributos `data-*` usados:

```
data-species-root         → wrapper del grid de especies
data-species-card         → cada tarjeta de especie
data-species-search       → input de búsqueda
data-species-family       → select de familia
data-species-pagination   → contenedor de paginación
data-species-page-status  → span con "Página X de Y"
data-species-page-prev    → botón anterior
data-species-page-next    → botón siguiente
data-etnobotanica-filter  → botones de categoría
data-etnobotanica-pagination → paginación del atlas
```

---

## 5. Consumo de API

### Estado actual

**El Frontend NO consume ninguna API externa en este momento.** Todos los datos provienen de archivos Markdown locales procesados por Astro Content Collections en tiempo de build. No hay `fetch`, `axios`, interceptores ni cliente HTTP configurado.

### Cómo se leen los datos actualmente

Los datos se consumen mediante las APIs de Astro Content Collections:

```typescript
// Obtener todas las especies (build time)
import { getCollection } from 'astro:content';
const species = await getCollection('species');

// Obtener una especie por slug (build time)
import { getCollection, render } from 'astro:content';
export async function getStaticPaths() {
  const species = await getCollection('species');
  return species.map((entry) => ({
    params: { slug: entry.data.slug ?? entry.id },
    props: { entry },
  }));
}
```

Esta lógica de `getCollection()` es la que debe reemplazarse por llamadas a la API real.

### Base URL

La única variable de entorno configurada actualmente es:

```
SITE_URL=http://localhost:4321  (valor por defecto en astro.config.mjs)
```

No existe una variable `API_URL` ni ninguna base URL para servicios externos. Debe crearse al integrar el Backend.

### Dónde se configurará

La Base URL de la API deberá configurarse en:
1. Una nueva variable de entorno `PUBLIC_API_URL` (prefijo `PUBLIC_` para que sea accesible en el cliente con Astro)
2. Un nuevo archivo de servicio HTTP, por ejemplo `src/lib/api.ts`

---

## 6. Variables de entorno

### Variables actualmente existentes

| Variable | Dónde se usa | Valor por defecto | Función |
|---|---|---|---|
| `SITE_URL` | `astro.config.mjs` | `http://localhost:4321` | URL base del sitio para sitemap, canonical URLs y metadatos Open Graph. Solo se usa en build time. |

> **Nota:** No existe archivo `.env` ni `.env.example` en el proyecto. `SITE_URL` se lee directamente en `astro.config.mjs` con `process.env.SITE_URL`.

### Variables que deben crearse para la integración

| Variable | Tipo | Ejemplo | Función |
|---|---|---|---|
| `PUBLIC_API_URL` | Pública (cliente + servidor) | `https://api.ecotec-flora.com` | Base URL de la API del Backend. El prefijo `PUBLIC_` es obligatorio en Astro para acceder a la variable desde scripts del cliente. |
| `API_SECRET_KEY` | Privada (solo servidor) | `Bearer eyJ...` | Token de autenticación para las peticiones server-side (si aplica). Sin prefijo `PUBLIC_`. |
| `PUBLIC_CLOUDINARY_BASE_URL` | Pública | `https://res.cloudinary.com/mi-cuenta` | Base URL de Cloudinary, si las URLs de imágenes pasan a construirse en el Frontend en lugar de venir completas del Backend. |

### Cómo declarar variables en Astro

```bash
# .env (no commitear)
PUBLIC_API_URL=https://api.ecotec-flora.com
API_SECRET_KEY=mi-token-secreto
SITE_URL=https://ecotec-flora.com
```

```typescript
// Acceso en archivos .astro o .ts
const apiUrl = import.meta.env.PUBLIC_API_URL;       // cliente y servidor
const secretKey = import.meta.env.API_SECRET_KEY;    // solo servidor (build time / SSR)
```

---

## 7. Formularios

### Inventario de formularios

El proyecto tiene **un único formulario** activo, ubicado en la página de inicio.

---

### Formulario 1 — Suscripción al boletín

| Atributo | Valor |
|---|---|
| **Componente / archivo** | `src/pages/index.astro` (inline, sin componente propio) |
| **Ruta donde aparece** | `/` (página de inicio, sección "Únete a nuestro blog") |
| **Método del `<form>`** | `method="post"` |
| **Action del `<form>`** | `action="#"` (apunta a `#`, no tiene endpoint real aún) |

#### Campos

| Campo | `id` | `name` | `type` | `placeholder` | Requerido |
|---|---|---|---|---|---|
| Nombre completo | `full-name` | `fullName` | `text` | `Dr. Elena Garro` | No (no tiene `required` en el HTML) |
| Correo institucional | `email` | `email` | `email` | `investigacion@flormedica.org` | No (no tiene `required` en el HTML) |

#### Botón de envío

```html
<button type="submit">Suscribirme al boletín</button>
```

#### Validaciones actuales

**No hay validaciones** implementadas. El formulario no tiene:
- Atributos HTML `required`
- Validación de formato de email (más allá del type="email" nativo del navegador)
- Validación JavaScript personalizada
- Mensajes de error
- Feedback de éxito o error

#### Función de submit

**No existe.** El formulario tiene `action="#"` y recarga la página al enviarse. No hay un `addEventListener('submit', ...)` ni ningún `fetch`/`axios` que intercepte el envío.

#### Código actual del formulario

```html
<form class="w-full space-y-8" action="#" method="post">
  <div class="w-full space-y-3">
    <label for="full-name">Nombre completo</label>
    <input id="full-name" type="text" name="fullName" placeholder="Dr. Elena Garro" />
  </div>
  <div class="w-full space-y-3">
    <label for="email">Correo institucional</label>
    <input id="email" type="email" name="email" placeholder="investigacion@flormedica.org" />
  </div>
  <button type="submit">Suscribirme al boletín</button>
</form>
```

---

### Formularios de login y registro

**No existen.** El sitio no tiene sistema de autenticación de usuarios. No hay páginas de `/login`, `/register` ni `/perfil`.

---

### Botón "SUSCRIBIRME" del Header

Adicional al formulario anterior, el `Header.astro` contiene un botón con la etiqueta "SUSCRIBIRME":

```html
<!-- src/components/Header.astro -->
<button class="border border-[#1B6D24] ...">
  SUSCRIBIRME
</button>
```

Este botón **no tiene evento asociado** actualmente. No tiene `onclick`, no abre un modal, no navega a ninguna ruta. Es un elemento visual pendiente de implementación.

---

## 8. Integración necesaria con Backend

La tabla siguiente cubre todas las pantallas del sitio y los endpoints que cada una necesita consumir del Backend.

> El contrato de API documentado por el equipo Backend se encuentra en `docs/REFERENCIA_SCHEMA_BACKEND.md`.

| Pantalla | Componente / Archivo | Endpoint necesario | Método HTTP | Estado actual | Prioridad |
|---|---|---|---|---|---|
| Catálogo de Especies | `src/pages/especies.astro` + `SpeciesGrid.astro` | `GET /api/plantas` | GET | Usa `getCollection('species')` local | Alta |
| Catálogo de Especies (filtro por familia) | `src/pages/especies.astro` | `GET /api/plantas?familia=Asteraceae` | GET | Filtrado client-side sobre datos locales | Alta |
| Detalle de Especie | `src/pages/especies/[slug].astro` | `GET /api/plantas/:slug` | GET | Usa `getCollection('species')` + `render()` local | Alta |
| Atlas Etnobotánico | `src/pages/etnobotanica.astro` + `Etnobotanicagrid.astro` | `GET /api/plantas` o endpoint dedicado `/api/etnobotanica` | GET | Usa `getCollection('etnobotanica')` local | Alta |
| Inicio — Estadísticas | `src/pages/index.astro` | `GET /api/stats` (ej: total especies, familias) | GET | Valores hardcodeados (27, 10, 4, 3) | Media |
| Inicio — Suscripción | `src/pages/index.astro` (formulario inline) | `POST /api/suscriptores` | POST | Sin endpoint, `action="#"` | Alta |
| Blog — Listado | `src/pages/blog/index.astro` | `GET /api/blog` o Content Collections local | GET | Usa `getCollection('blog')` local | Baja |
| Blog — Detalle | `src/pages/blog/[slug].astro` | `GET /api/blog/:slug` | GET | Usa Content Collections local | Baja |
| Cancelar suscripción | No existe aún | `PATCH /api/suscriptores/cancelar` o `DELETE /api/suscriptores/:id` | PATCH / DELETE | Sin implementar | Media |
| Confirmar suscripción (email) | No existe aún | `POST /api/suscriptores/confirmar` | POST | Sin implementar | Media |

### Notas sobre el contrato de API de Plantas

Según `docs/REFERENCIA_SCHEMA_BACKEND.md`, los endpoints documentados son:

```
GET /api/plantas
  Query param opcional: ?familia=Asteraceae
  Campos devueltos: slug, nombreComun, nombreCientifico,
                    taxonomia.familia, multimediaPrincipal.imagenUrl

GET /api/plantas/:slug
  Respuesta: objeto completo (ver sección 9 de este documento)
```

El schema del Frontend (`src/content.config.ts`) ya está alineado con este contrato. El campo `analisisAcademico` del Backend equivale al cuerpo Markdown de los archivos `.md` actuales (renderizado con `render(entry)`).

---

## 9. Componentes relacionados con Suscriptores

### 9.1 Formulario de suscripción (inline en index.astro)

**Archivo:** `src/pages/index.astro`

El formulario no es un componente independiente. Está escrito directamente en la sección de la página de inicio, dentro de un `<div>` con fondo oscuro.

#### Props

No aplica — el formulario es inline, no tiene props porque no es un componente `.astro` separado.

#### Campos del formulario

```typescript
// Campos que el Frontend enviaría al Backend
{
  fullName: string,   // input#full-name, name="fullName"
  email: string       // input#email, name="email"
}
```

#### Eventos

No hay eventos configurados actualmente. El `<form>` tiene `method="post"` y `action="#"`, lo que provoca un reload de página sin enviar datos a ningún endpoint.

#### Función de submit

**No existe.** Para implementar la integración se necesita:

```javascript
// Lo que debe implementarse en src/pages/index.astro
document.querySelector('form').addEventListener('submit', async (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);
  const response = await fetch(`${import.meta.env.PUBLIC_API_URL}/api/suscriptores`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      fullName: formData.get('fullName'),
      email: formData.get('email'),
    }),
  });
  // Manejar respuesta y mostrar feedback al usuario
});
```

---

### 9.2 Botón "SUSCRIBIRME" del Header

**Archivo:** `src/components/Header.astro`

```html
<button
  class="border border-[#1B6D24] text-[12px] ... rounded-4xl px-6 py-2.5 ..."
>
  SUSCRIBIRME
</button>
```

| Atributo | Valor actual |
|---|---|
| `type` | No definido (por defecto `submit`, debería ser `button`) |
| `onclick` | No existe |
| Props | Ninguna |
| Evento | Ninguno |
| Función | Ninguna |

Este botón debería abrir un modal con el formulario de suscripción o navegar a una sección de la home. Actualmente no hace nada.

---

### 9.3 Mensajes de feedback

**No existen.** No hay elementos en el DOM para mostrar mensajes de éxito, error, carga ni confirmación tras el envío del formulario. Deben implementarse junto con la lógica del submit.

---

## 10. Servicios HTTP

### Estado actual

**No existen servicios HTTP.** No hay carpeta `src/services/`, `src/lib/`, `src/api/` ni ningún archivo con funciones de fetch. No se usa `axios` ni ninguna librería HTTP.

Todo el consumo de datos ocurre en build time a través de `getCollection()` de Astro Content Collections, no mediante peticiones HTTP en runtime.

### Servicios que deben crearse para la integración

Se recomienda crear los siguientes archivos:

---

#### `src/lib/api.ts` — Cliente HTTP base

```typescript
// Archivo a crear: src/lib/api.ts
const BASE_URL = import.meta.env.PUBLIC_API_URL;

export async function apiFetch<T>(path: string, options?: RequestInit): Promise<T> {
  const res = await fetch(`${BASE_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options?.headers,
    },
    ...options,
  });

  if (!res.ok) {
    throw new Error(`API error ${res.status}: ${path}`);
  }

  return res.json() as Promise<T>;
}
```

---

#### `src/lib/species.ts` — Servicio de especies

```typescript
// Archivo a crear: src/lib/species.ts
import { apiFetch } from './api';

export interface PlantaResumen {
  slug: string;
  nombreComun: string;
  nombreCientifico: string;
  taxonomia: { familia: string };
  multimediaPrincipal: { imagenUrl: string };
}

export interface PlantaDetalle extends PlantaResumen {
  nombresAlternativos: string[];
  taxonomia: {
    reino: string; division: string; clase: string;
    familia: string; genero: string;
  };
  etnobotanica: {
    clasificacion: string; parteUtilizada: string;
    usoTradicional: string; compuestosQuimicos: string[];
  };
  analisisAcademico: {
    taxonomia: string; etnobotanica: string;
    fitoquimica: string; sostenibilidad: string;
  };
  estado: string;
}

export const getAllPlantas = (familia?: string) => {
  const query = familia ? `?familia=${encodeURIComponent(familia)}` : '';
  return apiFetch<PlantaResumen[]>(`/api/plantas${query}`);
};

export const getPlantaBySlug = (slug: string) =>
  apiFetch<PlantaDetalle>(`/api/plantas/${slug}`);
```

---

#### `src/lib/suscriptores.ts` — Servicio de suscripción

```typescript
// Archivo a crear: src/lib/suscriptores.ts
import { apiFetch } from './api';

export interface SuscriptorPayload {
  fullName: string;
  email: string;
}

export interface SuscriptorResponse {
  id: string;
  email: string;
  estado: 'pendiente' | 'activo';
}

export const suscribirse = (data: SuscriptorPayload) =>
  apiFetch<SuscriptorResponse>('/api/suscriptores', {
    method: 'POST',
    body: JSON.stringify(data),
  });

export const cancelarSuscripcion = (id: string) =>
  apiFetch<void>(`/api/suscriptores/${id}/cancelar`, { method: 'PATCH' });
```

---

## 11. Flujo completo de Suscripción

### Flujo actual (sin integración)

```
Usuario ve el formulario en la Home (/)
        ↓
Escribe nombre y email
        ↓
Hace clic en "Suscribirme al boletín"
        ↓
El <form action="#"> recarga la página
        ↓
Los datos se pierden. No hay envío, no hay feedback.
```

### Flujo objetivo (con integración completa)

```
Usuario ve el formulario en la Home (/)
        ↓
Escribe nombre y email
        ↓
Hace clic en "Suscribirme al boletín"
        ↓
[JavaScript intercepta el submit con preventDefault()]
        ↓
Validar que nombre y email no estén vacíos
Validar formato de email
        ↓
[Si validación falla]
  → Mostrar mensaje de error inline bajo el campo correspondiente
        ↓
[Si validación pasa]
  → Mostrar estado de carga en el botón ("Enviando...")
        ↓
POST /api/suscriptores
  Body: { fullName: "...", email: "..." }
        ↓
[Si respuesta 2xx]
  → Ocultar formulario
  → Mostrar mensaje de éxito: "¡Suscripción exitosa! Revisa tu correo para confirmar."
        ↓
[Si respuesta 4xx / 5xx]
  → Mostrar mensaje de error: "Ocurrió un error. Por favor intenta de nuevo."
  → Rehabilitar el botón
```

### Partes que faltan implementar

1. **Interceptor del submit** — `addEventListener('submit', ...)` en el `<form>` de `src/pages/index.astro`
2. **Validación de campos** — `required` en los inputs + validación JS para formato de email
3. **Llamada HTTP** — `fetch` o servicio `suscriptores.ts` al endpoint `POST /api/suscriptores`
4. **Estados de carga** — Deshabilitar botón y cambiar texto durante el request
5. **Feedback de éxito** — Elemento HTML para confirmar la suscripción
6. **Feedback de error** — Elemento HTML para mostrar errores de API
7. **Botón "SUSCRIBIRME" del Header** — Asignar acción (abrir modal o scroll a la sección del formulario)
8. **Confirmación por email** — El Backend envía email; el Frontend podría necesitar una página `/suscripcion/confirmar?token=...` que llame a `POST /api/suscriptores/confirmar`
9. **Cancelación** — Página o flujo para `PATCH /api/suscriptores/:id/cancelar`

---

## 12. Recomendaciones para integrar el Backend

La integración requiere cambios en el modo de renderizado (de SSG puro a SSR o fetch en cliente), además de nuevos archivos de servicio. A continuación se lista todo lo necesario ordenado por módulo.

### 12.1 Configuración general

| Archivo a modificar | Cambio necesario |
|---|---|
| `astro.config.mjs` | Agregar `output: 'hybrid'` o `output: 'server'` si se necesitan rutas SSR. Para fetch en cliente con SSG puro no es necesario. |
| `.env` (crear) | Agregar `PUBLIC_API_URL`, `SITE_URL` de producción y opcionalmente `API_SECRET_KEY` |
| `package.json` | Opcional: agregar adaptador SSR (`@astrojs/node`) si se necesita renderizado server-side dinámico |

### 12.2 Nuevos archivos a crear

| Archivo | Función |
|---|---|
| `src/lib/api.ts` | Cliente HTTP base (ver sección 10) |
| `src/lib/species.ts` | Servicio para `GET /api/plantas` y `GET /api/plantas/:slug` |
| `src/lib/suscriptores.ts` | Servicio para `POST /api/suscriptores` y `PATCH /api/suscriptores/:id/cancelar` |
| `src/lib/types.ts` | Interfaces TypeScript compartidas (`PlantaResumen`, `PlantaDetalle`, `SuscriptorPayload`) |
| `.env` | Variables de entorno (no commitear) |
| `.env.example` | Plantilla de variables de entorno (sí commitear) |

### 12.3 Páginas a modificar

| Archivo | Cambio necesario |
|---|---|
| `src/pages/index.astro` | Reemplazar `action="#"` del formulario por lógica JS que llame a `POST /api/suscriptores`. Agregar validaciones y mensajes de feedback. Añadir campos `required`. Opcionalmente: reemplazar estadísticas hardcodeadas (27 especies, 10 familias) con datos de `GET /api/stats`. |
| `src/pages/especies.astro` | Reemplazar `getCollection('species')` por `fetch(PUBLIC_API_URL + '/api/plantas')`. En SSG: fetch en build time. En SSR: fetch en cada request. |
| `src/pages/especies/[slug].astro` | Reemplazar `getCollection('species')` y `render()` por `fetch(PUBLIC_API_URL + '/api/plantas/' + slug)`. El análisis académico vendrá como string HTML o Markdown desde el Backend; adaptar el renderizado. |
| `src/pages/etnobotanica.astro` | Reemplazar `getCollection('etnobotanica')` por el endpoint correspondiente del Backend. |

### 12.4 Componentes a modificar

| Archivo | Cambio necesario |
|---|---|
| `src/components/Header.astro` | Asignar funcionalidad al botón "SUSCRIBIRME": scroll a la sección del formulario (`#suscripcion`) o abrir un modal. Añadir `type="button"` para evitar submit accidental. |
| `src/components/species/SpeciesGrid.astro` | Actualizar la interface `Props` para que acepte el tipo `PlantaResumen[]` del Backend. El campo `analisisAcademico` (texto largo) ya no vendrá en el listado. |
| `src/components/species/SpeciesCard.astro` | Sin cambios en la interfaz si el Backend sigue devolviendo `slug`, `nombreComun`, `nombreCientifico`, `taxonomia.familia`, `multimediaPrincipal.imagenUrl`. |
| `src/components/etnobotanicacomp/Etnobotanicagrid.astro` | Reemplazar las dos llamadas a `getCollection()` por datos del Backend. |

### 12.5 Consideraciones importantes

**Renderizado estático vs dinámico:**
- Actualmente todo el sitio es SSG (generado en build). Funciona mientras las especies no cambien frecuentemente.
- Si el catálogo se actualiza con frecuencia, considerar cambiar las páginas de especies a SSR (`export const prerender = false`) o agregar ISR (Incremental Static Regeneration con un adaptador de Astro).

**Análisis académico (cuerpo Markdown):**
- El Backend devuelve `analisisAcademico` como strings de texto plano o HTML (ver `REFERENCIA_SCHEMA_BACKEND.md`), no como Markdown renderizable con `render()`.
- En la página de detalle (`[slug].astro`) se usa `<Content />` que renderiza el cuerpo del archivo `.md`. Al migrar a API, este bloque deberá reemplazarse por `<Fragment set:html={data.analisisAcademico.taxonomia} />` u otro método de renderizado según el formato que devuelva el Backend.

**CORS:**
- El Backend debe permitir requests desde el dominio del Frontend. Si se usa fetch desde el cliente (browser), el Backend necesita los headers CORS correctos.

**Paginación:**
- La paginación actual es 100% client-side sobre todos los datos cargados de golpe. Con la API, se recomienda implementar paginación server-side: `GET /api/plantas?page=1&limit=9`.

**Imágenes:**
- Las URLs de Cloudinary actualmente vienen completas en los datos locales (`https://res.cloudinary.com/...`). El Backend debe seguir devolviendo URLs absolutas en `multimediaPrincipal.imagenUrl` para que los componentes funcionen sin cambios adicionales.

**Formulario de suscripción — campo `id` del formulario:**
- Se recomienda agregar `id="suscripcion"` al `<form>` o a su sección contenedora para permitir scroll directo desde el botón del Header: `<a href="/#suscripcion">`.
