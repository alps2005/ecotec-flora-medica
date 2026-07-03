# Migrar `species` y `etnobotanicacont` de JSON a Markdown

## 0. Por qué conviene el cambio

Ahora mismo `species` guarda textos largos (`analisisAcademico.taxonomia`,
`.etnobotanica`, `.fitoquimica`, `.sostenibilidad`) como *strings* dentro de un
JSON. Eso es justo lo que Markdown resuelve mejor: el texto largo va en el
**cuerpo** del archivo `.md` (con `##` headings, listas, negritas, etc.) y el
**frontmatter** se queda solo con los datos cortos y estructurados que
realmente vas a filtrar/mostrar como campos (nombre, familia, categoría,
multimedia, estado).

Ventajas prácticas:

- Puedes escribir/editar el contenido académico en Markdown normal en vez de
  escapar comillas dentro de un string JSON.
- Astro te da `render(entry)` para convertir el cuerpo en HTML listo para
  pintar, con soporte de headings, listas, énfasis, etc. sin que tengas que
  parsear nada a mano.
- Es el flujo "nativo" de Content Collections: `glob({ pattern: '**/*.md' })`
  ya lo tienes puesto en `blog`, solo falta aplicar el mismo patrón a las
  otras dos colecciones.

`etnobotanicacont` es más simple (`uso` y `compuestos` son strings cortos),
pero migrarla también tiene sentido por consistencia y porque esos campos
tienden a crecer con el tiempo. El cambio es mínimo.

---

## 1. `content.config.ts` actualizado

Reemplaza tu archivo completo por este. Los cambios están comentados.

```ts
import { defineCollection } from 'astro:content';
import { glob } from 'astro/loaders';
import { z } from 'astro/zod';

const blog = defineCollection({
  loader: glob({ pattern: '**/*.md', base: './src/content/blog' }),
  schema: z.object({
    title: z.string(),
    description: z.string(),
    pubDate: z.date(),
    updatedDate: z.date().optional(),
    tags: z.array(z.string()).default([]),
  }),
});

const species = defineCollection({
  // antes: pattern: '**/*.json'
  loader: glob({ pattern: '**/*.md', base: './src/content/species' }),
  schema: z.object({
    // slug eliminado: el nombre del archivo (p. ej. romero.md -> "romero")
    // ya funciona como id/slug con el loader glob. Si necesitas un slug
    // distinto al nombre del archivo, puedes dejarlo como opcional:
    slug: z.string().optional(),

    nombreComun: z.string(),
    nombreCientifico: z.string(),
    nombresAlternativos: z.array(z.string()).default([]),

    taxonomia: z.object({
      reino: z.string(),
      division: z.string(),
      clase: z.string(),
      familia: z.string(),
      genero: z.string(),
    }),

    etnobotanica: z.object({
      clasificacion: z.string(),
      parteUtilizada: z.string(),
      usoTradicional: z.string(),
      compuestosQuimicos: z.array(z.string()),
    }),

    // analisisAcademico ya NO son strings largos en el frontmatter:
    // ese contenido ahora vive en el CUERPO del .md, bajo headings
    // ## Taxonomía / ## Etnobotánica / ## Fitoquímica / ## Sostenibilidad
    // (ver plantilla y ejemplos abajo).

    multimediaPrincipal: z.object({
      imagenUrl: z.string().url(),
      imagenPublicId: z.string(),
      videoUrl: z.string(),
      videoPublicId: z.string(),
      proveedor: z.string(),
    }),

    estado: z.string(),
  }),
});

const etnobotanica = defineCollection({
  // antes: pattern: '**/*.json'
  loader: glob({ pattern: '**/*.md', base: './src/content/etnobotanicacont' }),
  schema: z.object({
    nombre: z.string(),
    cientifico: z.string(),
    categoria: z.string(),
    parteUsada: z.string(),
    uso: z.string(),
    compuestos: z.string(),
    img: z.string().url(),
  }),
});

export const collections = { blog, species, etnobotanica };
```

