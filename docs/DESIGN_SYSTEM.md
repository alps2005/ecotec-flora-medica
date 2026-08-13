# Sistema de Diseño — Ecotec Flora Médica

Todos los tokens están definidos en `src/styles/global.css` usando el bloque `@theme` de Tailwind v4, lo que los hace disponibles como clases utilitarias de Tailwind y propiedades CSS nativas en todo el proyecto. Desde el **1 de agosto de 2026** (`07782b6`) el sistema de diseño se renovó por completo: paleta de escalas azul/teal/neutral (identidad institucional ECOTEC) y tipografía unificada en Poppins, reemplazando la paleta verde botánica y las fuentes EB Garamond/Hanken Grotesk usadas hasta esa fecha. Además, el proyecto ahora integra **shadcn/ui** (componentes React) junto a los componentes `.astro`, por lo que parte de la configuración de tokens (`@theme inline`, variables `oklch(...)`) proviene del preset `shadcn/tailwind.css` y convive con los tokens propios del proyecto.

---

## Paleta de Colores

### Escalas de Marca (Tokens de Tema Personalizados)

Cada color de marca se define como una escala completa de 10 pasos (50–900) más un alias semántico (`primary`, `secondary`, `tertiary`) que apunta al paso 500/600 de su escala.

#### Primary (azul ECOTEC)

| Paso | Hex |
|---|---|
| 50 | `#F3F8FC` |
| 100 | `#DCEAF6` |
| 200 | `#BDD8EE` |
| 300 | `#94BFE2` |
| 400 | `#6CA4D5` |
| **500 (`--color-primary`)** | `#0A5CA5` |
| **600 (`--color-primary-fixed-dim`)** | `#084A84` |
| 700 | `#063863` |
| 800 | `#042642` |
| 900 | `#021321` |

#### Secondary (teal/celeste)

| Paso | Hex |
|---|---|
| 50 | `#F2FCFE` |
| 100 | `#D9F5FB` |
| 200 | `#B5EBF6` |
| 300 | `#82DCF0` |
| 400 | `#52CDE9` |
| **500 (`--color-secondary`)** | `#2BBAE2` |
| **600 (`--color-secondary-fixed-dim`)** | `#1E97B8` |
| 700 | `#16728B` |
| 800 | `#0F4C5D` |
| 900 | `#08262F` |

#### Neutral (azul marino — usado como `tertiary` y como base de texto)

| Paso | Hex |
|---|---|
| 50 | `#F4F6F9` |
| 100 | `#DDE2EA` |
| 200 | `#BEC8D5` |
| 300 | `#98A7BA` |
| 400 (`--color-text-muted`) | `#6D819C` |
| **500 (`--color-tertiary` / `--color-text`)** | `#1A2843` |
| **600 (`--color-tertiary-fixed-dim`)** | `#162239` |
| 700 | `#101A2C` |
| 800 | `#0A111D` |
| 900 | `#05080E` |

#### Whites

| Nombre | Hex | Uso |
|---|---|---|
| Pure White | `#FFFFFF` | — |
| Snow (`--color-surface`) | `#FCFCFC` | Fondo de página |
| Cloud | `#F8F9FA` | — |
| Soft | `#F4F6F8` | — |
| Mist (`--color-surface-muted`) | `#EEF2F7` | Superficies atenuadas, tarjetas secundarias |

### Alias Semánticos

```css
--color-primary: var(--color-primary-500);           /* #0A5CA5 */
--color-primary-fixed-dim: var(--color-primary-600);  /* #084A84 */
--color-secondary: var(--color-secondary-500);        /* #2BBAE2 */
--color-secondary-fixed-dim: var(--color-secondary-600); /* #1E97B8 */
--color-tertiary: var(--color-neutral-500);           /* #1A2843 */
--color-tertiary-fixed-dim: var(--color-neutral-600); /* #162239 */
--color-surface: var(--color-white-snow);             /* #FCFCFC */
--color-surface-muted: var(--color-white-mist);       /* #EEF2F7 */
--color-text: var(--color-neutral-500);               /* #1A2843 */
--color-text-muted: var(--color-neutral-400);         /* #6D819C */
```

