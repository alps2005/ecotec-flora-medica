# Registro de Cambios — Ecotec Flora Médica

Derivado del historial de Git. Los commits se listan del más reciente al más antiguo dentro de cada versión. Los hashes de commit se abrevian a 7 caracteres.

---

## [Sin Publicar] — Rama `dev`

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
