# Diagramas para el Reporte — Ecotec Flora Médica

Diagramas en código Mermaid listos para insertar en el documento (sección 6 "Arquitectura del Sistema" y sección 13.2 "Seguridad"). Generados a partir de `docs/ARCHITECTURE.md`, `docs/FRONTEND_INTEGRACION_BACKEND.md`, `docs/DEPLOYMENT.md` y `docs/api_usage.md`.

---

## 1. Arquitectura general por capas

```mermaid
flowchart TB
    subgraph PRES["Capa de Presentación"]
        HTML["HTML estático (SSG)"]
        REACTISLA["Isla React\nTradeExplorer.tsx"]
    end

    subgraph COMP["Capa de Componentes"]
        ASTROC["Componentes Astro\n(.astro)"]
        UIC["Componentes shadcn/ui\n(React, solo panel de comercio)"]
    end

    subgraph LOGICA["Capa de Lógica de Negocio"]
        SRC["species-source.ts\nmerge API + Markdown"]
        TRADE["trade-data.ts\ncruce con UN Comtrade"]
        SUS["suscriptores.ts"]
        SCRIPTS["Scripts de cliente\n(filtrado, paginación, GSAP)"]
    end

    subgraph DATOS["Capa de Acceso a Datos"]
        API["api.ts\ncliente HTTP (get/post)"]
        CC["Content Collections\n(Zod) — src/content/species"]
        JSON["trade-data.json"]
    end

    subgraph EXT["Servicios Externos"]
        BACKEND["API Backend REST\n(Node/Express + MongoDB)"]
        COMTRADE["UN Comtrade\n(datos pre-procesados)"]
        FONTS["Google Fonts / CDN Tabler Icons"]
        IMG["Picsum / Unsplash\n(imágenes de especies)"]
    end

    PRES --> COMP --> LOGICA --> DATOS
    SRC --> API --> BACKEND
    SUS --> API
    TRADE --> JSON
    SRC --> CC
    DATOS -.-> EXT
    REACTISLA --> UIC
```

---

## 2. Diagrama de flujo del sistema (petición del usuario)

```mermaid
flowchart LR
    U["Usuario"] --> REQ["Solicita una ruta\n(ej. /especies/achiote)"]
    REQ --> ROUTE["Enrutamiento de Astro\n(basado en archivos, SSG)"]
    ROUTE --> BUILDCHECK{"¿Página generada\nen build time?"}
    BUILDCHECK -->|Sí, HTML pre-renderizado| SERVE["Servidor estático\nentrega HTML/CSS/JS"]

    subgraph BUILD["Ocurrido antes, en tiempo de build"]
        CALL["species-source.ts llama a\nGET /api/plantas(/:slug)"]
        CALL --> OK{"¿Backend responde\na tiempo (5s)?"}
        OK -->|Sí| MERGE["Merge por campo:\nAPI + Markdown de respaldo"]
        OK -->|No / timeout / error| MD["Fallback total a\nMarkdown (.md)"]
        MERGE --> RENDER["Astro renderiza la página\na HTML estático"]
        MD --> RENDER
        RENDER --> DIST["/dist (HTML final)"]
    end

    DIST -.-> SERVE
    SERVE --> BROWSER["Navegador"]
    BROWSER --> HYDRATE["Hidrata la isla React\n(solo /importacion-exportacion)"]
    BROWSER --> CLIENTJS["Ejecuta scripts vanilla\n(búsqueda, filtros, paginación)"]
```

---

## 3. Arquitectura Frontend (Astro + islas de React)

```mermaid
flowchart TB
    subgraph ASTRO["Núcleo Astro (SSG)"]
        PAGES["src/pages/*.astro\nenrutamiento basado en archivos"]
        LAYOUT["Layout.astro\n(BaseHead + Header + Footer)"]
        COMPONENTS["Componentes .astro\n(species/, etnobotanicacomp/)"]
        SCRIPT["<script> vanilla TS\n(búsqueda, filtros, paginación, GSAP)"]
    end

    subgraph ISLAND["Isla de React (client:load)"]
        TE["TradeExplorer.tsx"]
        STATE["useState / useMemo\n(estado local, sin store global)"]
        RECHARTS["Recharts\n(gráfico export/import)"]
        SHADCN["shadcn/ui\n(select, card, table, tabs)"]
    end

    subgraph DATA["Capa de datos compartida"]
        LIB["src/lib/*.ts"]
    end

    PAGES --> LAYOUT
    PAGES --> COMPONENTS
    COMPONENTS --> SCRIPT
    PAGES -->|"solo importacion-exportacion.astro"| TE
    TE --> STATE --> RECHARTS
    TE --> SHADCN
    PAGES --> LIB
    TE --> LIB

    style ISLAND fill:#eef2f7,stroke:#0A5CA5
```

---

## 4. Arquitectura Backend — ciclo de vida de una petición

> Inferido de `docs/api_usage.md` (Node/Express + MongoDB/Mongoose, JWT). El backend es un repositorio independiente; este diagrama documenta el contrato observado, no el código fuente.