Estos alias son los que se usan en la mayoría de componentes (`text-primary`, `bg-secondary`, `text-text-muted`, etc.), en lugar de referenciar directamente los pasos numéricos de cada escala.

### Fondo de Página

```css
body {
    background-color: var(--color-surface);
    background-image:
        radial-gradient(circle at top left, rgba(43, 186, 226, 0.18), transparent 28%),
        radial-gradient(circle at top right, rgba(10, 92, 165, 0.12), transparent 32%),
        linear-gradient(180deg, var(--color-surface) 0%, var(--color-white-mist) 100%);
    background-attachment: fixed;
}
```

### Tokens de shadcn/ui (`@theme inline` + `:root` / `.dark`)

Desde la integración de shadcn/ui (`3129b4b`), `global.css` también define el set estándar de variables de shadcn en formato `oklch()` — `--background`, `--foreground`, `--card`, `--popover`, `--primary`, `--secondary`, `--muted`, `--accent`, `--destructive`, `--border`, `--input`, `--ring`, `--chart-1`…`--chart-5`, `--sidebar-*` — con variantes para `:root` (claro) y `.dark`. Estas variables alimentan exclusivamente a los componentes `src/components/ui/*.tsx` (Table, Badge, Select, Chart, Card, Button, Tabs) generados por la CLI de shadcn y **no** deben confundirse con los alias `--color-primary` / `--color-secondary` propios del proyecto — ambos sistemas de color coexisten en la misma hoja de estilos pero sirven capas distintas (componentes React de shadcn vs. resto del sitio en Astro).

El proyecto actualmente no activa el modo oscuro (no hay toggle ni clase `.dark` aplicada en runtime); las variables `.dark` están definidas por el preset de shadcn pero sin uso activo.

---

## Tipografía

### Fuente Tipográfica

Desde el `07782b6` (1 de agosto de 2026), **Poppins** es la única familia tipográfica del proyecto, tanto para encabezados como para cuerpo y UI — reemplaza a EB Garamond (display) y Hanken Grotesk (cuerpo/UI) usadas anteriormente.

```css
--font-display: 'Poppins', sans-serif;
--font-body: 'Poppins', sans-serif;
```

Se carga vía `@import url(...)` de Google Fonts al inicio de `global.css`, con los pesos `300, 400, 500, 600, 700, 800` en normal e itálica.

```css
h1, h2, h3, h4, h5, h6 {
    font-family: 'Poppins', sans-serif;
    font-weight: 600;
}

body {
    font-family: 'Poppins', sans-serif;
}

.logo-font {
    font-family: 'Poppins', sans-serif;
    font-weight: 600;
    font-size: 1.5rem;
}
```

> **Nota histórica:** las capturas de escala tipográfica, tracking y patrones de tamaño documentados más abajo se establecieron durante la fase EB Garamond/Hanken Grotesk y en su mayoría siguen aplicando (los tamaños `text-7xl`, `text-5xl`, etc. no cambiaron con la migración de fuente), pero el efecto visual de itálicas decorativas cambió de una serif cursiva a la itálica de Poppins.

### Escala Tipográfica

| Uso | Tamaño | Peso | Notas |
|---|---|---|---|
| Titular hero (escritorio) | `text-7xl` (4.5rem) | `font-medium` | Inicio + hero de especie |
| Titular hero (tablet) | `text-6xl` (3.75rem) | `font-medium` | |
| Título de sección | `text-5xl`–`text-7xl` | `font-light` | Variante itálica usada de forma decorativa |
| Encabezado H2 de tarjeta | `text-4xl`–`text-5xl` | `font-semibold` | Secciones del detalle de especie |
| Título de tarjeta | `text-2xl`–`text-3xl` | `font-semibold` | `nombreComun` en tarjeta de especie |
| Etiqueta eyebrow | `text-xs`–`text-sm` | `font-bold`/`font-semibold` mayúsculas con tracking ancho | Etiquetas de sección, p. ej. "Sobre nosotros", "Comercio internacional" |
| Texto del cuerpo | `text-base`–`text-lg` | `font-normal` | Altura de línea `leading-7`–`leading-8` |
| Metadatos pequeños | `text-sm` | `font-medium` | Nombres de familia, fechas, etiquetas |
| Etiqueta micro | `text-[10px]`–`text-xs` | `font-bold` mayúsculas | Etiquetas de rango taxonómico, texto de insignias |