> Nota: si algún día quieres que `imagenUrl`/`img` sean imágenes locales
> optimizadas por Astro (en vez de URLs de Cloudinary), cambiarías
> `z.string().url()` por el helper `image()` que provee el segundo argumento
> del `schema` (`schema: ({ image }) => z.object({...})`). Como ya usas
> Cloudinary como proveedor externo, no es necesario tocar esto ahora.

---

## 2. Estructura de carpetas y nombres de archivo

```
src/content/
├── blog/
├── species/
│   ├── naranja-dulce.md
│   ├── guayaba.md
│   ├── manzanilla.md
│   └── ...
└── etnobotanicacont/
    ├── naranja-dulce.md
    ├── guayaba.md
    └── ...
```

Reglas:

- Nombre de archivo en **kebab-case**, sin tildes ni espacios
  (`naranja-dulce.md`, no `Naranja Dulce.md`). Ese nombre es el `id` que
  usarás en rutas dinámicas (`/plantas/[slug]`).
- Un archivo `.md` por planta, en cada colección donde aplique (una planta
  puede existir en `species` y/o en `etnobotanicacont`, son colecciones
  independientes).
- Borra el `.json` equivalente después de crear el `.md` — no dejes ambos,
  Astro leería ambas entradas como registros separados si conviven.

---

## 3. Plantilla de `species/*.md`

```md
---
nombreComun: "Nombre común"
nombreCientifico: "Genus species"
nombresAlternativos: ["alias 1", "alias 2"]
taxonomia:
  reino: "Plantae"
  division: "Magnoliophyta"
  clase: "Magnoliopsida"
  familia: "Familia"
  genero: "Genero"
etnobotanica:
  clasificacion: "Alimenticia - Medicinal"
  parteUtilizada: "Parte usada"
  usoTradicional: "Resumen corto del uso tradicional"
  compuestosQuimicos: ["Compuesto 1", "Compuesto 2"]
multimediaPrincipal:
  imagenUrl: "https://res.cloudinary.com/tu-cuenta/image/upload/v.../slug.jpg"
  imagenPublicId: "slug"
  videoUrl: ""
  videoPublicId: ""
  proveedor: "cloudinary"
estado: "publicado"
---

## Introducción

Texto de introducción de la ficha.

## Taxonomía

Texto de la sección de taxonomía / historia y evolución botánica.

## Etnobotánica

Texto del perfil etnobotánico.

## Fitoquímica

Texto de características químicas principales.

## Sostenibilidad

Comercio, importación/exportación, o cualquier nota de sostenibilidad.
```

En tu página de detalle (`[slug].astro`) usas `render(entry)` para pintar
todo el cuerpo de una, o `entry.body` + un remark plugin si en algún momento
quieres extraer secciones individuales por heading. Para empezar, lo más
simple es renderizar el cuerpo completo bajo un solo bloque "Análisis
académico".

---

## 4. Plantilla de `etnobotanicacont/*.md`

```md
---
nombre: "Nombre común"
cientifico: "Genus species"
categoria: "Medicinal / Alimenticia / etc."
parteUsada: "Parte usada"
uso: "Descripción del uso principal"
compuestos: "Lista o descripción de compuestos activos"
img: "https://res.cloudinary.com/tu-cuenta/image/upload/v.../slug.jpg"
---

Texto libre opcional con más contexto etnobotánico, si algún día lo necesitas
para esta planta (puede quedar vacío).
```

---

## 5. Ejemplos completos con datos de tu ficha técnica

### `species/naranja-dulce.md`