```mermaid
flowchart LR
    REQ["Petición HTTP\n/api/..."] --> MW1["Middleware global\n(CORS, JSON parser)"]
    MW1 --> ROUTER["Router de Express\n(prefijo /api)"]
    ROUTER --> AUTHCHECK{"¿Ruta protegida?"}
    AUTHCHECK -->|No| CONTROLLER
    AUTHCHECK -->|Sí| AUTHMW["authMiddleware\nvalida JWT (Bearer token)"]
    AUTHMW --> VALID{"¿Token válido?"}
    VALID -->|No| E401["401 Unauthorized"]
    VALID -->|Sí| ROLEMW["roleMiddleware\nverifica rol (SUPER_ADMIN/EDITOR)"]
    ROLEMW --> ROLEOK{"¿Rol suficiente?"}
    ROLEOK -->|No| E403["403 Forbidden"]
    ROLEOK -->|Sí| CONTROLLER["Controlador\n(valida body con express-validator)"]
    CONTROLLER --> SERVICE["Capa de servicio"]
    SERVICE --> MONGO["Mongoose / MongoDB\n(colecciones: plantas, usuarios_admin,\nsuscriptores, multimedia, noticias...)"]
    MONGO --> RESPONSE["Respuesta JSON"]
    RESPONSE --> CLIENT["Cliente\n(build de Astro o navegador)"]
```

---

## 5. Secuencia de integración Frontend–Backend

```mermaid
sequenceDiagram
    participant MD as Archivos Markdown (respaldo)
    participant SRC as species-source.ts
    participant BACKAPI as API Backend (/api/plantas)
    participant PAGE as Página Astro (build time)
    participant HTML as HTML Compilado
    participant USER as Navegador
    participant JS as Script del Cliente
    participant SUBAPI as API Backend (/api/suscriptores)

    PAGE->>SRC: getSpeciesList() / getSpeciesDetail(slug)
    SRC->>BACKAPI: GET /api/plantas(/:slug) — timeout 5s
    BACKAPI-->>SRC: JSON o error/timeout
    SRC->>MD: getCollection('species') — siempre como respaldo
    SRC-->>PAGE: Datos fusionados (source: 'api' | 'md')
    PAGE->>HTML: Render a HTML estático + atributos data-*
    HTML->>USER: Entrega vía HTTP
    USER->>JS: Ejecuta scripts inline/externos
    JS->>SUBAPI: POST /api/suscriptores (única llamada en runtime)
    SUBAPI-->>JS: Respuesta JSON
    JS-->>USER: Feedback de éxito/error
```

---

## 6. Flujo de autenticación y autorización JWT

```mermaid
sequenceDiagram
    participant C as Cliente (curl / admin panel)
    participant AUTH as POST /api/auth/login
    participant MW as authMiddleware
    participant ROLE as roleMiddleware
    participant EP as Endpoint protegido\n(ej. POST /api/plantas)
    participant DB as MongoDB

    C->>AUTH: { correo, password }
    AUTH->>DB: Verifica credenciales (bcrypt)
    DB-->>AUTH: Usuario + rol
    AUTH-->>C: { token } (JWT firmado, incluye rol)

    C->>EP: Petición con\nAuthorization: Bearer <token>
    EP->>MW: Valida firma y expiración del JWT
    alt Token inválido o ausente
        MW-->>C: 401 Unauthorized
    else Token válido
        MW->>ROLE: Verifica rol del payload
        alt Rol insuficiente (no SUPER_ADMIN/EDITOR)
            ROLE-->>C: 403 Forbidden
        else Rol autorizado
            ROLE->>EP: Continúa al controlador
            EP->>DB: Operación (crear/editar/borrar)
            DB-->>EP: Resultado
            EP-->>C: 200/201 + datos
        end
    end
```

---

## 7. Pipeline de build (referencia — ya documentado en `DEPLOYMENT.md`)

```mermaid
flowchart LR
    A[pnpm build] --> B[Carga astro.config.mjs]
    B --> C[Escanea src/content/species]
    C --> D[Valida frontmatter con Zod]
    D --> E{Validación OK?}
    E -->|No| F[Build falla]
    E -->|Sí| G[species-source.ts: API + fallback MD]
    G --> H[Genera páginas estáticas]
    H --> I[Optimiza imágenes]
    I --> J[Empaqueta Tailwind + shadcn/ui]
    J --> K[Empaqueta JS: GSAP + filtros + isla React]
    K --> L[Genera sitemap]
    L --> M[/dist]
```

---

### Notas de uso

- Los diagramas 1–4 y 6 son **nuevos** (no existían antes en `docs/`).
- Los diagramas 5 y 7 son versiones limpias/reordenadas de los que ya existen en `ARCHITECTURE.md` y `DEPLOYMENT.md` respectivamente — reutilízalos si prefieres mantener una sola fuente de verdad.
- El diagrama 4 (backend) es **inferido** a partir del comportamiento documentado en `api_usage.md` (no hay acceso al código fuente del backend en este repositorio); dejar la nota de "inferido" si se incluye en el reporte formal.
- Para pegarlos en Word/PDF: renderízalos con [mermaid.live](https://mermaid.live) o con un plugin de Mermaid en el editor que uses, y exporta como PNG/SVG.
