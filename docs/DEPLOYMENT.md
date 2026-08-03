# Despliegue — Ecotec Flora Médica

---

## Descripción General

El proyecto es un **sitio Astro completamente estático** (SSG) sin tiempo de ejecución del lado del servidor — la isla React del panel de comercio (`TradeExplorer.tsx`) se hidrata en el navegador (`client:load`), no requiere un servidor Node. La salida de la compilación es un directorio de archivos HTML, CSS y JS que puede desplegarse en cualquier proveedor de alojamiento estático sin configuración adicional. La única dependencia de red en **tiempo de build** es opcional: si `PUBLIC_API_URL` apunta a un backend disponible, la compilación consulta especies reales; si no, usa el contenido Markdown de respaldo (ver [Variables de Entorno](#variables-de-entorno)).

---

## Proceso de Compilación

### Requisitos Previos

| Requisito | Versión |
|---|---|
| Node.js | `>=22.12.0` |
| pnpm | cualquier versión reciente |

### Scripts Disponibles

Definidos en `package.json`:

```json
{
  "scripts": {
    "dev": "NODE_ENV=development astro dev",
    "build": "astro build",
    "preview": "astro preview",
    "astro": "astro"
  }
}
```

> `NODE_ENV=development` se forzó explícitamente en `dev` el 2026-07-31 (`c6281ee`) para evitar un bug de `jsxDEV` relacionado con la integración de React.

| Comando | Descripción |
|---|---|
| `pnpm dev` | Inicia el servidor de desarrollo local en `http://localhost:4321` con HMR |
| `pnpm build` | Compila el sitio en `/dist` |
| `pnpm preview` | Sirve la compilación de `/dist` localmente para pruebas previas al despliegue |
| `pnpm astro` | Acceso directo a la CLI de Astro |

### Pasos de Compilación (lo que hace `astro build`)

```mermaid
flowchart LR
    A[pnpm build] --> B[Carga astro.config.mjs]
    B --> C[Escanea src/content/species\ncon cargador glob]
    C --> D[Valida frontmatter con Zod]
    D --> E{¿Validación exitosa?}
    E -->|No| F[La compilación falla con\nerror de esquema]
    E -->|Sí| G["species-source.ts: intenta\nGET /api/plantas (fallback a .md)"]
    G --> H[Renderiza todas las páginas\ngetStaticPaths para rutas dinámicas]
    H --> I[Procesa src/assets/\nvía pipeline de imágenes de Vite]
    I --> J[Empaqueta CSS de Tailwind + shadcn/ui\nvía @tailwindcss/vite]
    J --> K[Empaqueta scripts del cliente\nGSAP + JS de filtros + isla React]
    K --> L[Genera /sitemap-index.xml\nvía @astrojs/sitemap]
    L --> M[Salida en /dist]
```

### Estructura de la Salida de Compilación

```
dist/
├── index.html                  ← Página de inicio
├── especies/
│   ├── index.html              ← Catálogo de especies
│   ├── achiote/
│   │   └── index.html         ← Página de detalle por especie
│   ├── ajo/
│   │   └── index.html
│   └── ... (41 páginas de especies)
├── etnobotanica/
│   └── index.html
├── importacion-exportacion/
│   └── index.html              ← Panel de comercio (incluye la isla React hidratada)
├── sobre-nosotros/
│   └── index.html
├── robots.txt
├── sitemap-index.xml
├── favicon-02.ico
├── favicon.ico
├── favicon.svg
└── _astro/
    ├── [hash].css              ← Bundle de CSS de Tailwind
    └── [hash].js               ← Bundles de scripts del cliente (vanilla + isla React de TradeExplorer)
```

> La ruta `blog/` (con `primer-post/`) formaba parte de esta salida hasta el 2026-08-02, cuando la sección de Blog fue eliminada y reemplazada por `sobre-nosotros/`.

---

## Variables de Entorno

| Variable | Valor Predeterminado | Descripción |
|---|---|---|
| `SITE_URL` | `http://localhost:4321` | Usada por Astro como URL base `site` para URLs canónicas, etiquetas OG, sitemap y robots.txt |
| `PUBLIC_API_URL` | `''` (string vacío) | Base URL del backend, usada en **build time** por la integración de especies (`src/lib/species-source.ts`) y en **runtime del navegador** por el formulario de suscripción (`src/lib/suscriptores.ts`). Ver `docs/FRONTEND_INTEGRACION_BACKEND.md`. |

**Establece ambas variables en tu entorno de alojamiento/CI** para que coincidan con tu dominio de producción y tu backend real. Por ejemplo:
```
SITE_URL=https://flora.ecotec.edu.ec
PUBLIC_API_URL=https://api.ecotec-flora.com
```

> **Importante para el build:** si `PUBLIC_API_URL` no está definida o el backend no responde durante `astro build`, la integración de especies **no falla** — cae automáticamente al contenido Markdown de respaldo (`src/content/species/`). Esto hace que el build sea resiliente a una caída del backend, a costa de servir datos desactualizados hasta el siguiente rebuild.

Esta variable fluye hacia:
- `<link>` canónico en el head de cada página
- Meta tag `og:url` de Open Graph
- Meta tag `twitter:url` de Twitter
- URLs del sitemap
- Referencia del sitemap en `robots.txt`

---

## Alojamiento

**[inferido]** No hay ningún proveedor de alojamiento configurado en el repositorio (no se encontraron archivos `netlify.toml`, `vercel.json`, `.github/workflows/` ni similares). El proyecto puede desplegarse en cualquier host estático.

### Opciones de Alojamiento Recomendadas

| Proveedor | Esfuerzo | Notas |
|---|---|---|
| **Vercel** | Sin configuración | Detecta Astro automáticamente; establece la variable de entorno `SITE_URL` |
| **Netlify** | Sin configuración | Detecta Astro; admite arrastrar y soltar o integración con Git |
| **GitHub Pages** | Bajo | Requiere la ruta `base` en `astro.config.mjs` si no está en la raíz |
| **AWS S3 + CloudFront** | Medio | CDN completo; establece `SITE_URL` al dominio de CloudFront |
| **Cualquier servidor Apache/Nginx** | Bajo | Copia el contenido de `dist/` a la raíz web |

### Comandos de Despliegue

```bash
# Instalar dependencias
pnpm install

# Compilar
SITE_URL=https://tu-dominio.com pnpm build

# Desplegar: copiar dist/ a tu host
```

---

## CI/CD

**No hay ningún pipeline de CI/CD en el repositorio.** El directorio `scripts/` está vacío y no hay flujos de trabajo de GitHub Actions, GitLab CI ni similares.

**[inferido]** El despliegue es actualmente manual. Dado el equipo (proyecto universitario con una rama `dev` que recibe ramas de funcionalidades fusionadas), el flujo de trabajo esperado es:

1. El desarrollador hace push a su rama de funcionalidad.
2. El desarrollador líder revisa y fusiona en `dev`.
3. Cuando está listo, `dev` se fusiona en `main`.
4. Alguien ejecuta manualmente `pnpm build` y despliega.

### Configuración de CI/CD Sugerida (GitHub Actions)

Un flujo de trabajo mínimo para despliegue automático a cualquier host estático:

```yaml
# .github/workflows/deploy.yml
name: Despliegue

on:
  push:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: pnpm/action-setup@v4
      - uses: actions/setup-node@v4
        with:
          node-version: 22
          cache: pnpm

      - run: pnpm install --frozen-lockfile
      - run: pnpm build
        env:
          SITE_URL: ${{ secrets.SITE_URL }}

      # Luego agrega el paso de despliegue de tu proveedor de alojamiento
      # p. ej., para Netlify: netlify-labs/netlify-actions
      # p. ej., para Vercel: amondnet/vercel-action
```

---

## Optimización

### Lo que Astro Hace Automáticamente

| Optimización | Detalle |
|---|---|
| HTML estático | Todas las páginas se pre-renderizan — costo cero de renderizado del lado del servidor |
| Hashing de assets | Los archivos CSS y JS en `_astro/` tienen hashes de contenido para caché de larga duración |
| Empaquetado de CSS | Tailwind solo emite las clases utilitarias usadas (sin CSS muerto) |
| Optimización de imágenes | El pipeline de Vite de Astro optimiza las imágenes en `src/assets/` |
| División de código | El JS de cada página es mínimo; los scripts de GSAP y filtros solo se empaquetan cuando son necesarios |
| Sitemap | Generado automáticamente en tiempo de compilación |

### Carga de Imágenes Externas

Actualmente, todas las imágenes de especies se cargan desde CDN externos (picsum.photos, Unsplash). Esto significa que:
- Las imágenes **no** son procesadas por el pipeline de imágenes de Astro.
- No se genera carga diferida (lazy-loading) ni `srcset` para las imágenes de especies.
- Existe una dependencia de la disponibilidad de servicios externos.

**[inferido]** La migración a Cloudinary (sugerida por los campos `imagenPublicId` y `proveedor` en el esquema de especies) permitiría:
- Transformaciones de imágenes responsive.
- Eliminación de la dependencia de picsum.photos.
- Gestión coherente de medios.

### Consideraciones de Rendimiento

- GSAP se carga como dependencia JS del lado del cliente en **cada** página (animación del encabezado). Con ~70KB minimizado+comprimido, sigue siendo el payload compartido por todo el sitio.
- La página `/importacion-exportacion` carga adicionalmente React + Recharts + los componentes shadcn/ui para hidratar `TradeExplorer.tsx` (`client:load`) — un payload notablemente mayor que el resto del sitio, pero **acotado a esa sola página**: el resto del sitio no paga ese costo.
- La fuente de íconos Tabler (usada en etnobotánica y en "Sobre nosotros") se carga desde un CDN vía `<link>` en `global.css`/componentes — no se empaqueta. Los componentes `shadcn/ui` usan en cambio `lucide-react`, que sí se empaqueta como parte del bundle de la isla React.
- La Google Font `Poppins` (única tipografía del sitio desde el 2026-08-01) se carga en tiempo de ejecución desde `fonts.googleapis.com`. Para producción, se deberían considerar sugerencias de preconexión y subconjuntos de fuentes.
