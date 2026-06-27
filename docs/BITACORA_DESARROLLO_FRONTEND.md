# Bitácora de Desarrollo Frontend

## Información general

- Proyecto: Ecotec Flora Médica
- Área de trabajo: Frontend
- Módulo desarrollado: Catálogo de especies
- Tecnologías utilizadas: Astro 6, Tailwind CSS v4, Astro Content Collections, JavaScript vanilla y GSAP para animaciones puntuales en elementos compartidos del sitio

## Objetivo de la semana

El propósito del trabajo realizado durante esta semana fue implementar y consolidar el módulo de catálogo de especies como una experiencia editorial funcional, con contenido local estructurado, navegación entre listado y detalle, y una base de datos compatible con el contrato de Backend para una futura integración con una API real.

## Desarrollo cronológico

### Iteración 1 – Implementación inicial del catálogo

La primera etapa del trabajo se centró en construir la base del catálogo como una vista independiente del resto del sitio. Se definió la estructura general de la página principal del módulo, se crearon componentes reutilizables para la tarjeta, el buscador, el filtro por familia y la paginación visual, y se configuró una colección de contenido para gestionar especies mediante archivos JSON locales.

Durante esta iteración también se incorporaron datos de ejemplo para cuatro especies del proyecto: Manzanilla, Menta Piperita, Naranjo Amargo y Romero. La información se organizó para facilitar una sustitución futura por una API, manteniendo una presentación coherente con el diseño editorial propuesto.

Además, se implementó la lógica de búsqueda y filtrado por familia en el cliente mediante JavaScript vanilla, sin introducir frameworks adicionales ni modificar componentes compartidos fuera del módulo. La paginación se dejó como una capa visual provisional, dada la cantidad limitada de especies de ejemplo.

### Iteración 2 – Migración al esquema del Backend

En una segunda iteración se llevó a cabo la migración del modelo de datos del catálogo hacia una estructura alineada con el contrato real del equipo de Backend. La colección de especies se adaptó para trabajar con un esquema anidado que separa la información en bloques como taxonomía, etnobotánica, análisis académico y multimedia principal.

Se actualizaron los archivos JSON de las especies para ajustarlos al nuevo modelo, conservando el contenido esencial y reorganizando los campos según la estructura esperada por Backend. En paralelo, se modificaron las referencias en los componentes del catálogo para que consumieran los nuevos campos sin alterar el diseño visual ni la lógica de interacción ya implementada.

Esta etapa fue clave para preparar el módulo para una futura integración con una API, manteniendo la compatibilidad con un esquema más rico y estructurado que el inicial.

### Iteración 3 – Nuevas funcionalidades

La tercera iteración incorporó la página de detalle de cada especie como una ruta dinámica. Se implementó la vista de detalle mediante generación estática de páginas a partir del slug de cada entrada de la colección, permitiendo acceder a una ficha completa de la especie desde el catálogo.

En esta vista se incluyeron los elementos principales del contenido académico y etnobotánico: encabezado con nombre común y científico, badge de familia, línea taxonómica completa, información básica de etnobotánica, listado de compuestos químicos y cuatro bloques de análisis académico. Además, se integró la imagen principal como elemento visual central de la ficha.

Paralelamente, las tarjetas del catálogo se enlazaron a sus respectivas páginas de detalle, de modo que la navegación entre listado y ficha quedó habilitada. También se ajustó el contenedor del catálogo para mejorar el ancho de la página y se incorporó un estilo propio al selector de familias para que el control nativo del navegador se integrara visualmente con el sistema de diseño.

### Iteración 4 – Correcciones finales

En la última etapa del trabajo se aplicaron ajustes de detalle para mejorar la experiencia visual y el rendimiento percibido. Se corrigió la transición de hover de las tarjetas del catálogo para limitar las propiedades animadas a las que realmente cambiaban, evitando que el scroll se sintiera pesado al pasar el cursor sobre las tarjetas.

Asimismo, se ajustó la sección de compuestos químicos en la página de detalle para que el contenido se visualizara de forma más equilibrada y visualmente destacada, mejorando la jerarquía de la tarjeta y la legibilidad de los chips.

## Archivos creados

- [src/components/species/Pagination.astro](src/components/species/Pagination.astro) — componente visual de paginación del catálogo.
- [src/components/species/SearchBar.astro](src/components/species/SearchBar.astro) — buscador de especies del módulo.
- [src/components/species/SpeciesCard.astro](src/components/species/SpeciesCard.astro) — tarjeta reutilizable para mostrar cada especie en el listado.
- [src/components/species/SpeciesGrid.astro](src/components/species/SpeciesGrid.astro) — contenedor del listado y lógica de renderizado de las tarjetas.
- [src/components/species/TaxonomyFilter.astro](src/components/species/TaxonomyFilter.astro) — filtro por familia taxonómica.
- [src/content/species/manzanilla.json](src/content/species/manzanilla.json) — contenido local de ejemplo para la especie Manzanilla.
- [src/content/species/menta-piperita.json](src/content/species/menta-piperita.json) — contenido local de ejemplo para la especie Menta Piperita.
- [src/content/species/naranjo-amargo.json](src/content/species/naranjo-amargo.json) — contenido local de ejemplo para la especie Naranjo Amargo.
- [src/content/species/romero.json](src/content/species/romero.json) — contenido local de ejemplo para la especie Romero.
- [src/pages/especies.astro](src/pages/especies.astro) — página principal del catálogo de especies.
- [src/pages/especies/[slug].astro](src/pages/especies/[slug].astro) — página dinámica de detalle para cada especie.