```md
---
nombreComun: "Naranja dulce / Naranjo"
nombreCientifico: "Citrus sinensis"
nombresAlternativos: ["Naranjo"]
taxonomia:
  reino: "Plantae"
  division: "Magnoliophyta"
  clase: "Magnoliopsida"
  familia: "Rutaceae"
  genero: "Citrus"
etnobotanica:
  clasificacion: "Alimenticia - Medicinal"
  parteUtilizada: "Fruto (pulpa, jugo) y cáscara (epicarpio / flavedo)"
  usoTradicional: "Antioxidante, digestiva, aromatizante y fitoterapéutica"
  compuestosQuimicos: ["Ácido ascórbico", "Ácido cítrico", "Hesperidina", "Narirutina", "D-limoneno", "Betacaroteno"]
multimediaPrincipal:
  imagenUrl: "https://res.cloudinary.com/tu-cuenta/image/upload/v.../naranja-dulce.jpg"
  imagenPublicId: "naranja-dulce"
  videoUrl: ""
  videoPublicId: ""
  proveedor: "cloudinary"
estado: "publicado"
---

## Introducción

El naranjo dulce es uno de los cultivos frutales más emblemáticos y
consumidos a nivel global, y representa un puente entre la biodiversidad
agrícola y el bienestar humano. Su matriz biológica aporta vitamina C,
mientras que su cáscara concentra compuestos de alto valor industrial,
farmacéutico y medicinal.

## Taxonomía

Nativa del sudeste asiático (sur de China, noreste de India y Birmania),
llegó al Mediterráneo por rutas comerciales y fue introducida en América
por los colonizadores españoles durante el siglo XVI. Estudios genéticos
modernos confirman que es un híbrido antiguo entre el pomelo (*Citrus
maxima*) y la mandarina (*Citrus reticulata*).

## Etnobotánica

Se aprovecha tanto la pulpa y el jugo del fruto como los aceites de la
cáscara, con propiedades antioxidantes, digestivas, aromatizantes y usos
fitoterapéuticos.

## Fitoquímica

Domina el ácido ascórbico (vitamina C) junto al ácido cítrico. Los
flavonoides hesperidina y narirutina aportan efectos antioxidantes,
antiinflamatorios y cardioprotectores. El flavedo es rico en D-limoneno
(supera el 90% del aceite volátil), con propiedades antimicrobianas. Los
carotenoides (betacaroteno, luteína, criptoxantina) son precursores de
vitamina A.

## Sostenibilidad

Egipto es el principal exportador mundial de naranja fresca; Sudáfrica
lidera el hemisferio sur; España destaca en cítricos premium; Brasil,
como mayor productor, redirige la mayoría de su cosecha a jugo concentrado
congelado (FCOJ). La UE, EE.UU., China, Canadá y Reino Unido concentran la
importación.
```

### `species/guayaba.md`

```md
---
nombreComun: "Guayaba / Guayabo"
nombreCientifico: "Psidium guajava"
nombresAlternativos: []
taxonomia:
  reino: "Plantae"
  division: "Magnoliophyta"
  clase: "Magnoliopsida"
  familia: "Myrtaceae"
  genero: "Psidium"
etnobotanica:
  clasificacion: "Medicinal tradicional"
  parteUtilizada: "Hojas y fruto"
  usoTradicional: "Antidiarreica, antimicrobiana, espasmolítica y astringente"
  compuestosQuimicos: ["Taninos", "Ácido ascórbico"]
multimediaPrincipal:
  imagenUrl: "https://res.cloudinary.com/tu-cuenta/image/upload/v.../guayaba.jpg"
  imagenPublicId: "guayaba"
  videoUrl: ""
  videoPublicId: ""
  proveedor: "cloudinary"
estado: "publicado"
---

## Introducción

Una de las especies medicinales y alimenticias más valiosas del trópico.
Las infusiones de hoja están respaldadas en la farmacopea tradicional para
problemas gastrointestinales.

## Taxonomía

Nativa de zonas tropicales de América (desde México hasta Brasil),
dispersada por españoles y portugueses hacia Filipinas, India y África.

## Etnobotánica

El uso principal de las hojas es antidiarreico, disminuyendo el
peristaltismo intestinal. El fruto es un reconstituyente vitamínico
potente.

## Fitoquímica

Los taninos, concentrados en las hojas, son responsables de la acción
astringente y espasmolítica. El fruto contiene de 3 a 5 veces más
vitamina C que los cítricos comunes.

## Sostenibilidad

India y China lideran exportación de producto procesado; México y Brasil
lideran fruta fresca en Latinoamérica. Estados Unidos es el principal
importador, seguido por la UE y Medio Oriente.
```

### `species/manzanilla.md`

