# Registro de Cambios — Ecotec Flora Médica

Derivado del historial de Git. Los commits se listan del más reciente al más antiguo dentro de cada versión. Los hashes de commit se abrevian a 7 caracteres.

---

## [Sin Publicar] — Rama `dev`

### 2026-08-13

- `f1f756f` **fix:** Tailwind classes — se reemplazan valores arbitrarios (`max-w-[1480px]`, `min-h-[26rem]`, `w-[480px]`/`h-[600px]`, `bg-gradient-to-r`) por las utilidades nativas de Tailwind v4 equivalentes (`max-w-370`, `min-h-104`, `w-120`/`h-125`, `bg-linear-to-r`) en `especies.astro`, `importacion-exportacion.astro` e `index.astro`.

### 2026-08-12

- `eff9c9d` **content:** Completar información faltante en fichas de especies — se investiga y redacta taxonomía, historia/evolución, compuestos químicos y comercio (exportación/importación) para 17 especies con campos vacíos (hortensia, lazo-de-amor, madre-de-miles, dulcamara, tomate, naranjo-amargo, perejil, y 10 especies a las que solo les faltaba el texto de evolución). No cambia el esquema ni el conteo de especies (sigue en 41).

### 2026-08-03

- `955871a` **chore:** Actualización de la documentación — pasada de actualización sobre `docs/` para reflejar el estado del proyecto a esa fecha.
- `7e1d267` **chore:** Actualización de la sintaxis de Tailwind para la página "Sobre nosotros" — se migran valores arbitrarios de transformación 3D (`[perspective:1200px]`, `[transform-style:preserve-3d]`, `[backface-visibility:hidden]`, `bg-gradient-to-r/br`) a las utilidades nativas de Tailwind v4 (`perspective-distant`, `transform-3d`, `backface-hidden`, `bg-linear-to-r/br`) en la tarjeta "flip" del equipo y la banda de departamentos.

### 2026-08-02

- `a979739` **feat:** Reemplazar sección de blog por página "Sobre nosotros" — se elimina por completo la funcionalidad de blog: colección de contenido `blog`, páginas `src/pages/blog/index.astro` y `src/pages/blog/[slug].astro`, el endpoint `rss.xml.js` (agregado apenas el 27-07) y las referencias a RSS en `BaseHead.astro`. En su lugar se agrega `src/pages/sobre-nosotros.astro`: página institucional con hero, misión, banda de departamentos colaboradores, tarjetas "flip" del equipo (con LinkedIn/correo al hover) y grid de valores. `Header.astro` cambia el enlace "Blog" por "Sobre nosotros" y el `index.astro` ajusta sus CTAs.

### 2026-08-01

- `07782b6` **feat:** Renovar sistema de diseño y agregar paginación a la tabla de comercio — **segundo cambio de identidad visual global**: la paleta pasa del azul institucional plano (`#0049A4`) a escalas completas de 10 pasos `primary`/`secondary`/`neutral` (`#0A5CA5`/`#2BBAE2`/`#1A2843`) y la tipografía se unifica en Poppins (reemplaza EB Garamond + Hanken Grotesk). Se corrige el layout de `Etnobotanicacard.astro` y se agrega paginación del lado del cliente (10 filas por página) a `TradeTable.astro`. Documentado en `docs/design-system.md` (nota: superseded — ver `docs/DESIGN_SYSTEM.md`).
- `580adf5` (stexc7) **feat:** Mejoras app móvil: legibilidad, detalle etnobotánica, silueta pro, animaciones, release minificado — se agrega la pantalla `EtnobotanicaDetailScreen.kt` con su ruta propia, se mejora `BodySilhouette.kt` y se agregan animaciones (`Animations.kt`) y ajustes de tipografía/color en la app Android.
- `f727945` (stexc7) **feat:** App móvil Android (Kotlin + Compose) — fase 1 datos locales — primera versión de la app nativa Android en `mobile/`: arquitectura por capas (`data/model`, `data/repository` con patrón Repository), navegación con Compose Navigation, pantallas de inicio/especies/detalle/etnobotánica/autores, y un dataset local (`assets/content.json`) extraído del contenido del sitio. No depende del backend en esta fase. Ver `mobile/README.md`.
- `59d646b` **feat:** Integrar datos de especies desde la API con respaldo en Markdown — se agrega `src/lib/species-source.ts`, la nueva fuente única de especies: intenta `GET /api/plantas`/`GET /api/plantas/:slug` y hace *merge por campo* con el contenido `.md` existente, cayendo a este último ante cualquier error o timeout. `src/lib/api.ts` gana un método `get()` con `AbortController`/timeout de 5s. Todas las páginas y componentes que antes llamaban a `getCollection('species')` directamente (`especies.astro`, `especies/[slug].astro`, `index.astro`, `Etnobotanicagrid.astro`) pasan a usar `getSpeciesList()`/`getSpeciesDetail()`.

