# Ecotec Flora Médica — App Android

App nativa Android (Kotlin + Jetpack Compose) que replica el sitio web de Ecotec Flora Médica
(Astro) para el proyecto de vinculación. **Fase 1**: todo el contenido vive local en el
dispositivo (no depende del backend, que hoy no está desplegado ni tiene URL pública estable).
Es un proyecto Gradle independiente dentro del monorepo — no comparte build ni dependencias con
el sitio web.

## Qué incluye

- Datos **reales** del sitio: se extrajeron las fichas de especies directamente de la content
  collection de Astro (`src/content/species`) y se empaquetaron en
  `app/src/main/assets/content.json` (41 especies + 1 entrada de blog heredada de una foto previa
  del contenido; el sitio web ya no tiene sección de blog). No es contenido de relleno.
- 6 pantallas con navegación por bottom bar (Inicio / Especies / Etnobotánica / Autores):
  - **Inicio** (`HomeScreen`)
  - **Catálogo de especies** (`EspeciesListScreen`) y **Detalle de especie** (`EspecieDetailScreen`)
  - **Etnobotánica** (`EtnobotanicaScreen`, derivada del mismo dataset de especies — igual que en
    el sitio web) y **Detalle etnobotánico** (`EtnobotanicaDetailScreen`), con una silueta corporal
    interactiva (`BodySilhouette.kt`) para explorar por zona del cuerpo
  - **Autores** (`AutoresScreen`)

  No hay pantallas de blog — el dataset trae una entrada heredada sin pantalla que la muestre.
- Paleta y tipografía propias de la app, inspiradas en el sitio (navy `#142347`, azul `#2454C9`,
  teal `#4FD1C5`, fuentes del sistema serif/sans) — **no** siguen la paleta azul/teal + Poppins
  actual del sitio web (renovada el 2026-08-01 en `docs/DESIGN_SYSTEM.md`); están desincronizadas
  a propósito por ahora, pendiente de una pasada de diseño dedicada a la app.
- Arquitectura con patrón Repository (`ContentRepository` interface + `LocalContentRepository`)
  para que pasar a una API remota en Fase 2 sea un cambio contenido.
- Imágenes cargadas con Coil directamente desde las URLs originales (ej. Wikipedia Commons,
  picsum.photos) — el **texto** funciona 100% offline, las **imágenes** necesitan algo de
  internet (cualquiera, no dependen del túnel/servidor propio).

## Requisitos

- Android Studio (Koala o más reciente recomendado)
- JDK 17 (el que trae embebido Android Studio sirve)
- Un emulador o dispositivo físico con Android 7.0 (API 24) o superior

## Cómo correrlo

1. Abre la carpeta `EcotecFloraMedicaApp` completa en Android Studio (`File → Open`).
2. Espera a que sincronice Gradle (la primera vez descarga dependencias, toma unos minutos).
3. Elige un emulador o conecta un dispositivo por USB con depuración habilitada.
4. Dale **Run ▶**.

No hace falta configurar nada más — no hay claves de API ni backend que levantar para esta fase.

## Estructura del proyecto

```
app/src/main/java/com/ecotec/floramedica/
  data/model/            Species, ContentBundle (mismo schema de especie que content.config.ts)
  data/repository/       ContentRepository (interfaz) + LocalContentRepository (JSON local)
  data/BodyMapping.kt    Mapeo de zonas del cuerpo → especies relacionadas (silueta interactiva)
  data/ImageUrl.kt        Helpers de resolución de URL de imagen
  navigation/             Routes + EcotecNavGraph (NavHost)
  ui/theme/               Color.kt, Type.kt, Theme.kt
  ui/components/          Pill, SpeciesCard, SpeciesImage, StatsBanner, BodySilhouette,
                          BodyRegionsDialog, Animations (reutilizables)
  ui/screens/             home/, especies/ (lista + detalle), etnobotanica/ (lista + detalle),
                          autores/
  MainActivity.kt         Scaffold (TopAppBar + bottom nav: Inicio/Especies/Etnobotánica/Autores)
app/src/main/assets/
  content.json            Dataset real (41 especies; una clave "blog" heredada se ignora)
```

## Fase 2 — conectar a la API real

Cuando el backend (`/srv/agronomia/backend`) esté desplegado con una URL estable:

1. Crear `data/repository/RemoteContentRepository.kt` implementando la misma interfaz
   `ContentRepository`, con Retrofit/Ktor apuntando a la API.
2. Cambiar una sola línea en `MainActivity.kt` (`LocalContentRepository(appContext)` →
   `RemoteContentRepository(...)`). Ninguna pantalla necesita tocarse porque todas dependen de
   la interfaz, no de la implementación.
3. Opcional: repositorio híbrido con cache local como fallback si no hay conexión.

## Notas / limitaciones honestas

- Este proyecto se generó en un entorno Linux sin Android SDK ni emulador, así que **no se
  compiló ni se corrió** acá — no hay garantía de que Gradle sincronice a la primera. Si hay
  errores de sync (versión de AGP/Kotlin no disponible en tu instalación, por ejemplo), son los
  típicos ajustes menores de versión, no problemas de arquitectura.
- La tipografía usa `FontFamily.Serif` / `FontFamily.SansSerif` (fuentes del sistema) como
  aproximación a la serif elegante del sitio. Si quieren la fuente exacta (parece una serif tipo
  Playfair Display/Lora), se agrega como Google Font descargable en `Type.kt`.
- El ícono de la app es un vector simple (hoja) generado a mano, no un diseño final — se puede
  reemplazar el `drawable/ic_launcher_foreground.xml` cuando tengan un logo real.
- Las secciones de detalle de especie (perfil etnobotánico, historia/evolución, comercio,
  compuestos) se armaron con el dataset real completo aunque solo vimos la primera sección
  ("01 / Análisis académico") en la captura — si el diseño real de esas secciones se ve distinto,
  mándame más capturas y ajusto layout sin tocar los datos.