```md
---
nombreComun: "Manzanilla"
nombreCientifico: "Matricaria chamomilla"
nombresAlternativos: []
taxonomia:
  reino: "Plantae"
  division: "Magnoliophyta"
  clase: "Magnoliopsida"
  familia: "Asteraceae"
  genero: "Matricaria"
etnobotanica:
  clasificacion: "Medicinal aromática"
  parteUtilizada: "Flores"
  usoTradicional: "Digestiva"
  compuestosQuimicos: ["Apigenina", "Bisabolol", "Camazuleno", "Cumarinas"]
multimediaPrincipal:
  imagenUrl: "https://res.cloudinary.com/tu-cuenta/image/upload/v.../manzanilla.jpg"
  imagenPublicId: "manzanilla"
  videoUrl: ""
  videoPublicId: ""
  proveedor: "cloudinary"
estado: "publicado"
---

## Introducción

Una de las hierbas medicinales más consumidas del mundo en infusión. Sus
flores concentran aceites esenciales con efecto relajante muscular,
antiespasmódico y protector de la mucosa gástrica.

## Taxonomía

Originaria de Europa y el norte de Asia, usada desde el antiguo Egipto,
Grecia y Roma; se naturalizó en América tras la colonización europea.

## Etnobotánica

Las flores secas alivian trastornos digestivos (indigestión, cólicos,
flatulencia), actúan como sedante suave para el insomnio leve y de forma
tópica desinflaman piel y ojos.

## Fitoquímica

La apigenina aporta efectos sedantes y ansiolíticos suaves. El bisabolol
y el camazuleno (aceite esencial) tienen propiedades antiinflamatorias y
antimicrobianas; las cumarinas dan efecto antiespasmódico gástrico.

## Sostenibilidad

Egipto, México y Alemania son los principales productores/exportadores;
Argentina destaca en Sudamérica. Estados Unidos y la UE son los grandes
consumidores, tanto en la industria de té como en cosmética.
```

---

## 6. Pasos para migrar el resto de las 35 plantas de la ficha

1. Para cada planta de `fichas_tecnicas_final.docx`, crea un archivo
   `species/<slug>.md` con la tabla de parámetros (Nombre común, Nombre
   científico, Familia, Categoría, Partes utilizadas, Usos medicinales)
   mapeada al frontmatter, siguiendo la plantilla de la sección 3.
2. Copia las secciones "Historia y evolución botánica", "Perfil
   etnobotánico", "Características químicas principales" e "Importación y
   exportación" al cuerpo del `.md`, bajo los headings `## Taxonomía`,
   `## Etnobotánica`, `## Fitoquímica`, `## Sostenibilidad` respectivamente
   (puedes reescribirlas o resumirlas a tu gusto, no hace falta copiarlas
   literales).
3. Sube la imagen de cada planta a Cloudinary y pega la URL en
   `multimediaPrincipal.imagenUrl` (o en `img` si también la agregas a
   `etnobotanicacont`).
4. Repite el mismo archivo (con campos reducidos) en `etnobotanicacont/`
   solo si esa planta también debe aparecer en esa vista.
5. Borra los `.json` que reemplaces.
6. Corre `astro sync` (o simplemente levanta el dev server) para que Astro
   regenere los tipos de las colecciones a partir del nuevo schema.

---

## 7. Ejemplo de consumo en una página Astro

```astro
---
// src/pages/plantas/[slug].astro
import { getCollection, render } from 'astro:content';

export async function getStaticPaths() {
  const plantas = await getCollection('species');
  return plantas.map((entry) => ({
    params: { slug: entry.id },
    props: { entry },
  }));
}

const { entry } = Astro.props;
const { Content } = await render(entry);
---

<article>
  <h1>{entry.data.nombreComun}</h1>
  <p><em>{entry.data.nombreCientifico}</em> — {entry.data.taxonomia.familia}</p>
  <img src={entry.data.multimediaPrincipal.imagenUrl} alt={entry.data.nombreComun} />

  <Content />
</article>
```

`Content` renderiza automáticamente el cuerpo Markdown (los `##`
Taxonomía/Etnobotánica/Fitoquímica/Sostenibilidad) como HTML, sin que
tengas que tocar nada más.