### 2026-07-31

- `e04f354` **chore:** Api usage manual update — actualización de `docs/api_usage.md` con hallazgos de pruebas contra el backend real (bugs de validación, colecciones de Mongo desalineadas, endpoints verificados).
- `7559d39` **chore:** Agregar manual de uso de la API del backend — creación inicial de `docs/api_usage.md`: base URL, autenticación JWT, tabla de endpoints por módulo (auth, configuración, fuentes, auditoría, plantas, usuarios admin, multimedia, noticias, Comtrade).
- `d0a387e` **feat:** Agregar sección de importación y exportación con datos de UN Comtrade — nueva página `src/pages/importacion-exportacion.astro` con panel de comercio internacional: `TradeKpiCards.astro`, `TradeTable.astro` y la isla React `TradeExplorer.tsx` (selector de especie + gráfico Recharts + paneles narrativos). Se agrega `src/lib/trade-data.ts` (cruce del dataset `src/data/trade-data.json` con las especies) y `src/data/hs-code-map.mjs`. `Header.astro` gana el enlace "Comercio".
- `3129b4b` **chore:** Configurar React, shadcn/ui y Tailwind v4 en el proyecto — se agregan `@astrojs/react`, React 19, shadcn/ui (`components.json`, estilo `base-nova`), `class-variance-authority`, `clsx`, `tailwind-merge`, `tw-animate-css` y los primeros componentes generados en `src/components/ui/` (table, badge, select, card, tabs, button, chart). `astro.config.mjs` agrega la integración `react()`. Sienta la base para el panel de comercio del commit siguiente.
- `874c6d4` **fix:** Corregir colores del panel de etnobotánica en la ficha de especie — ajuste de clases de color en la barra lateral del perfil etnobotánico de `especies/[slug].astro`.
- `c6281ee` **fix:** Forzar `NODE_ENV=development` en el script `dev` para evitar el bug de jsxDEV — `package.json`: `"dev": "NODE_ENV=development astro dev"`.
- `cef74d5` **feat:** Nueva pantalla modal para etnobotánica con detalles de la planta — se agrega `Etnobotanicamodal.astro`: modal de detalle rápido que se abre desde `Etnobotanicacard.astro` sin salir de `/etnobotanica`.
- `4ba325d` **chore:** Link de foto de zapallo actualizado — corrección puntual de `multimediaPrincipal.imagenUrl` en `zapallo.md`.

### 2026-07-27

- `8f1a789` **chore:** Minor changes — se elimina `src/content/species/aji.md` (especie retirada del catálogo) y ajustes menores de `package.json`/lockfile.
- `76cb87d` **fix:** Iconos de etnobotánica, feed RSS, formulario de suscripción y filtrado de especies activas — corrige varios cabos sueltos: carga la fuente de íconos Tabler (CDN) que faltaba para los badges de etnobotánica; agrega `@astrojs/rss` y `src/pages/rss.xml.js` para el feed que `BaseHead` ya anunciaba (eliminado más tarde junto con el blog, el 02-08); reescribe `src/scripts/suscripcion.js` para usar `suscriptoresService`/`api.ts` en vez de una URL localhost fija; aplica el filtro `estado === 'ACTIVO'` de forma consistente en `especies.astro`, `especies/[slug].astro` e `index.astro` (antes solo se aplicaba en `Etnobotanicagrid`); corrige el shuffle sesgado de imágenes del home (Fisher-Yates); valida `multimediaPrincipal.imagenUrl` como URL y evita colisiones de slug en `getStaticPaths`.

### 2026-07-16

- `80b49fb` **chore:** Actualización de astro config — se revirtió la integración `astro-icon` que se había agregado el día anterior, dejando la configuración en su estado base con `sitemap()` únicamente.

### 2026-07-15

