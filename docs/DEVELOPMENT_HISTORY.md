# Historial de Desarrollo — Ecotec Flora Médica

Narrativa cronológica del desarrollo del proyecto reconstruida a partir del historial de Git. Los mensajes de commit se citan textualmente. El contexto inferido está marcado como **[inferido]**.

---

## Resumen de la Línea de Tiempo

```mermaid
gantt
    dateFormat  YYYY-MM-DD
    title Ecotec Flora Médica — Línea de Tiempo de Desarrollo

    section Fundación
    Scaffold de Astro y configuración de Tailwind  :done, 2026-06-13, 1d
    Componentes base (BaseHead/Header/Footer)       :done, 2026-06-13, 1d
    Colección de blog y configuración de contenido  :done, 2026-06-13, 1d
    Mock del home y páginas mock de especies/etno   :done, 2026-06-13, 1d

    section Encabezado y Pulido
    Estilos del header, subrayado GSAP, favicon     :done, 2026-06-14, 3d
    Header responsive (breakpoint md)               :done, 2026-06-16, 1d

    section Trabajo Paralelo del Equipo
    SebastianAllauca — Catálogo de especies         :done, 2026-06-22, 4d
    Jean — Sección de etnobotánica                  :done, 2026-06-24, 5d

    section Rediseño del Home
    Reconstrucción del nuevo layout del home        :done, 2026-06-20, 3d
    Home finalizado                                 :done, 2026-07-01, 1d

    section Fusión de Ramas
    Merge sebas en dev                              :milestone, 2026-07-03, 0d
    Merge jean en dev                               :milestone, 2026-07-03, 0d
    Merge adrian en dev                             :milestone, 2026-07-03, 0d

    section Contenido y Refinamiento
    Migración de MD de especies y nuevo esquema     :done, 2026-07-03, 3d
    Actualización del layout de página de especie   :done, 2026-07-05, 1d
    Limpieza de documentación                       :done, 2026-07-06, 1d
```

---

## Fase 1 — Inicialización del Proyecto (2026-06-13)

### Scaffold Inicial de Astro
**Commit:** `"Initial commit from Astro"` — `houston[bot]`

El proyecto se inicializó usando el starter oficial de la CLI de Astro. Esto creó la estructura base del proyecto incluyendo el `package.json` predeterminado, tsconfig y páginas de marcador de posición.

### Configuración de Tailwind
**Commits:** `[chore] actualización de dependencias. Tailwind para el proyecto`, `[chore] actualización de configuración de astro`, `[chore] actualización de permisos para hacer builds con pnpm`

Inmediatamente después del scaffold, Adrian Palma configuró Tailwind CSS v4 usando el enfoque del plugin `@tailwindcss/vite` (la nueva integración nativa de Vite con Tailwind, no la basada en PostCSS). El archivo `astro.config.mjs` se actualizó para agregar el plugin Vite de Tailwind y la integración de `@astrojs/sitemap`. **[inferido]** La decisión de usar Tailwind v4 desde el inicio sugiere que el equipo quería las últimas funcionalidades (tokens de diseño nativos de CSS mediante `@theme`, sin necesidad de archivo `tailwind.config.js`).

### Fundación de SEO
**Commits:** `[feat] título y descripción meta para SEO del home`, `[feat] componente que configura de metadata a través de props para cada página`

Se construyó tempranamente un componente `BaseHead.astro` reutilizable — antes de cualquier página real — demostrando una mentalidad de SEO desde el principio. Acepta las props `title`, `description` y una `image` opcional, y genera etiquetas OG/Twitter Card completas en cada página.

### Componentes de Infraestructura Base
**Commit:** `[feat] componentes generales para metadata, header y footer`

`Header.astro`, `Footer.astro`, `HeaderLink.astro` y `Layout.astro` se crearon en un único commit. Esto estableció el envoltorio de página universal que utilizan todas las rutas. **[inferido]** El enfoque todo-en-uno sugiere que se realizó como una sesión de configuración rápida.

### Configuración de Contenido y Blog
**Commits:** `[feat] configuración de contenido para posts y post example`, `[feat] configuración de página de blog y slug para blog post`

Se configuró la colección de contenido de Astro para `blog` con un esquema Zod, se agregó una entrada de ejemplo y se crearon las páginas de índice `/blog` y de detalle `/blog/[slug]`.

### Sitemap y Robots
**Commits:** `[chore] configuración de sitemap` (×2), `[feat] configuración de robots`

Se configuró la integración del sitemap (aparentemente necesitó dos intentos) y se creó un endpoint `robots.txt.ts` que referencia automáticamente la URL del sitemap.

