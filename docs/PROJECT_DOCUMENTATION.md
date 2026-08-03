# Documentación del Proyecto — Ecotec Flora Médica

---

## Resumen Ejecutivo

**Ecotec Flora Médica** es un herbario digital y plataforma de referencia académica construida con Astro 7, con islas de React para sus vistas de datos más interactivas. Cataloga especies de plantas medicinales nativas de Ecuador y América Latina, o de uso común en esas regiones, presentando cada entrada desde perspectivas complementarias: taxonomía, etnobotánica, fitoquímica y comercio internacional. Desde agosto de 2026 el sitio ya no depende únicamente de archivos de contenido: la capa `src/lib/species-source.ts` consulta primero la API real del backend (`GET /api/plantas`) y usa el Markdown local solo como respaldo cuando el backend no responde, por lo que el proyecto está a medio camino de su migración a datos en vivo.

El proyecto es desarrollado de forma colaborativa por un equipo pequeño en el marco de una iniciativa universitaria de *vinculación* (vinculación con la comunidad) de la Universidad ECOTEC. Según la página `/sobre-nosotros`, los roles actuales son: Nestor Camilo Ruiz Conforme (docente de vinculación), Luis David Aurea Ramírez (líder de base de datos), Luis Anibal Eraso Viteri (líder de desarrollo), Raúl Stephano Coello Albán (líder de infraestructura, también autor de la app móvil Android) y Adrian Leniel Palma Santana (líder de diseño); el rol de líder de documentación está vacante.

---

## Propósito del Proyecto

El proyecto existe para:

1. **Preservar y digitalizar el conocimiento botánico** — específicamente las tradiciones etnobotánicas de las comunidades andinas y amazónicas, conectando el saber ancestral sobre el uso de plantas con el análisis fitoquímico moderno.
2. **Proveer contenido estructurado y citable** — cada entrada de especie sigue un esquema riguroso que cubre taxonomía, uso tradicional, compuestos químicos, historia de dispersión y comercio internacional.
3. **Servir como herramienta de enseñanza** — la sección "Marco de Estudio" de la página de inicio organiza explícitamente el conocimiento en cuatro dimensiones académicas: Taxonomía, Etnobotánica, Fitoquímica y Sostenibilidad.
4. **Actuar como andamiaje de frontend** — la página `especies.astro` indica explícitamente que el catálogo está "preparado para sustituir su contenido por una API real sin reestructurar la experiencia".

---

## Tecnologías Utilizadas