- `2f06dfc` **feat:** Actualización del footer, cambios en la orientación de layout e icono de WhatsApp — se refactorizó `Footer.astro` con mayor detalle de markup (formato expandido por líneas), se actualizó el ícono de WhatsApp a uno SVG inline mejorado y se ajustó la orientación del layout de la columna derecha del pie de página.
- `e5314a7` **feat:** Actualización del badge de las tarjetas de etnobotánica — ajuste puntual en el estilo del badge en `Etnobotanicacard.astro`.
- `b6bf03d` **chore:** Actualización de astro config — se agregó la integración `astro-icon` a `astro.config.mjs` (revertida al día siguiente).
- `0e73a70` **chore:** Actualización de dependencias — actualización masiva de dependencias del proyecto; `pnpm-lock.yaml` reconstruido (~620 adiciones / ~1359 eliminaciones).
- `a4f18db` **feat:** Actualización de la página de etnobotánica — **cambio arquitectónico mayor**: se eliminó la colección independiente `etnobotanica` de `content.config.ts` y se eliminaron los 43 archivos `.md` de `src/content/etnobotanicacont/`. `Etnobotanicagrid.astro` fue refactorizado para derivar todos sus datos directamente de la colección `species` existente, extrayendo la categoría etnobotánica mediante parsing del campo `etnobotanica.clasificacion`. Las páginas `etnobotanica.astro` e `index.astro` fueron ajustadas para la nueva fuente de datos.
- `719f21e` **feat:** Footer actualizado al que está en la página de vinculación — se reemplazó el footer minimalista por un footer completo alineado con el sitio de vinculación de ECOTEC: columna de logos (Samborondón, Guayaquil, Costa), sección de admisiones con enlace a WhatsApp, links institucionales y copyright. Se agregaron cuatro nuevas imágenes a `src/assets/` (`isotipo-costa.webp`, `isotipo-guayaquil.webp`, `isotipo-samborondon.webp`, `logo-ecotec-2025-transparente.webp`).

### 2026-07-14

- `c131b32` **feat:** Ajuste de grid en homepage — revisión del layout de cuadrícula en `src/pages/index.astro` (+87/-38 líneas), refinando el sistema de columnas y espaciado del hero y secciones KPI.

### 2026-07-13

- `360694c` **feat:** KPIs de homepage actualizados — actualización de los valores numéricos y etiquetas de la barra de estadísticas de la homepage.
- `44d7813` **feat:** Refactor de paleta de colores y grid del home — **cambio de identidad visual global**: la paleta se migró de tonos verdes botánicos a azul institucional ECOTEC. Nuevos tokens en `global.css`:
  - `--color-primary: #0049A4` (azul ECOTEC)
  - `--color-secondary: #4DD0D1` (teal)
  - `--color-tertiary: #0A233C` (azul marino oscuro)
  - Se actualizó el fondo del `body` con gradientes radiales y lineales fijos.
  - Se propagaron los cambios de color a todos los componentes: `Header`, `HeaderLink`, `Footer`, `SpeciesCard`, `SpeciesGrid`, `TaxonomyFilter`, `SearchBar`, y todos los componentes de etnobotánica.
  - `src/pages/index.astro` fue reescrito extensamente con el nuevo layout (+273/-118 líneas).

### 2026-07-11

- `d9e2359` **chore:** Merge branch 'dev' into luis — fusión de la rama `dev` en la rama `luis` para mantener sincronización.
- `ce002ca` **feat:** Suscripción (Luis Eraso) — **nueva capa de integración con el backend**:
  - Se crearon `src/lib/api.ts` (cliente HTTP genérico que consume `PUBLIC_API_URL`) y `src/lib/suscriptores.ts` (servicio con interfaz `SuscriptorDTO` y llamada a `POST /api/suscriptores`).
  - Se agregó `src/scripts/suscripcion.js` con el handler del formulario del lado del cliente: validación de campos, llamada al servicio y manejo de errores.
  - Se actualizó `src/pages/index.astro` con el formulario de suscripción funcional conectado al backend.
  - Se generó `docs/FRONTEND_INTEGRACION_BACKEND.md` con la documentación completa de la integración frontend-backend.
- `e9c85407` **feat:** 3 plantas agregadas — se agregaron tres nuevas fichas de especies a `src/content/species/`: `hortensia.md`, `lazo-de-amor.md`, `madre-de-miles.md`. El total de especies sube a **42 entradas**.
- `e5fa554` **chore:** Actualización de imágenes para 8 plantas — se actualizaron las URLs de imagen (`multimediaPrincipal.imagenUrl`) en los archivos `.md` de: ajo, cedrón, dulcamara, eucalipto, hierba luisa, limón, neem y ruda.

### 2026-07-06



- `d96f00b` **chore:** Eliminación de archivos innecesarios para la nueva documentación — se limpiaron archivos obsoletos antes de la generación de documentación.

### 2026-07-05

- `7e59235` **feat:** Actualización del layout de la página para cada planta — revisión exhaustiva de la página de detalle de especie (`/especies/[slug]`), agregando sección hero, migas de pan taxonómicas, secciones académica/química/comercial y la barra lateral del perfil etnobotánico.