### Páginas Mock
**Commits:** `[feat] mock de la página home`, `[feat] páginas mock para especies y etnobotánica`, `[feat] layout wrapper reutilizable`

Se creó contenido provisional para las páginas de inicio, especies y etnobotánica. El envoltorio reutilizable `Layout.astro` se extrajo en este punto.

---

## Fase 2 — Pulido del Encabezado y Sistema de Diseño (2026-06-13 al 2026-06-16)

### Primera Pasada de Estilos
**Commits:** `[style]`, `[style] cambios de estilo del botón suscribirme`, `[style] cambios en el estilo del texto del footer y botón suscribirse del header`

Múltiples commits de estilo rápidos refinaron el botón del encabezado, el texto del pie de página y la apariencia general. **[inferido]** Estos commits representan una sesión exploratoria de diseño más que incrementos planificados.

### Comportamiento del Encabezado
**Commit:** `[feat + style] cambios en el comportamiento del navbar y estilo general de la página`

El encabezado se convirtió de una barra estática a un elemento fijo con efecto de vidrio esmerilado usando `backdrop-blur` y `bg-white/60`. Los enlaces de navegación recibieron su lógica de detección de estado activo.

### Animación GSAP Añadida
**Commits:** `[chore] actualización de dependencias, gsap para animaciones`, `[feat] animación de underline para header links`

Se añadió GSAP como dependencia de producción y se implementó la animación de subrayado en los enlaces del encabezado: un subrayado animado con `scaleX` que se expande al pasar el cursor y se retrae hacia la derecha al salir, usando una timeline de GSAP con `tweenFromTo`.

### Favicon
**Commit:** `[chore] nuevo favicon agregado`

Se añadió un `favicon-02.ico` personalizado a `public/` y se conectó a `BaseHead.astro`, reemplazando el favicon SVG predeterminado de Astro.

### Encabezado Responsive
**Commit:** `[feat] responsive header para pantallas md`

La navegación recibió padding responsive (`md:px-6 lg:px-60`) para evitar que los elementos del nav colapsaran en pantallas medianas.

---

## Fase 3 — Rediseño de la Página de Inicio (2026-06-20 al 2026-07-01)

### Trabajo Inicial en el Layout del Home
**Commits:** `[feat] avance del nuevo layout para home`, `[fix] avance en la reconstrucción del diseño del home`, `[fix] espaciado de los elementos del header ajustados`

La página de inicio predeterminada de Astro se reemplazó con un layout personalizado de múltiples secciones. Esto implicó commits iterativos corrigiendo alineación y espaciado — particularmente entre el encabezado fijo y el contenido de la página. **[inferido]** Los dos commits `[fix]` sugieren que el padding superior `pt-24` para compensar el encabezado fijo se calibró durante este período.

### Home Finalizado
**Commit:** `[feat] página home`

Se confirmó la página de inicio completa con todas las secciones:
- Hero con layout dividido (titular + cuadrícula de imágenes)
- Barra de contador de estadísticas (27 especies, 10 familias, 4 dimensiones de estudio, 3 matrices analíticas) — fondo verde `#27692b`
- Sección del formulario de suscripción al boletín (fondo oscuro/negro)
- Sección "Marco de Estudio" con 4 tarjetas (fondo azul claro `#eaf5ff`)

---

## Fase 4 — Desarrollo Paralelo por Ramas (2026-06-22 al 2026-06-29)

El desarrollo se dividió en al menos tres ramas con nombre: `sebas` (SebastianAllauca), `jean` (Jean) y `adrian` (Adrian Palma). Un cuarto colaborador ("Astic" / Luis) realizó dos commits experimentales (`prueba xd`, `preuba 2 xd`) el 17-06-2026.

### Rama: sebas — Catálogo de Especies (SebastianAllauca)

| Fecha | Commit |
|---|---|
| 2026-06-22 | `[feat]: catálogo de especies optimizado con funciones de búsqueda y filtrado` |
| 2026-06-22 | Merge `sebas` → `dev` |
| 2026-06-26 | `feat(especies): migrar schema al contrato real del backend` |
| 2026-06-26 | `docs: agregar referencia de schema del backend` |
| 2026-06-26 | `[feat]: implementar catálogo de especies con páginas de detalle y mejoras de UI` |
| 2026-06-26 | `[docs]: Creación de bitácora semanal rama sebas` |
| 2026-06-26 | `[docs]: Edición de bitácora semanal rama sebas` |

