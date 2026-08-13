# Tarea de Actualización de Documentación

Tu objetivo **ya no es generar documentación desde cero** — este repositorio ya tiene una documentación completa en `docs/`. Tu objetivo es **auditarla contra el estado actual del código y del historial de Git, y actualizar únicamente lo que esté desactualizado, incompleto o sea incorrecto**, preservando todo lo que siga siendo válido.

Tienes acceso a:

- el código fuente completo
- todas las carpetas
- todos los archivos de configuración
- cada commit del historial de Git
- cada rama fusionada en `main`
- `package.json`
- la configuración de Astro
- la configuración de Tailwind
- los assets
- el contenido Markdown
- los componentes
- los layouts
- **los archivos ya existentes en `docs/`** (ver lista abajo) — son tu punto de partida, no un borrador a descartar

---

## Documentación existente en `docs/`

| Archivo | Contenido |
|---|---|
| `PROJECT_DOCUMENTATION.md` | Resumen ejecutivo, propósito, tecnologías, funcionalidades, estado actual |
| `ARCHITECTURE.md` | Estructura de carpetas, enrutamiento, arquitectura de Astro, componentes, flujo de datos |
| `DESIGN_SYSTEM.md` | Paleta de colores, tipografía, espaciado, componentes shadcn/ui, patrones de botones/tarjetas |
| `CONTENT_STRUCTURE.md` | Esquema de la colección `species`, catálogo de especies, datos derivados (etnobotánica, comercio) |
| `FRONTEND_INTEGRACION_BACKEND.md` | Contrato frontend-backend, estado de integración por pantalla, servicios HTTP |
| `api_usage.md` | Manual de la API del backend: autenticación JWT, endpoints, hallazgos de pruebas |
| `DEPLOYMENT.md` | Proceso de build, variables de entorno, alojamiento, CI/CD, optimización |
| `DEVELOPMENT_HISTORY.md` | Narrativa cronológica por fases, derivada del historial de Git, con diagrama Gantt |
| `CHANGELOG.md` | Registro de cambios commit por commit, agrupado por fecha |
| `DIAGRAMS.md` | Diagramas Mermaid de arquitectura, flujo de datos y autenticación JWT, listos para insertar en el reporte del proyecto |

---

## Método de actualización

No reescribas un archivo completo si solo una parte cambió. Sigue este proceso por cada archivo de la tabla anterior:

1. **Ubica la última vez que el archivo fue actualizado.** Usa `git log -- docs/<archivo>.md` para encontrar el commit más reciente que lo tocó.
2. **Compara ese punto con el estado actual.** Usa `git diff <ese-commit>..HEAD -- src/ package.json astro.config.mjs mobile/` (o el alcance relevante al archivo) para ver qué cambió en el código desde entonces.
3. **Decide si el cambio afecta al documento:**
   - Cambios de sintaxis/refactor sin efecto observable (p. ej. migración de valores arbitrarios de Tailwind a utilidades nativas, renombres internos) → actualiza solo las referencias de código puntuales (nombres de clases, rutas de archivo) que quedaron desactualizadas.
   - Contenido nuevo del mismo tipo (p. ej. una especie más, un commit más) → agrega la entrada, no reescribas la sección.
   - Cambios estructurales (nueva página, nuevo componente, nuevo endpoint, nueva dependencia, cambio de esquema) → actualiza la sección correspondiente y, si aplica, agrega una entrada nueva en `CHANGELOG.md` y una fase/commit nuevo en `DEVELOPMENT_HISTORY.md`.
   - Sin cambios relevantes → deja el archivo intacto.
4. **Verifica antes de afirmar.** Antes de dar por buena una referencia de código existente en la documentación (una clase CSS, un nombre de función, una ruta, un conteo de archivos), confírmala con `grep`/`find` contra el código actual — no asumas que sigue vigente solo porque el documento lo dice.

---

## Estilo de la documentación

Usa escritura técnica profesional, en español, coherente con el tono ya establecido en los archivos existentes.

Explica las decisiones, no solo el qué.

Incluye diagramas Mermaid cuando sea apropiado (ver `DIAGRAMS.md` como referencia de formato).

Genera o actualiza tablas cuando sean útiles.

Usa solo Markdown.

Cuando agregues una entrada nueva (a `CHANGELOG.md`, `DEVELOPMENT_HISTORY.md`, o una nota de actualización dentro de otro documento):
- Cita el hash de commit abreviado y la fecha.
- Si el cambio es puramente sintáctico/cosmético, dilo explícitamente para que quede claro que no hay impacto funcional.

Si no puedes encontrar información:

- infiérela desde el repositorio
- márcala como **[inferido]**
- nunca inventes hechos imposibles

La documentación final debe seguir siendo lo suficientemente detallada para que un nuevo desarrollador entienda el proyecto —y sus cambios más recientes— sin hablar con sus autores originales.