### 2026-07-04

- `c41a8ec` **feat:** Nueva estructura para archivos md — migración de los archivos Markdown de especies al esquema completo de frontmatter rico (taxonomía, etnobotánica, historia, comercio, compuestos químicos, multimedia).
- `62c0c25` **chore:** Cambios menores — correcciones menores de contenido.
- `68df05f` **chore:** Actualización de archivos md — correcciones y adiciones de contenido en archivos de especies.
- `c430bf5` **chore:** Eliminación de parte del código — eliminación de código redundante tras la migración de contenido.

### 2026-07-03

- `69470ee` **feat:** Cambios de estilo en título de sección suscribirse — actualización de los estilos del título de la sección del boletín.
- `4e44139` **feat:** Actualización de la página etnobotánica — actualizaciones post-fusión de la página de etnobotánica.
- `91dabee` **feat:** Etnobotánica agregada — entradas de contenido de etnobotánica añadidas tras la fusión de ramas.
- `e99598b` **feat:** Actualización y especies agregadas — entradas de especies adicionales y actualizaciones tras la fusión.
- `4abadbf` **feat:** Minor changes — ajustes menores post-fusión.
- `53307f5` **chore:** Merge branch 'jean' into dev — fusión del trabajo de etnobotánica de Jean en la rama principal de desarrollo.
- `0c50287` **chore:** Merge branch 'sebas' into dev — fusión del trabajo del catálogo de especies de SebastianAllauca.
- `956ddd8` **chore:** Merge branch 'adrian' into dev — fusión del trabajo de rediseño del home de Adrian.

---

## [Rama: jean] — Sección de Etnobotánica

### 2026-06-29

- `647d301` **feat:** Agrega paginación en etnobotánica — se agregó paginación del lado del cliente (9 por página) al atlas de etnobotánica con botones anterior/siguiente/numerados.
- `2edf853` **feat:** Agrega sección etnobotánica — se agregó la página completa del atlas de etnobotánica: hero, 7 botones de filtro por categoría (MEDICINAL/ALIMENTICIA/ESTIMULANTE/AROMÁTICA/RITUAL/AGROECOLÓGICA/GENERAL) con íconos de Tabler, cuadrícula de tarjetas y componente de tarjeta.
- `b565995` **chore:** Merge con origin/jean — sincronización de la rama jean con el remoto.

### 2026-06-24

- `2bef7e7` **feat:** Actualización de la página etnobotánica — trabajo inicial en la página de etnobotánica.

---

## [Rama: sebas] — Catálogo de Especies

### 2026-06-26

- `9843a9a` **feat:** Implementar catálogo de especies con páginas de detalle y mejoras de UI — se agregaron páginas de detalle completas de especies en `/especies/[slug]` con todas las secciones de datos estructurados; mejoras de UI al catálogo.
- `adc0820` **feat(especies):** Migrar schema al contrato real del backend — se reemplazó el esquema provisional de especies por el contrato rico completo que coincide con la API de backend prevista (taxonomía, etnobotánica, historia, comercio, compuestos químicos, multimedia, enum de estado).
- `1cacc45` **docs:** Agregar referencia de schema del backend — se añadió documentación de referencia del esquema del backend.
- `44582ce` **docs:** Edición de bitácora semanal rama sebas — actualización del diario de desarrollo semanal.
- `d74a995` **docs:** Creación de bitácora semanal rama sebas — creación del diario de desarrollo semanal.

### 2026-06-22

- `ef58070` **chore:** Merge branch 'sebas' into dev — primera fusión de la rama sebas en dev.
- `bcc2330` **feat:** Catálogo de especies optimizado con funciones de búsqueda y filtrado — catálogo de especies inicial con búsqueda del lado del cliente, filtro de familia taxonómica y paginación; componentes `SpeciesCard`, `SpeciesGrid`, `SearchBar`, `TaxonomyFilter`, `Pagination`.

---

## [Rama: adrian] — Rediseño de la Página de Inicio

### 2026-07-01

- `7b6037c` **feat:** Página home — página de inicio finalizada con todas las secciones: hero (layout dividido + cuadrícula de imágenes), barra de contador de estadísticas, formulario del boletín y cuadrícula de 4 tarjetas del "Marco de Estudio".

### 2026-06-21

- `e60671b` **fix:** Avance en la reconstrucción del diseño del home — continuación de la reconstrucción del layout del inicio.
- `7f5f0b6` **fix:** Espaciado de los elementos del header ajustados — corrección del espaciado de los elementos del header durante el rediseño del inicio.