## Archivos modificados

- [src/content.config.ts](src/content.config.ts) — adaptación del esquema de la colección de especies al modelo anidado del backend.
- [src/components/species/SpeciesCard.astro](src/components/species/SpeciesCard.astro) — ajuste de la tarjeta para soportar navegación hacia el detalle y mantener el diseño existente.
- [src/components/species/SpeciesGrid.astro](src/components/species/SpeciesGrid.astro) — adaptación para leer el nuevo modelo de datos y aplicar la lógica de acento por familia.
- [src/components/species/SearchBar.astro](src/components/species/SearchBar.astro) — adaptación del componente para trabajar con la estructura de datos migrada.
- [src/components/species/TaxonomyFilter.astro](src/components/species/TaxonomyFilter.astro) — ajuste del selector de familias al diseño del catálogo.
- [src/pages/especies.astro](src/pages/especies.astro) — integración de la vista del catálogo, el filtrado y la estructura de presentación del módulo.

## Decisiones técnicas

- Se optó por Astro Content Collections como mecanismo principal para gestionar el contenido local del módulo, manteniendo la lógica del catálogo separada del resto del proyecto.
- Se trabajó con archivos JSON para representar las especies de forma simple y fácil de reemplazar por datos reales en una etapa posterior.
- Se adoptó un esquema de datos anidado para reflejar el contrato del backend, especialmente en los bloques de taxonomía, etnobotánica, análisis académico y multimedia principal.
- La lógica de color por familia se mantuvo encapsulada dentro del módulo de especies para no afectar a componentes compartidos del proyecto.
- Se reutilizaron componentes específicos del catálogo para mantener una estructura consistente y facilitar el mantenimiento del módulo.
- El filtrado del catálogo se implementó con JavaScript vanilla y reactivación en eventos de carga de página de Astro, preservando la simplicidad del stack y evitando la incorporación de frameworks adicionales.
- La página de detalle se generó de forma estática mediante getStaticPaths, compatible con la estructura actual del proyecto y con la navegación esperada por el backend.
- Se mantuvo el diseño editorial del módulo durante la migración del esquema, priorizando la compatibilidad visual y la continuidad de la experiencia.

## Problemas encontrados

- El modelo inicial del catálogo estaba basado en un esquema plano y no coincidía con la estructura esperada por Backend, lo que requirió una migración de datos y referencias en varios puntos del módulo.
- La adaptación de los componentes existentes a un modelo anidado implicó actualizar rutas de datos y asegurar que la interfaz siguiera mostrando la misma información sin alterar el diseño ya implementado.
- La implementación de la página de detalle exigió un tratamiento más completo de la información, especialmente en los bloques de análisis académico y la visualización de compuestos químicos, sin romper la estructura general del catálogo.
- Se identificó que ciertas transiciones de hover podían afectar la experiencia de scroll en la vista del catálogo, por lo que se limitó la animación a las propiedades realmente necesarias para mantener un comportamiento más fluido.
- La preparación del módulo para una futura sustitución por una API implicó mantener una separación clara entre contenido local, presentación y lógica de interacción, de forma que el reemplazo posterior no requiriera reescribir la experiencia completa.

## Validaciones realizadas

- Se verificó la compilación del proyecto mediante la ejecución de pnpm build.
- La compilación generó correctamente las rutas del catálogo y de las cuatro páginas de detalle asociadas a las especies incluidas en el contenido local.
- Se comprobó que la estructura del módulo quedó integrada en el flujo de renderizado de Astro y que la implementación del catálogo, la búsqueda, el filtro y la navegación a detalle estaban presentes en el código compilado.

## Alcance del desarrollo

El trabajo desarrollado se limitó al módulo de catálogo de especies y a sus componentes asociados, respetando la división de responsabilidades del equipo y evitando modificar componentes compartidos del proyecto fuera del alcance definido. La implementación se concentró en la experiencia del catálogo, la colección de contenido y la página de detalle, dejando para etapas posteriores la integración real con la API del equipo de Backend.

## Resultado alcanzado

Al cierre de la semana, el módulo de catálogo de especies quedó implementado como una vista editorial funcional, con contenido local estructurado, filtrado, navegación a detalle y una base de datos compatible con el esquema del backend. El catálogo y las fichas de especie quedaron listos para una futura sustitución del contenido local por datos remotos sin reestructurar la experiencia de usuario.

## Trabajo pendiente

- Integración real del módulo con la API del equipo de Backend.
- Sustitución del contenido local quemado por datos dinámicos y persistidos en un servicio externo.
- Ampliación del contenido de especies y enriquecimiento del detalle editorial según criterios de producto y contenido.