### Espaciado de Letras

El tracking ancho se usa extensivamente para etiquetas y eyebrows:
- `tracking-[0.2em]` — etiquetas de familia en tarjetas de especies, eyebrows de "Sobre nosotros"
- `tracking-[0.22em]` — etiquetas eyebrow de secciones en el detalle de especie
- `tracking-[0.25em]` — texto del botón del boletín
- `tracking-[0.3em]` — etiquetas de campos de formulario (todo en mayúsculas)
- `tracking-[0.1rem]` — botón "SUSCRIBIRME" del encabezado (actualmente comentado/oculto en `Header.astro`)

---

## Espaciado

El proyecto usa la escala de espaciado predeterminada de Tailwind (múltiplos de 0.25rem / 4px). Patrones comunes:

| Contexto | Valor | Notas |
|---|---|---|
| Padding superior de página | `pt-32` (8rem) | Compensa el encabezado fijo |
| Padding vertical de sección | `py-24` (6rem) | Ritmo estándar de sección |
| Padding de tarjeta | `p-6`–`p-8` (1.5–2rem) | Tarjetas y contenedores |
| Separación entre tarjetas | `gap-6`–`gap-8` (1.5–2rem) | Espacio de cuadrícula entre tarjetas |
| Ancho máximo del contenido | `max-w-3xl` (encabezados) · `max-w-370` (≈1480px) | Catálogo, etnobotánica, panel de comercio usan `max-w-370` desde el `f1f756f` (2026-08-13), que reemplazó el valor arbitrario `max-w-[1480px]` por la utilidad de escala de Tailwind v4 |
| Padding horizontal del cuerpo | `px-6` / `lg:px-8` | Padding responsive estándar |

---

## Radio de Borde

| Token | Valor | Uso |
|---|---|---|
| `--radius-card` | `1.5rem` | Tarjetas (definido como variable CSS) |
| `--radius` (shadcn) | `0.625rem` | Base de radios de componentes `ui/*.tsx` (`--radius-sm/md/lg/xl/2xl/3xl/4xl` derivan de esta) |
| `rounded-[1.5rem]` | 1.5rem | Tarjetas de especies, contenedores |
| `rounded-[1.75rem]` | 1.75rem | Tarjetas del catálogo de especies, tarjetas del equipo en "Sobre nosotros" |
| `rounded-[2rem]` | 2rem | Hero del detalle de especie |
| `rounded-[28px]` | 1.75rem | Tarjetas del marco de estudio, tarjetas de valores en "Sobre nosotros" |
| `rounded-[32px]` | 2rem | Tarjeta de cierre (CTA) de "Sobre nosotros" |
| `rounded-full` | 9999px | Botones, insignias, pastillas, paginación |
| `rounded-2xl` | 1rem | Tarjetas de etnobotánica, paneles narrativos del panel de comercio |
| `rounded-xl` | 0.75rem | Tarjetas de perfil internas en el detalle de especie |

El uso consistente de radios grandes (1.5–2rem) le da al sitio una estética suave y contemporánea.

---

## Sombras

| Token | Valor | Uso |
|---|---|---|
| `--shadow-card` | `0 20px 60px -24px rgba(10, 92, 165, 0.24)` | Definido pero referenciado de forma inline en algunos componentes |
| `shadow-[0_24px_70px_-28px_rgba(0,73,164,0.25)]` | Inline | Hover de tarjeta de especie / tarjetas de equipo |
| `shadow-[0_20px_60px_-24px_rgba(0,73,164,0.2)]` | Inline | Tarjeta de cierre CTA de "Sobre nosotros" |
| `shadow-sm` | Predeterminado de Tailwind | La mayoría de las tarjetas en reposo |

Las sombras usan el tinte azul primario (`rgba(10,92,165,...)` / `rgba(0,73,164,...)`) en lugar del negro neutro, creando un resplandor de marca coherente en los elementos clave.

---

## Íconos