SebastianAllauca construyó la funcionalidad central de especies: la página de catálogo `/especies` con búsqueda, filtro taxonómico y paginación, además de las páginas de detalle `/especies/[slug]`. Un commit crítico fue "migrar schema al contrato real del backend" — reemplazando un esquema provisional simple con el contrato rico de especies (taxonomía, etnobotánica, historia, comercio, compuestos químicos, multimedia) destinado a reflejar una API real de backend. Los commits `[docs]` indican que este desarrollador también mantenía un diario de desarrollo semanal **[inferido: requisito de un proyecto universitario]**.

### Rama: jean — Sección de Etnobotánica (Jean)

| Fecha | Commit |
|---|---|
| 2026-06-24 | `Actualizacion de la página etnobotánica` |
| 2026-06-29 | `feat: agrega sección etnobotánica` |
| 2026-06-29 | `merge con origin/jean` |
| 2026-06-29 | `feat: agrega paginación en etnobotánica` |

Jean construyó toda la sección `/etnobotanica`: el hero, los botones de filtro (7 categorías con íconos de Tabler), la cuadrícula de tarjetas y la paginación del lado del cliente — todo en aproximadamente una semana de trabajo.

---

## Fase 5 — Fusión de Ramas e Integración (2026-07-03)

**Commits:** `Merge branch 'adrian' into dev`, `Merge branch 'sebas' into dev`, `Merge branch 'jean' into dev`

Las tres ramas de funcionalidades se fusionaron en `dev` el mismo día. A esto le siguieron inmediatamente varios commits de integración y corrección:

- `[feat] minor changes`
- `[feat] actualización y especies agregadas`
- `[feat] etnobotánica agregada`
- `[feat] actualización de la página etnobotánica`
- `[feat] cambios de estilo en título de sección suscribirse`

---

## Fase 6 — Migración de Contenido y Refinamiento del Esquema (2026-07-03 al 2026-07-06)

### Nueva Estructura de Archivos MD
**Commit:** `[feat] nueva estructura para archivos md` (2026-07-04)

Los archivos de contenido Markdown de especies se refactorizaron para usar el esquema rico de frontmatter YAML (taxonomia, etnobotanica, perfilEtnobotanico, historiaEvolucion, comercio, compuestosQuimicos, multimediaPrincipal, estado). **[inferido]** Esta fue la carga masiva de contenido: 39 archivos de especies con perfiles detallados.

### Correcciones Menores
**Commits:** `cambios menores`, `[chore] actualización de archivos md`, `[chore] eliminación de parte del código` (2026-07-04)

Limpieza y correcciones de contenido después de la migración masiva.

### Actualización del Layout de la Página de Detalle de Especie
**Commit:** `[feat] actualización del layout de la página para cada planta` (2026-07-05)

La página de detalle `/especies/[slug]` recibió su última pasada de layout — agregando la sección hero completa, las migas de pan taxonómicas, secciones académicas/químicas/comerciales y la barra lateral del perfil etnobotánico.

### Limpieza de Documentación
**Commit:** `[chore] eliminación de archivos innecesarios para la nueva documentación` (2026-07-06)

Se eliminaron archivos obsoletos o redundantes para limpiar el repositorio en preparación para la generación de documentación.

---

## Colaboradores

| Nombre | Rol |
|---|---|
| Adrian Palma | Líder del proyecto, arquitectura, página de inicio, animaciones del header, coordinación |
| SebastianAllauca | Catálogo de especies, páginas de detalle de especies, definición del esquema del backend |
| Jean | Sección de etnobotánica (página del atlas, filtros, paginación) |
| Astic / Luis | Contribuciones experimentales (2 commits el 17-06-2026) |

---

## Decisiones Arquitectónicas Clave (Inferidas del Historial)

1. **Sin framework de frontend (React/Vue/Svelte)** — Toda la interactividad es TypeScript puro en etiquetas `<script>` de Astro. Esto mantiene el bundle mínimo y el despliegue muy sencillo.
2. **Colecciones de contenido en lugar de base de datos** — El equipo usó las colecciones de contenido con tipado de Astro como capa de datos local desde el inicio, con la intención explícita de intercambiarlas por una API real más adelante.
3. **Tailwind v4 (nativo de CSS)** — Elegido al inicio del proyecto; refleja el conocimiento de la dirección del ecosistema.
4. **Estrategia de ramas paralelas** — Las funcionalidades se desarrollaron de forma aislada (`sebas`, `jean`, `adrian`) y se fusionaron, coherente con el flujo de trabajo de un equipo universitario donde los miembros trabajan de forma independiente.
