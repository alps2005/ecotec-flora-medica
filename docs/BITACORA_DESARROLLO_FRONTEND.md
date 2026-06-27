# Bitácora de Desarrollo Frontend

## Información general

- Proyecto: Ecotec Flora Médica
- Área de trabajo: Frontend
- Módulo desarrollado: Catálogo de especies
- Tecnologías utilizadas: Astro 6, Tailwind CSS v4, Astro Content Collections, JavaScript vanilla y GSAP para animaciones puntuales en elementos compartidos del sitio

## Objetivo de la semana

El objetivo principal de la semana fue consolidar el módulo de catálogo de especies como una vista editorial, navegable y preparada para la futura integración con un backend real. La implementación buscó cubrir la estructura inicial del catálogo, la gestión de contenido local mediante colecciones, la migración del modelo de datos al contrato definido por Backend y la incorporación de una vista de detalle para cada especie sin alterar el enfoque visual del proyecto.

## Desarrollo cronológico

### Iteración 1 – Implementación inicial del catálogo

La primera etapa del trabajo se centró en construir la base del catálogo de especies como una experiencia independiente del resto del sitio. Se definió la estructura general de la página principal del módulo, se crearon componentes reutilizables para la tarjeta, el buscador, el filtro por familia y la paginación visual, y se configuró una colección de contenido para gestionar especies mediante archivos JSON locales.

Durante esta iteración también se incorporaron datos de ejemplo para cuatro especies del proyecto: Manzanilla, Menta Piperita, Naranjo Amargo y Romero. La información se organizó para facilitar una sustitución futura por una API, manteniendo la presentación del catálogo coherente con el diseño editorial propuesto.

Además, se implementó la lógica de búsqueda y filtrado por familia en el cliente mediante JavaScript vanilla, de manera que la navegación del catálogo resultara funcional sin introducir librerías adicionales. La paginación se dejó como una capa visual provisional, dada la cantidad limitada de especies de ejemplo.

### Iteración 2 – Migración al esquema del Backend

En una segunda iteración se llevó a cabo la migración del modelo de datos del catálogo hacia una estructura alineada con el contrato real definido por el equipo de Backend. La colección de especies se adaptó para trabajar con un esquema anidado que separa la información en bloques como taxonomía, etnobotánica, análisis académico y multimedia principal.

Se actualizaron los archivos JSON de las especies para ajustarlos al nuevo modelo, conservando el contenido esencial y reorganizando los campos según la estructura esperada por Backend. En paralelo, se modificaron las referencias en los componentes del catálogo para que consumieran los nuevos campos, manteniendo el comportamiento previo del módulo.

Esta etapa fue clave para asegurar la compatibilidad con el flujo futuro de consumo de datos desde una API, sin introducir cambios visuales significativos en la experiencia ya implementada.

### Iteración 3 – Nuevas funcionalidades

La tercera iteración incorporó la página de detalle de cada especie como una ruta dinámica. Se implementó la vista de detalle mediante generación estática de páginas a partir del slug de cada entrada de la colección, permitiendo acceder a una ficha completa de la especie desde el catálogo.

En esta vista se incluyeron los elementos principales del contenido académico y etnobotánico: encabezado con nombre común y científico, badge de familia, línea taxonómica completa, información básica de etnobotánica, listado de compuestos químicos y cuatro bloques de análisis académico. Además, se integró la imagen principal como elemento visual central de la ficha.

Paralelamente, las tarjetas del catálogo se enlazaron a sus respectivas páginas de detalle, de modo que la navegación entre listado y ficha quedó completamente habilitada. También se ajustó el contenedor del catálogo para mejorar el ancho de la página y se incorporó un estilo propio al selector de familias para que el control nativo del navegador se integrara visualmente con el sistema de diseño.

### Iteración 4 – Correcciones finales

En la última etapa del trabajo se aplicaron ajustes de detalle para mejorar la experiencia visual y el rendimiento percibido. Se corrigió la transición de hover de las tarjetas del catálogo para limitar los cambios animados a las propiedades realmente afectadas, evitando que el desplazamiento vertical de la página se sintiera pesado al pasar el cursor sobre las tarjetas.

Asimismo, se ajustó la sección de compuestos químicos en la página de detalle para que el contenido se visualizara de forma más equilibrada y visualmente destacada, mejorando la jerarquía de la tarjeta y la legibilidad de los chips.

## Archivos creados

- src/components/species/Pagination.astro
- src/components/species/SearchBar.astro
- src/components/species/SpeciesCard.astro
- src/components/species/SpeciesGrid.astro
- src/components/species/TaxonomyFilter.astro
- src/content/species/manzanilla.json
- src/content/species/menta-piperita.json
- src/content/species/naranjo-amargo.json
- src/content/species/romero.json
- src/pages/especies.astro
- src/pages/especies/[slug].astro

## Archivos modificados

- src/content.config.ts
- src/components/species/SpeciesCard.astro
- src/components/species/SpeciesGrid.astro
- src/components/species/SearchBar.astro
- src/components/species/TaxonomyFilter.astro
- src/pages/especies.astro

## Decisiones técnicas

- Se optó por Astro Content Collections como mecanismo principal para gestionar el contenido local del módulo.
- Se trabajó con archivos JSON para representar las especies de forma simple y fácil de reemplazar por datos reales en una etapa posterior.
- Se adoptó un esquema de datos anidado para reflejar el contrato del backend, especialmente en los bloques de taxonomía, etnobotánica y multimedia.
- La lógica de color por familia se mantuvo local al módulo de especies, reutilizando una paleta estable y coherente con el diseño del proyecto.
- El filtrado del catálogo se implementó con JavaScript vanilla y reactivación en eventos de carga de página de Astro, preservando la simplicidad del stack.
- La página de detalle se generó de forma estática mediante getStaticPaths, compatible con la estructura actual del proyecto.

## Problemas encontrados

- El modelo inicial del catálogo estaba basado en un esquema plano y no coincidía con la estructura real esperada por Backend, lo que requirió una migración de datos y referencias en varios puntos del módulo.
- La implementación inicial del detalle necesitó adaptarse a un esquema más rico de información sin perder la claridad visual de la interfaz.
- Se identificó que ciertas transiciones de hover podían afectar la experiencia de scroll en la vista del catálogo, por lo que se limitó la animación a las propiedades realmente necesarias.
- La sección de compuestos químicos requería un tratamiento visual más sólido para que su contenido no se perdiera dentro del espacio disponible de la tarjeta.

## Validaciones realizadas

- Se validó la compilación del proyecto mediante la ejecución de pnpm build.
- La compilación final generó correctamente las rutas del catálogo y las cuatro páginas de detalle asociadas a las especies incluidas en el contenido local.
- Se comprobó la integración básica de búsqueda, filtro por familia, navegación al detalle y renderizado de la estructura de datos migrada.

## Resultado alcanzado

Al cierre de la semana, el módulo de catálogo de especies quedó implementado como una vista editorial funcional, con contenido local estructurado, filtrado y navegación a detalle. El catálogo y las fichas de especie utilizan un modelo de datos alineado con el contrato del backend, lo que deja el módulo preparado para una futura sustitución de contenido por una fuente remota sin reestructurar la experiencia de usuario.

## Trabajo pendiente

- Integración real del módulo con la API del equipo de Backend.
- Sustitución del contenido local quemado por datos dinámicos y persistidos en un servicio externo.
- Ampliación del contenido de especies y enriquecimiento del detalle editorial según criterios de producto y contenido.