| Tecnología | Versión | Rol |
|---|---|---|
| [Astro](https://astro.build) | `^7.1.3` | Generador de sitios estáticos, enrutamiento, colección de contenido |
| [React](https://react.dev) | `^19.2.8` | Islas interactivas (panel de comercio internacional) vía `@astrojs/react` |
| [Tailwind CSS](https://tailwindcss.com) | `^4.3.2` | Estilos utilitarios mediante plugin de Vite |
| [@tailwindcss/vite](https://tailwindcss.com/docs/vite) | `^4.3.2` | Integra Tailwind v4 como plugin de Vite |
| [shadcn/ui](https://ui.shadcn.com) | `^4.16.0` | Componentes React (tabla, badge, select, chart, card, tabs, button) sobre Tailwind v4, estilo `base-nova` |
| [Recharts](https://recharts.org) | `3.8.0` | Gráfico de barras de exportación/importación en el panel de comercio |
| [GSAP](https://gsap.com) | `^3.15.0` | Animaciones de subrayado en los enlaces del encabezado |
| [@astrojs/sitemap](https://docs.astro.build/en/guides/integrations-guide/sitemap/) | `^3.7.3` | Sitemap generado automáticamente en `/sitemap-index.xml` |
| [pnpm](https://pnpm.io) | workspace | Gestión de paquetes |
| Node.js | `>=22.12.0` | Requisito de entorno de ejecución |
| Zod | (incluido con Astro) | Validación del esquema de la colección de contenido `species` |
| Poppins | Google Fonts | Tipografía única del sitio (display, cuerpo y UI) desde el 2026-08-01 |
| Tabler Icons + Lucide | CDN / `lucide-react` | Iconografía del sitio Astro (Tabler) y de los componentes shadcn/ui (Lucide) |

> La app móvil Android (`mobile/`, Kotlin + Jetpack Compose) es un proyecto Gradle independiente con su propio stack; ver [`mobile/README.md`](../mobile/README.md).

---

## Objetivos del Proyecto

- **Corto plazo:** Construir un herbario estático completo y visualmente pulido que cubra ~40 especies medicinales con perfiles taxonómicos y etnobotánicos completos. *(logrado)*
- **Mediano plazo:** Reemplazar los archivos Markdown planos por un backend de API en vivo, utilizando el frontend de Astro existente como capa de consumo. *(en curso: `species-source.ts` ya consulta la API con fallback a Markdown; falta retirar el respaldo local una vez el backend sea la fuente confiable en producción)*
- **Largo plazo:** Contribuir a la preservación del conocimiento biocultural a través de una plataforma digital de acceso público, optimizada para SEO y con datos estructurados.

---

## Descripción General del Sitio Web

El sitio cuenta con seis secciones principales accesibles desde la navegación:

| Sección | URL | Descripción |
|---|---|---|
| Inicio | `/` | Hero, contador de estadísticas, suscripción al boletín, resumen del marco de estudio |
| Catálogo de Especies | `/especies` | Cuadrícula filtrable y buscable de las especies medicinales (fuente: API + respaldo Markdown) |
| Detalle de Especie | `/especies/[slug]` | Página completa por especie: taxonomía, historia, química, comercio |
| Atlas Etnobotánico | `/etnobotanica` | Atlas filtrado por categoría, derivado de la misma fuente de especies, con modal de detalle rápido |
| Importación y Exportación | `/importacion-exportacion` | Panel de comercio internacional con cifras de UN Comtrade cruzadas por especie, gráfico interactivo y tabla paginada |
| Sobre Nosotros | `/sobre-nosotros` | Página institucional: misión, departamentos, equipo y valores (reemplazó al Blog el 2026-08-02) |

> La sección de **Blog** (`/blog`, `/blog/[slug]`) existió hasta el 2026-08-02, cuando se eliminó junto con su colección de contenido y el feed RSS, y fue reemplazada por "Sobre nosotros".

La estética general del diseño combina rigor científico e identidad institucional: encabezados y cuerpo en Poppins, paleta azul/teal ECOTEC (ver [`docs/DESIGN_SYSTEM.md`](./DESIGN_SYSTEM.md)) y tarjetas redondeadas con efectos de sombra al pasar el cursor.

---

## Funcionalidades Principales

### 1. Catálogo de Especies con Búsqueda y Filtrado en Tiempo Real
- Especies cargadas en tiempo de construcción vía `getSpeciesList()` (`src/lib/species-source.ts`): intenta la API real del backend (`GET /api/plantas`) y cae al contenido Markdown local si no responde.
- Búsqueda de texto libre sobre `nombreComun`, `nombreCientifico`, `familia` y `usoTradicional`.
- Filtro de familia taxonómica (menú desplegable).
- Paginación del lado del cliente (9 especies por página) con botones de anterior/siguiente y número de página.
- Mensaje de estado vacío cuando ningún resultado coincide.
- Toda la lógica de filtrado es TypeScript puro en una etiqueta `<script>` — la única isla de framework de frontend del sitio es el panel de comercio (ver punto 4).

### 2. Páginas de Detalle Completas por Especie
Cada página de especie en `/especies/[slug]` renderiza:
- Imagen hero a sangre completa con superposición de gradiente y nombres.
- Migas de pan taxonómicas (Reino → División → Clase → Familia → Género).
- Sección de análisis académico con contenido del cuerpo en Markdown.
- Historia evolutiva: Origen / Dispersión / Evolución en tarjetas de línea de tiempo.
- Cuadrícula de compuestos químicos activos (cada compuesto nombrado y explicado).
- Tabla de comercio internacional (exportadores e importadores).
- Barra lateral del perfil etnobotánico.

### 3. Atlas Etnobotánico
- Entradas derivadas de la misma fuente de especies (`getSpeciesList()`), sin colección de contenido propia desde el 2026-07-15.
- Siete categorías de filtro: GENERAL, MEDICINAL, ALIMENTICIA, ESTIMULANTE, AROMÁTICA, RITUAL, AGROECOLÓGICA — cada una con un ícono de Tabler.
- Paginación (9 por página) con lógica de filtrado del lado del cliente.
- Cada tarjeta abre un modal (`Etnobotanicamodal.astro`) con el detalle rápido de la ficha y también enlaza a la página de especie completa.

### 4. Panel de Importación y Exportación
- Página `/importacion-exportacion` con cifras de comercio exterior de Ecuador obtenidas de **UN Comtrade**, pre-procesadas en `src/data/trade-data.json` y cruzadas por especie mediante `src/lib/trade-data.ts`.
- Tarjetas KPI (especies con datos verificados, especies solo con relato cualitativo, principal destino de exportación/origen de importación).
- Explorador interactivo (`TradeExplorer.tsx`, isla React con `client:load`): selector de especie y gráfico de barras de exportación/importación por año (Recharts), con paneles narrativos de países cuando no hay cifras Comtrade.
- Tabla completa de especies (`TradeTable.astro`, construida con componentes `shadcn/ui`) con paginación del lado del cliente (10 filas por página).
- Cuando una especie no tiene código arancelario (HS) específico se usa la categoría comercial más cercana y se marca como "aproximada"; cuando Comtrade no reporta cifras, se muestra solo el relato cualitativo de la ficha, sin inventar números.

### 5. Sobre Nosotros
- Página institucional (reemplazó al Blog el 2026-08-02): misión, banda de departamentos colaboradores, tarjetas "flip" del equipo (con enlaces a LinkedIn/correo al pasar el cursor) y grid de valores.

### 6. SEO y Visibilidad
- `BaseHead.astro` inyecta URL canónica, Open Graph y metaetiquetas de Twitter Card en cada página.
- Sitemap generado automáticamente mediante `@astrojs/sitemap`.
- `robots.txt` servido como endpoint estático con referencia al sitemap.

### 7. Navegación Animada
- Animación de subrayado en los enlaces del encabezado impulsada por GSAP: se extiende al pasar el cursor y se retrae al salir.
- El enlace de la página activa se muestra con subrayado permanente y peso en negrita.
- Encabezado fijo (fondo blanco sólido, ya no vidrio esmerilado) con cuatro enlaces: Especies, Etnobotánica, Comercio, Sobre nosotros.

---

## Estado Actual

El proyecto está **en desarrollo activo** en la rama `dev`. A partir del 02-08-2026:

- 41 especies están catalogadas en `src/content/species/` como contenido de respaldo; el atlas etnobotánico se deriva de ellas (ya no existe una colección `etnobotanica` separada).
- La integración con el backend real está parcialmente completa: especies (`GET /api/plantas`) y comercio (Comtrade, vía dataset pre-procesado) ya consumen o cruzan datos de la API/fuentes externas; el formulario de suscripción al boletín ya llama a `POST /api/suscriptores` (integrado desde el 2026-07-11), condicionado a que `PUBLIC_API_URL` esté configurada en el entorno de build.
- La sección de Blog fue **eliminada** el 2026-08-02 y reemplazada por la página institucional "Sobre nosotros".
- El proyecto ahora incluye React (`@astrojs/react`), shadcn/ui y Recharts como dependencias, usadas exclusivamente en el panel de comercio — el resto del sitio sigue siendo Astro puro sin framework de cliente.
- Existe una **app móvil Android** independiente (Kotlin + Jetpack Compose) en `mobile/`, en fase 1 (datos locales empaquetados desde el contenido del sitio, sin conexión al backend todavía). Ver [`mobile/README.md`](../mobile/README.md).
- Las imágenes de especies se siguen sirviendo desde `picsum.photos`/Unsplash como marcador de posición; no se ha definido un alojamiento de medios definitivo (el campo `multimediaPrincipal.imagenPublicId` sugiere una futura migración a Cloudinary).
- No hay ningún pipeline de CI/CD configurado; el despliegue parece ser manual.
