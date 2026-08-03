# Documentación Técnica Frontend — Guía de Integración con Backend

> **Audiencia:** Desarrolladores Backend que necesitan conectar la API existente con este Frontend.
> **Fecha de generación:** Julio 2026 · **Última actualización:** 2026-08-02
> **Estado:** La integración avanzó desde la versión original de este documento (julio 2026). Ya **están conectados** a la API real: el formulario de suscripción (`POST /api/suscriptores`, desde el 2026-07-11) y las especies (`GET /api/plantas`, `GET /api/plantas/:slug`, desde el 2026-08-01, con *fallback* automático a Markdown si el backend no responde — ver [§5](#5-consumo-de-api)). **Siguen pendientes de integración directa:** el atlas de etnobotánica (deriva de especies, no llama a un endpoint propio) y las estadísticas del home (siguen hardcodeadas). El resto de este documento se actualizó para reflejar ese estado; las secciones 8/10/12 originales describían trabajo "a implementar" que en gran parte **ya se implementó** — se marcan explícitamente dónde.

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
11. [Flujo de Suscripción (implementado)](#11-flujo-de-suscripción-implementado)
12. [Cómo se resolvió la integración (y qué queda pendiente)](#12-cómo-se-resolvió-la-integración-y-qué-queda-pendiente)

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
| `astro` | ^7.1.3 | Framework principal, Content Collections, SSG |
| `@astrojs/react` | ^6.0.2 | Islas React (`client:load`), agregado 2026-07-31 |
| `react` / `react-dom` | ^19.2.8 | Usadas únicamente por la isla `TradeExplorer.tsx` del panel de comercio |
| `tailwindcss` | ^4.3.2 | Sistema de estilos utilitarios |
| `@tailwindcss/vite` | ^4.3.2 | Plugin de integración Tailwind con Vite |
| `shadcn` (CLI) + `class-variance-authority`, `clsx`, `tailwind-merge` | ^4.16.0 | Componentes React (`src/components/ui/*.tsx`) usados en el panel de comercio |
| `recharts` | 3.8.0 | Gráfico de barras de exportación/importación |
| `@astrojs/sitemap` | ^3.7.3 | Generación automática de sitemap XML |
| `gsap` | ^3.15.0 | Animaciones (underlines del header) |

**Actualizado (2026-07-31):** el proyecto **sí** tiene React desde `3129b4b`, pero acotado a una sola isla interactiva (`TradeExplorer.tsx`, con `client:load`). No hay Redux, Zustand, React Query ni `axios` — el cliente HTTP sigue siendo `fetch` nativo envuelto en `src/lib/api.ts`. El resto del sitio (catálogo, atlas, formularios) sigue siendo Astro + JS vanilla, sin cambios en ese patrón.

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
├── index.astro                    → /
├── especies.astro                 → /especies
├── etnobotanica.astro              → /etnobotanica
├── importacion-exportacion.astro   → /importacion-exportacion  (nueva, 2026-07-31)
├── sobre-nosotros.astro            → /sobre-nosotros            (nueva, 2026-08-02)
├── robots.txt.ts                  → /robots.txt
└── especies/
    └── [slug].astro               → /especies/:slug
```

> La sección de Blog (`blog/index.astro`, `blog/[slug].astro`, colección `blog`, `rss.xml.js`) fue **eliminada el 2026-08-02** y reemplazada por `sobre-nosotros.astro`.

### `src/components/`

Componentes `.astro` sin estado propio (no tienen hooks, no manejan sesión). Reciben datos como props en tiempo de build.

```
src/components/
├── BaseHead.astro            # Meta tags SEO, OG, Twitter, canonical
├── Header.astro              # Navegación principal fija (fondo blanco sólido + GSAP)
├── HeaderLink.astro          # Link de nav con underline animado y detección de ruta activa
├── Footer.astro              # Pie de página con copyright dinámico
├── ui/                       # Componentes shadcn/ui (React) — nuevo, 2026-07-31
│   ├── table.tsx, badge.tsx, select.tsx, card.tsx, tabs.tsx, button.tsx, chart.tsx
├── importexport/             # Componentes del panel de comercio — nuevo, 2026-07-31
│   ├── TradeKpiCards.astro
│   ├── TradeTable.astro          # Con paginación de cliente (agregada 2026-08-01)
│   └── TradeExplorer.tsx         # Isla React (client:load)
├── species/                  # Componentes del catálogo de especies
│   ├── SpeciesCard.astro     # Tarjeta de especie individual
│   ├── SpeciesGrid.astro     # Contenedor del listado
│   ├── SearchBar.astro       # Input de búsqueda libre
│   ├── TaxonomyFilter.astro  # Select de filtro por familia taxonómica
│   └── Pagination.astro      # Paginación del catálogo
└── etnobotanicacomp/         # Componentes del atlas etnobotánico
    ├── Etnobotanicahero.astro     # Encabezado de sección
    ├── Etnobotanicafilters.astro  # Botones de filtro por categoría
    ├── Etnobotanicagrid.astro     # Grid de tarjetas etnobotánicas (deriva de species-source)
    ├── Etnobotanicacard.astro     # Tarjeta etnobotánica individual
    ├── Etnobotanicamodal.astro    # Modal de detalle rápido — nuevo, 2026-07-31
    └── Etnobotanicapagination.astro # Paginación del atlas
```

### `src/content/`

Datos locales en formato Markdown con frontmatter YAML. **Ya no son la fuente principal de especies**: desde `59d646b` (2026-08-01) actúan como respaldo (`fallback`) de la API real, fusionados campo a campo por `src/lib/species-source.ts`.

```
src/content/
└── species/                 # 41 fichas completas de especies (schema complejo) — contenido de respaldo
```

> Las colecciones `blog` (1 post de ejemplo) y `etnobotanica` (43 entradas simplificadas) **ya no existen**: `etnobotanica` se eliminó el 2026-07-15 (el atlas deriva de `species`) y `blog` se eliminó el 2026-08-02 (reemplazado por `/sobre-nosotros`).

### `src/layouts/`

Contiene únicamente `Layout.astro`, el shell HTML base que todas las páginas utilizan. Incluye `<BaseHead>`, `<Header>`, `<main><slot/></main>` y `<Footer>`.

### `src/styles/`

Contiene `global.css` con Tailwind v4 (`@import "tailwindcss"`), el preset de shadcn/ui, Google Fonts (**Poppins**, única tipografía desde el 2026-08-01 — reemplazó a EB Garamond + Hanken Grotesk) y los tokens de diseño personalizados en escalas de 10 pasos (`--color-primary-500`, etc., con alias `--color-primary`, `--color-secondary`...). Ver `docs/DESIGN_SYSTEM.md` para el detalle completo.

### `src/assets/`

Imágenes locales procesadas por el pipeline de Astro (optimización automática): placeholder de la home, logo e isotipos institucionales del footer. Las imágenes de las especies son URLs externas (picsum.photos / Unsplash por ahora; el campo `imagenPublicId` sugiere una futura migración a Cloudinary).

### `src/lib/` y `src/data/`

> **Actualizado (2026-08-01/02):** a diferencia de lo que decía la versión original de esta sección, **sí existen** `src/lib/` (con `api.ts`, `species-source.ts`, `suscriptores.ts`, `trade-data.ts`, `utils.ts`) y `src/data/` (con `trade-data.json`, `hs-code-map.mjs`). No existen `hooks/`, `contexts/` ni `stores/` — la única gestión de estado en tiempo de ejecución sigue siendo local a cada `<script>`/isla React. Ver [§10](#10-servicios-http) para el detalle de cada servicio.

---

## 3. Flujo de navegación

El sitio tiene 6 pantallas públicas. La navegación principal está en el `Header` con cuatro enlaces fijos: Especies, Etnobotánica, Comercio y Sobre nosotros.

```
Inicio (/)
├── [botón "Explorar especies"] → /especies
└── [formulario de suscripción] → POST /api/suscriptores (integrado, ver §7)

Catálogo de Especies (/especies)
└── [clic en tarjeta] → /especies/:slug

Detalle de Especie (/especies/:slug)
└── [← Volver al catálogo] → /especies

Atlas Etnobotánico (/etnobotanica)
├── [clic en tarjeta] → abre Etnobotanicamodal.astro (detalle rápido in-page)
└── [enlace dentro del modal/tarjeta] → /especies/:slug

Importación y Exportación (/importacion-exportacion)
└── [selector de especie en TradeExplorer] → cambia el gráfico sin navegar (isla React)

Sobre Nosotros (/sobre-nosotros)
└── [CTAs de cierre] → /especies, /etnobotanica
```

> El botón "SUSCRIBIRME" del Header y la sección de Blog (`/blog`, `/blog/:slug`) mencionados en versiones previas de este documento **ya no existen**: el botón está comentado/oculto en el markup actual y el Blog se eliminó el 2026-08-02.

### Detalle por pantalla

| Pantalla | Ruta | Archivo | Descripción |
|---|---|---|---|
| Inicio | `/` | `src/pages/index.astro` | Hero, estadísticas, formulario de suscripción (funcional), marco de estudio |
| Catálogo | `/especies` | `src/pages/especies.astro` | Listado con búsqueda, filtro taxonómico y paginación; datos de `getSpeciesList()` |
| Detalle especie | `/especies/:slug` | `src/pages/especies/[slug].astro` | Ficha académica completa; datos de `getSpeciesDetail()` |
| Etnobotánica | `/etnobotanica` | `src/pages/etnobotanica.astro` | Atlas con filtros por categoría, paginación y modal de detalle |
| Comercio | `/importacion-exportacion` | `src/pages/importacion-exportacion.astro` | Panel de comercio internacional (UN Comtrade) con KPIs, explorador React y tabla paginada |
| Sobre nosotros | `/sobre-nosotros` | `src/pages/sobre-nosotros.astro` | Página institucional: misión, equipo, valores |

---

## 4. Gestión del estado

### Tipo de gestión

Este Frontend **no utiliza ningún sistema de gestión de estado global** (no hay Redux, Zustand, Context API, Recoil, ni React Query). Es mayormente un sitio Astro estático (SSG) con interactividad implementada mediante JavaScript vanilla en bloques `<script>` dentro de los archivos `.astro`. La única excepción es la isla React `TradeExplorer.tsx` (panel de comercio), que usa `React.useState`/`React.useMemo` locales al componente para la especie seleccionada — sin ningún store compartido con el resto de la página.

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

**No existe gestión de sesión.** El sitio no tiene login, autenticación ni área de usuario. El botón "SUSCRIBIRME" del header que aparecía en versiones previas de este documento está actualmente comentado/oculto en `Header.astro` (no se renderiza); el flujo de suscripción real vive en el formulario del home (ver §7).

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

### Estado actual (actualizado 2026-08-02)

**El Frontend sí consume la API real**, en dos flujos independientes:

1. **Especies** (`59d646b`, 2026-08-01) — en tiempo de **build**, `src/lib/species-source.ts` llama a `GET /api/plantas` y `GET /api/plantas/:slug`, y fusiona el resultado *campo a campo* con el contenido `.md` local. Si la API falla, no responde a tiempo (timeout de 5s) o devuelve algo inválido, el build cae automáticamente al `.md` sin romperse. Esto significa que **no hace falta que el backend esté siempre disponible** para que el sitio compile, pero cuando sí lo está, sus datos tienen prioridad campo por campo sobre el Markdown.
2. **Suscripción** (`ce002ca`, 2026-07-11; corregido en `76cb87d`, 2026-07-27) — en tiempo de **ejecución del navegador**, el formulario de la home llama a `POST /api/suscriptores` vía `suscriptoresService.registrar()`.

Lo que **sigue sin integrarse** directamente contra un endpoint propio:
- El atlas de etnobotánica no llama a la API por separado — deriva sus datos de `getSpeciesList()` (la misma llamada a `/api/plantas` que usa el catálogo).
- Las estadísticas del home (contador de especies/familias) siguen siendo valores calculados client-side sobre la lista ya cargada, no un endpoint `GET /api/stats` dedicado.
- El panel de comercio (`/importacion-exportacion`) no llama a un endpoint de comercio del backend documentado aquí — cruza especies con un dataset de UN Comtrade pre-procesado (`src/data/trade-data.json`). El backend sí expone endpoints `/api/comtrade/*` (ver `docs/api_usage.md`), pero el frontend actual no los consulta en runtime; el dataset se generó aparte.

### Cómo se leen los datos actualmente

```typescript
// src/lib/species-source.ts — listado (usado por especies.astro, index.astro, Etnobotanicagrid.astro)
export async function getSpeciesList(): Promise<{ rows: SpeciesEntry[]; source: 'api' | 'md' }> {
  const mdBySlug = await getActiveMdBySlug(); // getCollection('species', estado ACTIVO)
  let apiList = null;
  try {
    const data = await api.get('/api/plantas');
    if (Array.isArray(data)) apiList = data.filter((p) => !p.estado || p.estado === 'ACTIVO');
  } catch { apiList = null; } // fallback silencioso
  if (!apiList) return { rows: sortByNombre([...mdBySlug.values()].map(toRow)), source: 'md' };
  // slugs = unión API ∪ md; cada fila se fusiona campo a campo con mergeSpecies()
  // ...
}

// src/lib/species-source.ts — detalle (usado por especies/[slug].astro)
export async function getSpeciesDetail(slug: string, mdData: SpeciesData | null): Promise<SpeciesData> {
  try {
    const apiDetail = await api.get(`/api/plantas/${slug}`);
    if (apiDetail && typeof apiDetail === 'object') return mergeSpecies(apiDetail, mdData);
  } catch { /* fallthrough */ }
  return mdData ?? skeleton(null);
}
```

Esta capa **ya reemplaza** las llamadas directas a `getCollection('species')` mencionadas en versiones previas de este documento — sigue existiendo `getCollection()` únicamente *dentro* de `species-source.ts`, como fuente del `.md` de respaldo.

### Base URL

```
SITE_URL=http://localhost:4321        (astro.config.mjs, sitemap/canonical — sin cambios)
PUBLIC_API_URL=<url del backend>      (src/lib/api.ts — ya en uso, prefijo PUBLIC_ requerido por Astro)
```

`PUBLIC_API_URL` debe definirse en el entorno de build (`.env`, variables de CI, etc.) para que tanto la integración de especies (build time) como la de suscriptores (runtime del navegador) apunten al backend correcto. Si no está definida, `api.ts` usa `''` como base — las peticiones fallarán y todo caerá al fallback de Markdown (especies) o al mensaje de error del formulario (suscripción).

---

## 6. Variables de entorno

### Variables actualmente existentes

| Variable | Dónde se usa | Valor por defecto | Función |
|---|---|---|---|
| `SITE_URL` | `astro.config.mjs` | `http://localhost:4321` | URL base del sitio para sitemap, canonical URLs y metadatos Open Graph. Solo se usa en build time. |
| `PUBLIC_API_URL` | `src/lib/api.ts` (usado por `species-source.ts` en build time y por `suscriptores.ts` en runtime del navegador) | `''` (string vacío si no se define) | Base URL del backend. **Ya está en uso desde el 2026-07-11**, ampliada el 2026-08-01 para las especies. |

> **Nota:** sigue sin existir archivo `.env.example` en el repositorio (`.env` no se commitea, como es esperable). Ambas variables se leen con `import.meta.env.*` / `process.env.*` según el contexto (build vs. cliente).

### Variables que podrían agregarse a futuro

| Variable | Tipo | Ejemplo | Función |
|---|---|---|---|
| `API_SECRET_KEY` | Privada (solo servidor) | `Bearer eyJ...` | Token de autenticación para peticiones server-side, si el backend llega a requerir rutas de escritura desde el frontend (hoy no hace ninguna, solo lecturas `GET` y el `POST /api/suscriptores` público). |
| `PUBLIC_CLOUDINARY_BASE_URL` | Pública | `https://res.cloudinary.com/mi-cuenta` | Base URL de Cloudinary, si las URLs de imágenes pasan a construirse en el Frontend en lugar de venir completas del Backend (hoy `multimediaPrincipal.imagenUrl` siempre viene completa). |

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

> **Actualizado 2026-08-02:** este formulario **ya está integrado** con el backend desde `ce002ca` (2026-07-11), corregido en `76cb87d` (2026-07-27) para dejar de apuntar a una URL localhost fija. La descripción siguiente refleja el estado real, no una recomendación pendiente.

| Atributo | Valor |
|---|---|
| **Componente / archivo** | `src/pages/index.astro` (inline, sin componente propio) + handler en `src/scripts/suscripcion.js` |
| **Ruta donde aparece** | `/` (página de inicio, sección de suscripción) |
| **Método del `<form>`** | `method="post"` |
| **Action del `<form>`** | `action="#"` (irrelevante: el submit se intercepta con `preventDefault()` antes de que el navegador lo use) |
| **Atributo de enganche** | `data-suscripcion-form` — usado por `suscripcion.js` para encontrar el formulario, en vez de un selector genérico |

#### Campos

| Campo | `id` | `name` | `type` | Requerido |
|---|---|---|---|---|
| Nombre completo | `full-name` | `fullName` | `text` | No a nivel HTML; sí se valida en JS (ver abajo) |
| Correo institucional | `email` | `email` | `email` | No a nivel HTML; sí se valida en JS |

#### Validaciones actuales

Implementadas en `src/scripts/suscripcion.js`: si `nombre` o `correo` (tras `.trim()`) vienen vacíos, se muestra `alert("Complete todos los campos.")` y no se envía la petición. No hay validación de formato de email más allá del `type="email"` nativo del navegador, ni mensajes de error inline (el feedback usa `alert()`, no elementos del DOM).

#### Función de submit (implementada)

```javascript
// src/scripts/suscripcion.js
import { suscriptoresService } from "../lib/suscriptores";

document.addEventListener("DOMContentLoaded", () => {
  const formulario = document.querySelector("[data-suscripcion-form]");
  if (!(formulario instanceof HTMLFormElement)) return;

  formulario.addEventListener("submit", async (e) => {
    e.preventDefault();
    const nombre = formulario.querySelector("#full-name")?.value.trim();
    const correo = formulario.querySelector("#email")?.value.trim();
    if (!nombre || !correo) { alert("Complete todos los campos."); return; }

    try {
      await suscriptoresService.registrar({ nombre, correo });
      alert("¡Suscripción realizada correctamente!");
      formulario.reset();
    } catch (error) {
      alert(error instanceof Error ? error.message : "No fue posible registrar la suscripción.");
    }
  });
});
```

**Nota para el backend:** el payload usa las claves `nombre`/`correo` (español), no `fullName`/`email` como sugerían los `name` de los inputs HTML o versiones previas de este documento — `suscriptoresService.registrar()` hace ese mapeo. Confirmar que `POST /api/suscriptores` espera `{ nombre?, correo }` (ver `SuscriptorDTO` en `src/lib/suscriptores.ts` y el contrato documentado en `docs/api_usage.md`).

**Pendiente (no implementado aún):** feedback inline en el DOM en vez de `alert()`, estados de carga en el botón, confirmación por email y flujo de cancelación de suscripción — si el backend los expone, valdría la pena mejorarlo, pero no son bloqueantes para la integración actual.

---

### Formularios de login y registro

**No existen.** El sitio no tiene sistema de autenticación de usuarios. No hay páginas de `/login`, `/register` ni `/perfil`.

---

### Botón "SUSCRIBIRME" del Header

El `Header.astro` conservaba un botón con la etiqueta "SUSCRIBIRME" en versiones previas del sitio; actualmente está **comentado en el markup** (no se renderiza):

```html
<!-- src/components/Header.astro -->
<!-- <button class="border border-[#0049A4] ...">
  SUSCRIBIRME
</button> -->
```

No requiere integración porque no está visible; si se reactiva en el futuro, seguirá sin tener evento asociado hasta que se le asigne uno (scroll a la sección del formulario o modal).

---

## 8. Integración necesaria con Backend

La tabla siguiente cubre todas las pantallas del sitio y su estado real de integración con el Backend (actualizado 2026-08-02). El contrato de endpoints vigente está en `docs/api_usage.md` (reemplaza a la referencia `REFERENCIA_SCHEMA_BACKEND.md` citada en versiones previas de este documento, que ya no existe en el repositorio).

| Pantalla | Componente / Archivo | Endpoint | Método | Estado actual | Prioridad de lo pendiente |
|---|---|---|---|---|---|
| Catálogo de Especies | `especies.astro` → `species-source.ts` | `GET /api/plantas` | GET | **Integrado** (2026-08-01), con fallback a `.md` | — |
| Detalle de Especie | `especies/[slug].astro` → `species-source.ts` | `GET /api/plantas/:slug` | GET | **Integrado**, con fallback a `.md` | — |
| Atlas Etnobotánico | `Etnobotanicagrid.astro` → `species-source.ts` | (ninguno propio; reusa `GET /api/plantas`) | GET | Integrado indirectamente vía especies; no se prevé un endpoint dedicado | — |
| Inicio — Estadísticas | `index.astro` | `GET /api/stats` (o calcular sobre `getSpeciesList()`) | GET | Sin integrar — valores calculados/hardcodeados en el cliente | Media |
| Inicio — Suscripción | `index.astro` (formulario) + `suscripcion.js` | `POST /api/suscriptores` | POST | **Integrado** desde 2026-07-11 | — (ver mejoras opcionales en §7) |
| Panel de Comercio | `importacion-exportacion.astro` → `trade-data.ts` | `GET /api/comtrade/*` (existe en el backend, no consumido aún) | GET | Usa dataset pre-procesado `trade-data.json`, no llama al backend en runtime/build | Baja — ver nota abajo |
| Cancelar suscripción | No existe pantalla | `PATCH/DELETE /api/suscriptores/:id` | PATCH/DELETE | Sin implementar en frontend | Media |
| Confirmar suscripción (email) | No existe pantalla | `POST /api/suscriptores/confirmar` | POST | Sin implementar en frontend | Media |

> La sección de Blog fue eliminada el 2026-08-02; ya no aplica ninguna integración de `/api/blog` o `/api/noticias` a una pantalla equivalente del frontend (el backend sí expone `/api/noticias`, ver `docs/api_usage.md`, pero el frontend no la consume).

### Notas sobre el contrato de API de Plantas

Endpoints reales, según pruebas documentadas en `docs/api_usage.md` (2026-07-31):

```
GET /api/plantas           — sin auth. Listado liviano (no incluye `estado` en algunos casos;
                              species-source.ts trata la ausencia de estado como ACTIVO).
GET /api/plantas/:slug     — sin auth. 404 si no existe.
POST/PUT/DELETE /api/plantas/... — requieren rol SUPER_ADMIN o EDITOR.
```

El esquema del Frontend (`src/content.config.ts`) sigue alineado con este contrato. El campo `analisisAcademico` (con subcampos `taxonomia`, `etnobotanica`, `fitoquimica`, `sostenibilidad`) es exclusivo de la respuesta de la API — no existe en el frontmatter `.md` — y `species-source.ts` lo agrega tal cual venga de la API (`analisisAcademico: apiData.analisisAcademico ?? base.analisisAcademico`), sin fallback local porque no hay equivalente en Markdown.

**Sobre el panel de comercio:** el backend ya expone `GET /api/comtrade/catalogo`, `GET /api/comtrade/:plantaSlug` y `GET /api/comtrade/consulta/:plantaSlug` (probados end-to-end contra la API real de UN Comtrade, ver `docs/api_usage.md`), pero el frontend actual no los llama — el panel `/importacion-exportacion` lee de un archivo `src/data/trade-data.json` generado aparte. Consumir esos endpoints directamente en build time sería la evolución natural de esta sección, siguiendo el mismo patrón de fallback que `species-source.ts`.

---

## 9. Componentes relacionados con Suscriptores

### 9.1 Formulario de suscripción (inline en index.astro)

**Archivos:** `src/pages/index.astro` (markup) + `src/scripts/suscripcion.js` (lógica) + `src/lib/suscriptores.ts` (servicio).

El formulario sigue sin ser un componente `.astro` independiente — está escrito directamente en la sección de suscripción del home — pero su lógica de envío **ya está implementada e importada como script externo** (`src/scripts/suscripcion.js`), no inline. Ver el código completo en [§7](#7-formularios).

### 9.2 Botón "SUSCRIBIRME" del Header

**Archivo:** `src/components/Header.astro` — el botón existe en el markup pero está **comentado** (no se renderiza). Ver detalle en [§7](#7-formularios).

### 9.3 Mensajes de feedback

Implementados con `window.alert()` en `suscripcion.js` (éxito: "¡Suscripción realizada correctamente!"; error: el mensaje de la excepción o un genérico). No hay elementos inline en el DOM para mostrar los mensajes — sería la mejora más visible pendiente sobre este flujo.

---

## 10. Servicios HTTP

### Estado actual (actualizado 2026-08-02)

**Sí existen servicios HTTP**, en `src/lib/`:

| Archivo | Rol | Estado |
|---|---|---|
| `src/lib/api.ts` | Cliente HTTP base: `api.get(endpoint, { timeoutMs })` (con `AbortController`) y `api.post(endpoint, body)` | Implementado |
| `src/lib/species-source.ts` | Servicio de especies: `getSpeciesList()`, `getSpeciesDetail(slug, mdData)`, con merge por campo API+`.md` | Implementado (ver [§5](#5-consumo-de-api)) |
| `src/lib/suscriptores.ts` | Servicio de suscriptores: `SuscriptorDTO`, `suscriptoresService.registrar(datos)` | Implementado |
| `src/lib/trade-data.ts` | Cruce de especies con el dataset de comercio (`trade-data.json`), no llama a la API de comercio del backend | Implementado, sin consumir `/api/comtrade/*` todavía |
| `src/lib/utils.ts` | Helper `cn()` para componentes shadcn/ui — no es un servicio HTTP | Implementado |

No se usa `axios` ni ninguna librería HTTP externa — todo es `fetch` nativo. La sección siguiente (`src/lib/api.ts` real) reemplaza el diseño propuesto en versiones previas de este documento (`apiFetch<T>()`), que nunca llegó a implementarse tal cual — la implementación real difiere en nombres y forma:

```typescript
// src/lib/api.ts — implementación real
const API_URL = import.meta.env.PUBLIC_API_URL ?? "";

export const api = {
  get: async (endpoint: string, { timeoutMs = 5000 }: { timeoutMs?: number } = {}) => {
    const controller = new AbortController();
    const timer = setTimeout(() => controller.abort(), timeoutMs);
    try {
      const response = await fetch(`${API_URL}${endpoint}`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        signal: controller.signal,
      });
      const data = await response.json();
      if (!response.ok) throw new Error(data?.message || "Error en la petición");
      return data;
    } finally {
      clearTimeout(timer);
    }
  },
  post: async (endpoint: string, body: any) => {
    const response = await fetch(`${API_URL}${endpoint}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });
    const data = await response.json();
    if (!response.ok) throw new Error(data.message || "Error en la petición");
    return data;
  },
};
```

`species-source.ts` usa `api.get()`; `suscriptores.ts` usa `api.post()`. No existe un `src/lib/species.ts` separado como sugería una versión previa de este documento — su rol lo cumple `species-source.ts`, que además hace el merge con Markdown (no es un simple wrapper de fetch).

**Si el backend agrega endpoints de comercio consumibles en build time**, el patrón a seguir sería el mismo de `species-source.ts`: intentar `api.get('/api/comtrade/...')`, fusionar o reemplazar el dataset pre-procesado, y caer al `trade-data.json` local ante error.

---

## 11. Flujo de Suscripción (implementado)

> Esta sección describía un flujo "objetivo" pendiente de implementar; a 2026-08-02 el flujo ya está en producción. Se deja como referencia del comportamiento real.

```
Usuario ve el formulario en la Home (/)
        ↓
Escribe nombre y correo
        ↓
Hace clic en "Suscribirme al boletín"
        ↓
JavaScript intercepta el submit (preventDefault) — src/scripts/suscripcion.js
        ↓
Valida que nombre y correo no estén vacíos (trim)
        ↓
[Si falla] → alert("Complete todos los campos.")
        ↓
[Si pasa] → suscriptoresService.registrar({ nombre, correo })
        ↓
POST /api/suscriptores   Body: { nombre?, correo }
        ↓
[Si respuesta ok] → alert("¡Suscripción realizada correctamente!") + formulario.reset()
[Si respuesta no-ok / error de red] → alert(mensaje de error del backend o genérico)
```

### Mejoras pendientes (no bloqueantes)

1. **Feedback inline** — reemplazar los `alert()` por elementos del DOM (mensajes de éxito/error, estado de carga en el botón).
2. **Validación de formato de email** — más allá del `type="email"` nativo del navegador.
3. **Confirmación por email** — si el backend la implementa, se necesitaría una página `/suscripcion/confirmar?token=...` que llame a `POST /api/suscriptores/confirmar`.
4. **Cancelación** — página o flujo para `PATCH`/`DELETE /api/suscriptores/:id`.
5. **Botón "SUSCRIBIRME" del Header** — si se reactiva, asignarle una acción (scroll al formulario o modal).

---

## 12. Cómo se resolvió la integración (y qué queda pendiente)

> Esta sección listaba recomendaciones para una integración que, a 2026-08-02, ya ocurrió parcialmente. Se reescribe para documentar **la estrategia real elegida** (útil para replicarla en las partes que faltan) en vez de un plan genérico.

### 12.1 Decisión clave: build-time fetch + fallback, no SSR/ISR

El equipo **no** cambió `astro.config.mjs` a `output: 'hybrid'`/`'server'` ni agregó un adaptador SSR, como sugería la versión anterior de este documento. En su lugar, `species-source.ts` hace el `fetch` a la API **dentro del build estático** (`getSpeciesList()`/`getSpeciesDetail()` corren en el frontmatter de las páginas, no en el navegador) y cae al contenido `.md` si la API no responde. Esto mantiene el sitio 100% SSG — sin servidor Node corriendo en producción — a costa de que el catálogo solo se actualiza en cada rebuild/redeploy, no en tiempo real. Si el catálogo necesita reflejar cambios del backend sin rebuild, ahí sí valdría la pena evaluar SSR/ISR para las rutas de especies específicamente.

### 12.2 Archivos que ya existen (no hace falta crearlos)

| Archivo | Función | Nota |
|---|---|---|
| `src/lib/api.ts` | Cliente HTTP base (`get` con timeout, `post`) | Implementado — ver [§10](#10-servicios-http) |
| `src/lib/species-source.ts` | Fuente de especies con merge API+`.md` | Implementado — cumple el rol que se había propuesto para `src/lib/species.ts` |
| `src/lib/suscriptores.ts` | Servicio de suscriptores | Implementado |
| `.env` | Variables de entorno (no commiteado, como corresponde) | En uso local/CI |

No existe `src/lib/types.ts` ni `.env.example` — si se quiere formalizar la integración para nuevos desarrolladores, `.env.example` con `PUBLIC_API_URL=` sería la adición de menor esfuerzo y mayor valor.

### 12.3 Lo que falta (pendiente real, no historia)

| Ítem | Dónde | Prioridad |
|---|---|---|
| Estadísticas del home vía `GET /api/stats` (o derivarlas de `getSpeciesList()` en vez de hardcodearlas) | `src/pages/index.astro` | Media |
| Consumir `GET /api/comtrade/*` en vez de (o adicionalmente a) `trade-data.json` pre-procesado | `src/lib/trade-data.ts` | Baja |
| Feedback inline (no `alert()`) en el formulario de suscripción | `src/scripts/suscripcion.js`, `index.astro` | Baja |
| `.env.example` documentando `PUBLIC_API_URL` | raíz del repo | Baja |
| Confirmación y cancelación de suscripción (si el backend los expone) | Nuevas páginas/flujos | Media |

### 12.4 Consideraciones que siguen aplicando

**Análisis académico (cuerpo Markdown):** en la página de detalle (`[slug].astro`) se sigue usando `<Content />` de Astro para renderizar el cuerpo del `.md`. El campo `analisisAcademico` que llega de la API (`taxonomia`, `etnobotanica`, `fitoquimica`, `sostenibilidad`) es **independiente** de ese cuerpo Markdown — no lo reemplaza, se agrega como dato adicional cuando la API lo provee (`species-source.ts` lo pasa tal cual, sin fallback porque no existe en el `.md`). Confirmar con el backend el formato exacto (texto plano vs. HTML) antes de renderizarlo con `set:html`.

**CORS:** el Backend debe permitir requests desde el dominio de build/preview del Frontend — aunque hoy la mayoría del tráfico a la API ocurre en build time (servidor a servidor), el `POST /api/suscriptores` sí ocurre desde el navegador del usuario final, así que CORS sigue siendo necesario para ese endpoint como mínimo.

**Paginación:** sigue siendo 100% client-side sobre todos los datos ya cargados (catálogo, atlas, tabla de comercio). No se implementó paginación server-side — con volúmenes de datos moderados (decenas de especies) no ha sido necesario, pero sería lo primero a revisar si el catálogo crece significativamente.

**Imágenes:** las URLs siguen viniendo completas en `multimediaPrincipal.imagenUrl` (hoy picsum.photos/Unsplash, Cloudinary planificado a futuro vía `imagenPublicId`). Sin cambios respecto a la recomendación original.