### 2026-06-20

- `04b6ea3` **feat:** Avance del nuevo layout para home — inicio del trabajo en el nuevo layout; se reemplazó el home predeterminado de Astro por un diseño botánico personalizado.

---

## [Rama: main → dev] — Fase de Fundación

### 2026-06-17

- `070a1fa` (Astic) — `preuba 2 xd` — commit experimental.
- `0be85b5` (Astic) — `prueba xd` — commit experimental.

### 2026-06-16

- `97f80fd` **fix:** Alineación del tag time al centro.
- `c61342` **feat:** Responsive header para pantallas md — se añadió padding responsive para pantallas medianas.
- `c8a847` **fix:** Cambio en el peso del texto del logo en el header.
- `503fec6` **fix:** Cambio en el layout de las páginas alineado al header — ajuste del padding superior de la página para quedar por debajo del header fijo.
- `4becc2d` **feat:** Animación de underline para header links — animación de subrayado en los enlaces del header impulsada por GSAP (se expande al entrar el cursor, se retrae al salir).

### 2026-06-15

- `9d06d90` **style:** Cambios en la apariencia y comportamiento de estilos de los elementos del header.
- `5026bab` **chore:** Actualización de dependencias, gsap para animaciones — se añadió GSAP `^3.15.0` como dependencia de producción.
- `0713a37` **style:** Cambio en la sombra del header.
- `a6cf592` **chore:** Nuevo favicon agregado — se añadió el `favicon-02.ico` personalizado a `public/`.

### 2026-06-13

- `27fb570` **style:** Actualizaciones generales de estilos.
- `2e26b05` **style:** Cambios de estilo del botón suscribirme.
- `436d5f0` **style:** Cambios en el estilo del texto del footer y botón suscribirse del header.
- `eef84d6` **feat + style:** Cambios en el comportamiento del navbar y estilo general — header convertido a vidrio esmerilado fijo; pasada de estilos global.
- `6efa45d` **style:** Cambios en el layout del footer.
- `8de7ff4` **fix:** Sintaxis ambigua — se corrigió un error de sintaxis ambigua en un componente.
- `5ccfef1` **feat:** Configuración de página de blog y slug para blog post — se crearon `/blog/index.astro` y `/blog/[slug].astro` con renderizado basado en colección.
- `b8410d6` **feat:** Configuración de robots — se creó el endpoint `robots.txt.ts` con referencia automática al sitemap.
- `2cf67dd` **feat:** Mock de la página home — se creó la página de inicio provisional.
- `b156599` **feat:** Páginas mock para especies y etnobotánica — páginas provisionales para `/especies` y `/etnobotanica`.
- `36eadc` **feat:** Layout wrapper reutilizable — se creó `src/layouts/Layout.astro`.
- `61a60fc` **feat:** Configuración de contenido para posts y post example — `content.config.ts` con esquema Zod de la colección de blog, más el archivo `primer-post.md` de ejemplo.
- `75052fd` **feat:** Componentes generales para metadata, header y footer — se crearon `BaseHead.astro`, `Header.astro`, `HeaderLink.astro`, `Footer.astro`.
- `9c4cc6d` **chore:** Configuración de sitemap — integración de `@astrojs/sitemap` añadida a `astro.config.mjs`.
- `2fb5f4f` **chore:** Configuración de sitemap — segundo intento de configuración del sitemap.
- `ca6b671` **chore:** Actualización de assets — se añadieron `blog-placeholder.jpg` e imágenes de la cuadrícula del inicio a `src/assets/`.
- `252736c` **feat:** Título y descripción meta para SEO del home — `SITE_TITLE` y `SITE_DESCRIPTION` definidos en `src/consts.ts`.
- `1a4a8d4` **feat:** Componente que configura de metadata a través de props para cada página — implementación inicial de `BaseHead.astro`.
- `b123911` **feat:** Uso de Tailwind para estilos del proyecto — Tailwind CSS importado en `global.css`.
- `4b0d62a` **chore:** Actualización de configuración de Astro — `astro.config.mjs` actualizado con el plugin Vite de Tailwind y sitemap.
- `269cef7` **chore:** Actualización de dependencias. Tailwind para el proyecto — Tailwind CSS v4 y `@tailwindcss/vite` añadidos a `package.json`.
- `c57af3e` **chore:** Actualización de permisos para hacer builds con pnpm — se corrigieron los permisos de compilación de pnpm.
- `e703f09` (houston[bot]) **Initial commit from Astro** — proyecto creado con el starter de la CLI de Astro.