El proyecto usa **Tabler Icons** mediante clases de fuente de íconos CDN (no importaciones SVG). El patrón de clase es `ti ti-[nombre-del-ícono]`.

| Sección | Uso |
|---|---|
| Etnobotánica | Botones de filtro + insignias de tarjeta por categoría (`ti-heart-plus`, `ti-tools-kitchen-2`, `ti-bolt`, `ti-flower`, `ti-sparkles`, `ti-seeding`, `ti-leaf`) |
| Sobre nosotros | Íconos de departamentos (`ti-database`, `ti-code`, `ti-palette`, `ti-server-2`, `ti-file-text`) y de contacto (`ti-brand-linkedin`, `ti-mail`) en las tarjetas del equipo |

Adicionalmente, el proyecto usa **Lucide** (vía `lucide-react`) como librería de íconos de los componentes shadcn/ui (`iconLibrary: "lucide"` en `components.json`).

Los íconos SVG inline (sin sistema de clases) se usan en:
- Sección "Marco de Estudio" del inicio — rutas SVG botánicas personalizadas para Taxonomía, Etnobotánica, Fitoquímica, Sostenibilidad
- `TaxonomyFilter.astro` — chevron SVG personalizado para el menú desplegable

---

## Componentes shadcn/ui

Desde `3129b4b` (configuración de React + shadcn/ui + Tailwind v4), el proyecto usa la CLI de shadcn (`components.json`) con estilo `base-nova`, color base `stone` y prefijo vacío. Los componentes generados viven en `src/components/ui/` y se usan únicamente dentro de las islas React (`client:load`) del panel de comercio:

| Componente | Archivo | Uso |
|---|---|---|
| `Table`, `TableHeader`, `TableBody`, `TableRow`, `TableCell` | `ui/table.tsx` | `TradeTable.astro` |
| `Badge` | `ui/badge.tsx` | Insignias "Comtrade" / "Narrativa (cualitativo)" en `TradeExplorer.tsx` y `TradeTable.astro` |
| `Select`, `SelectTrigger`, `SelectContent`, `SelectItem`, `SelectValue` | `ui/select.tsx` | Selector de especie en `TradeExplorer.tsx` |
| `Card` | `ui/card.tsx` | Disponible para paneles/tarjetas React |
| `Tabs` | `ui/tabs.tsx` | Disponible, sin uso activo aún |
| `Button` | `ui/button.tsx` | Disponible para acciones dentro de islas React |
| `ChartContainer`, `ChartTooltip`, `ChartTooltipContent`, `ChartLegend`, `ChartLegendContent` | `ui/chart.tsx` | Envoltorio de Recharts usado por `TradeExplorer.tsx` para el gráfico de barras de exportación/importación |

Estos componentes se combinan con `class-variance-authority`, `clsx` y `tailwind-merge` (vía `src/lib/utils.ts`, helper `cn()`) para variantes y merge de clases condicionales — el mismo patrón estándar de shadcn/ui.

---

## Botones

### Botón Primario (CTA)
```html
class="inline-flex items-center justify-center rounded-full bg-primary px-6 py-3
       text-sm font-semibold text-white transition hover:bg-primary-fixed-dim"
```
Usado para "Explorar especies" en la página de inicio y en el CTA de cierre de "Sobre nosotros".

### Botón Secundario (Contorno)
```html
class="inline-flex items-center justify-center rounded-full border border-primary/15
       bg-white/80 px-6 py-3 text-sm font-semibold text-tertiary transition
       hover:border-primary/25 hover:bg-white"
```
Usado para acciones secundarias como "Ver etnobotánica".

### Botón de Filtro (Activo) / (Inactivo)
```html
/* activo */   class="bg-primary text-white border-primary"
/* inactivo */ class="bg-white text-text-muted border-slate-300 hover:border-primary hover:text-primary"
```

### Botón de Paginación (Página Actual) / (Otras Páginas)
```html
/* actual */ class="rounded-full bg-primary px-3 py-1.5 font-semibold text-white transition"
/* otra */    class="rounded-full border border-tertiary/20 px-3 py-1.5 font-semibold text-text-muted
              transition hover:border-primary hover:text-primary"
```
Este patrón se repite en la paginación del catálogo de especies, del atlas de etnobotánica y de la tabla de comercio (`TradeTable.astro`).

