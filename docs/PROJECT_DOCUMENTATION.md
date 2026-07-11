# Documentación del Proyecto — Ecotec Flora Médica

---

## Resumen Ejecutivo

**Ecotec Flora Médica** es un herbario digital estático y plataforma de referencia académica construida con Astro 7. Cataloga especies de plantas medicinales nativas de Ecuador y América Latina, o de uso común en esas regiones, presentando cada entrada desde tres perspectivas complementarias: taxonomía, etnobotánica y fitoquímica. El sitio está concebido como un recurso educativo de acceso público y como una capa de datos estructurada destinada a conectarse con una API real de backend cuando el proyecto supere su etapa actual basada en archivos de contenido.

El proyecto es desarrollado de forma colaborativa por un equipo pequeño (Adrian Palma, SebastianAllauca, Jean y al menos otro colaborador identificado como "Astic" / Luis) en el marco de una iniciativa universitaria de *vinculación* (vinculación con la comunidad) de la institución Ecotec.

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
| [Astro](https://astro.build) | `^7.0.0` | Generador de sitios estáticos, enrutamiento, colecciones de contenido |
| [Tailwind CSS](https://tailwindcss.com) | `^4.3.1` | Estilos utilitarios mediante plugin de Vite |
| [@tailwindcss/vite](https://tailwindcss.com/docs/vite) | `^4.3.1` | Integra Tailwind v4 como plugin de Vite |
| [GSAP](https://gsap.com) | `^3.15.0` | Animaciones de subrayado en los enlaces del encabezado |
| [@astrojs/sitemap](https://docs.astro.build/en/guides/integrations-guide/sitemap/) | `^3.7.3` | Sitemap generado automáticamente en `/sitemap-index.xml` |
| [pnpm](https://pnpm.io) | workspace | Gestión de paquetes |
| Node.js | `>=22.12.0` | Requisito de entorno de ejecución |
| Zod | (incluido con Astro) | Validación de esquemas de colecciones de contenido |
| EB Garamond | Google Fonts | Tipografía de display / encabezados |
| Hanken Grotesk | Google Fonts | Tipografía de cuerpo / UI |

---

## Objetivos del Proyecto

- **Corto plazo:** Construir un herbario estático completo y visualmente pulido que cubra ~40 especies medicinales con perfiles taxonómicos y etnobotánicos completos.
- **Mediano plazo:** Reemplazar los archivos Markdown planos por un backend de API en vivo, utilizando el frontend de Astro existente como capa de consumo.
- **Largo plazo:** Contribuir a la preservación del conocimiento biocultural a través de una plataforma digital de acceso público, optimizada para SEO y con datos estructurados.

---

## Descripción General del Sitio Web

El sitio cuenta con cinco secciones principales accesibles desde la navegación:

| Sección | URL | Descripción |
|---|---|---|
| Inicio | `/` | Hero, contador de estadísticas, suscripción al boletín, resumen del marco de estudio |
| Catálogo de Especies | `/especies` | Cuadrícula filtrable y buscable de las 39 especies medicinales |
| Detalle de Especie | `/especies/[slug]` | Página completa por especie: taxonomía, historia, química, comercio |
| Atlas Etnobotánico | `/etnobotanica` | Atlas filtrado por categoría con 43 entradas de plantas |
| Blog | `/blog` | Artículos editoriales sobre temas botánicos y etnobotánicos |
| Entrada de Blog | `/blog/[slug]` | Artículo individual con renderizado en prosa |

La estética general del diseño es editorial y botánica: encabezados en fuente serif (EB Garamond), paleta principal en verde bosque y tarjetas redondeadas con efectos de sombra al pasar el cursor.

---

## Funcionalidades Principales

### 1. Catálogo de Especies con Búsqueda y Filtrado en Tiempo Real
- 39 especies cargadas desde las colecciones de contenido de Astro en tiempo de construcción.
- Búsqueda de texto libre sobre `nombreComun`, `nombreCientifico`, `familia` y `usoTradicional`.
- Filtro de familia taxonómica (menú desplegable).
- Paginación del lado del cliente (9 especies por página) con botones de anterior/siguiente y número de página.
- Mensaje de estado vacío cuando ningún resultado coincide.
- Toda la lógica de filtrado es TypeScript puro en una etiqueta `<script>` — sin dependencia de framework de frontend.

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
- 43 entradas con frontmatter simplificado (nombre, nombre científico, categoría, parte usada, uso, compuestos, imagen).
- Siete categorías de filtro: GENERAL, MEDICINAL, ALIMENTICIA, ESTIMULANTE, AROMÁTICA, RITUAL, AGROECOLÓGICA — cada una con un ícono de Tabler.
- Paginación (9 por página) con lógica de filtrado del lado del cliente.
- Las tarjetas enlazan a la página de especie completa correspondiente.

### 4. Blog
- Colección de contenido de Astro para artículos.
- Entradas etiquetadas, fechadas y ordenadas de forma descendente por fecha de publicación.
- Páginas de artículos individuales con renderizado de prosa de Tailwind Typography.

### 5. SEO y Visibilidad
- `BaseHead.astro` inyecta URL canónica, Open Graph y metaetiquetas de Twitter Card en cada página.
- Sitemap generado automáticamente mediante `@astrojs/sitemap`.
- `robots.txt` servido como endpoint estático con referencia al sitemap.
- Enlace al feed RSS referenciado en el `<head>`.

### 6. Navegación Animada
- Animación de subrayado en los enlaces del encabezado impulsada por GSAP: se extiende al pasar el cursor y se retrae al salir.
- El enlace de la página activa se muestra con subrayado permanente y peso en negrita.
- Encabezado fijo con efecto de vidrio esmerilado mediante `backdrop-blur`.

---

## Estado Actual

El proyecto está **en desarrollo activo** en la rama `dev`. A partir del 06-07-2026:

- 39 especies están completamente catalogadas con esquemas ricos.
- 43 entradas de etnobotánica están publicadas.
- Existe 1 entrada de blog (la infraestructura de la colección está lista para producción).
- El formulario de suscripción al boletín es una maqueta de UI — no tiene acción de backend conectada.
- Las imágenes se sirven desde `picsum.photos` (CDN de marcador de posición) con algunas de Unsplash; no se ha definido un alojamiento de medios definitivo.
- El directorio `scripts/` está vacío, lo que sugiere que se planean scripts de automatización pero aún no se han escrito.
- No hay ningún pipeline de CI/CD configurado; el despliegue parece ser manual.