---

## Tarjetas

### Tarjeta de Especie (`SpeciesCard.astro`)
- Exterior: `rounded-[1.75rem] border border-slate-200/80 bg-white shadow-sm`
- Hover: `-translate-y-1 shadow-[0_24px_70px_-28px_rgba(0,73,164,0.25)]`
- Imagen: relación de aspecto `aspect-4/3`, `object-cover`, escala al `105%` en hover de grupo

### Tarjeta de Etnobotánica (`Etnobotanicacard.astro`)
- `rounded-2xl border border-slate-200 bg-white`
- Hover: `-translate-y-0.5 border-primary/20 shadow-sm`
- Altura de imagen: fija `h-48`
- Insignia de categoría: posicionada en la esquina superior derecha con colores mapeados por categoría

### Tarjeta Flip del Equipo (`sobre-nosotros.astro`)
- Contenedor con `perspective-distant`; la cara interna gira 180° en `group-hover`/`group-focus-within` mediante `transform-3d` y `backface-hidden` — sintaxis nativa de utilidades 3D de Tailwind v4 (migrada desde los valores arbitrarios `[perspective:1200px]`/`[transform-style:preserve-3d]`/`[backface-visibility:hidden]` en `7e1d267`, 2026-08-03).
- Cara frontal: iniciales o foto sobre fondo pastel (`bg-primary/10` / `bg-secondary/15` alternado), nombre y rol.
- Cara trasera: degradado `from-tertiary to-primary` con enlaces a LinkedIn y correo.

### Tarjetas del Marco de Estudio (Inicio) / Valores (Sobre nosotros)
- `rounded-[28px]` con fondos pastel o tintados por rol (`bg-white`, `bg-primary/10`, `bg-secondary/15`)
- Contenido centrado con ícono/tag, título, descripción

---

## Animaciones

### Subrayado de Enlace del Encabezado (GSAP)
Implementado en `Header.astro` mediante un bloque `<script>` usando timelines de GSAP.

**Comportamiento:**
1. Al cargar la página, el subrayado está en `scaleX: 0` (invisible).
2. Al entrar con el cursor: la timeline reproduce de `0 → midway` — el subrayado escala de `0 → 1` desde la izquierda.
3. Al salir con el cursor: la timeline reproduce de `midway → end` — el subrayado escala de `1 → 0` deslizándose a la derecha (`xPercent: 100`).
4. Los enlaces de la página activa tienen `scaleX: 1` permanente (subrayado estático, sin animación).

### Escala de Imagen al Hover (CSS)
Todas las tarjetas y la imagen hero usan `group-hover:scale-105` con `transition-transform duration-300/500`.

### Elevación de Tarjeta al Hover (CSS)
Las tarjetas de especies usan `hover:-translate-y-1`; las de etnobotánica, la más ligera `hover:-translate-y-0.5`.

---

## Puntos de Quiebre Responsive

Se usan los puntos de quiebre estándar de Tailwind:

| Punto de Quiebre | Ancho Mínimo | Uso en el Proyecto |
|---|---|---|
| `sm` | 640px | Escalado tipográfico, cambios de dirección flex |
| `md` | 768px | Cambios de columnas en cuadrícula (1→2 columnas en tarjetas, cuadrícula de 12 col en detalle de especie) |
| `lg` | 1024px | Cambios de padding máximo, cambios de layout (apilado → lado a lado) |
| `xl` | 1280px | Cuadrícula de 4 columnas del marco de estudio/equipo, cuadrícula de 3 columnas de tarjetas de especie |

Patrones responsive notables:
- Tarjetas de especies: `md:w-[calc((100%-1.5rem)/2)]` → `xl:w-[calc((100%-3rem)/3)]` (1→2→3 columnas)
- Migas de pan taxonómicas: `flex-col` → `xl:flex-row`
- Tabla de comercio (`TradeTable.astro`): `overflow-x-auto` con celdas `whitespace-normal` para permitir el ajuste de texto en pantallas angostas
- Sección del boletín: se apila verticalmente en móvil, lado a lado en `lg`
