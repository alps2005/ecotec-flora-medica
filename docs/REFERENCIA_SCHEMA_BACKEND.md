# Referencia rápida — Esquema real del Backend (módulo Plantas)

> Extracto reducido de `DOCUMENTACION_FRONTEND.md` (equipo Backend, junio 2026).
> Solo incluye lo necesario para el catálogo de especies. No contiene auth,
> noticias, multimedia, suscriptores ni el resto de módulos del backend.

## 1. Endpoints relevantes

```
GET /api/plantas
  → Lista reducida. Solo devuelve plantas con estado: "ACTIVO".
  → Query param opcional: ?familia=Asteraceae (filtra por taxonomia.familia)
  → Campos devueltos: slug, nombreComun, nombreCientifico,
    taxonomia.familia, multimediaPrincipal.imagenUrl

GET /api/plantas/:slug
  → Detalle completo de una planta (ver esquema abajo).
```

## 2. Esquema completo de una "Planta" (detalle)

```json
{
  "slug": "romero",
  "nombreComun": "Romero",
  "nombreCientifico": "Salvia rosmarinus",
  "nombresAlternativos": ["Rosmarinus officinalis"],
  "taxonomia": {
    "reino": "Plantae",
    "division": "Magnoliophyta",
    "clase": "Magnoliopsida",
    "familia": "Lamiaceae",
    "genero": "Salvia"
  },
  "etnobotanica": {
    "clasificacion": "Medicinal aromática",
    "parteUtilizada": "Hojas",
    "usoTradicional": "Estimulante circulatorio y cognitivo, tónico digestivo",
    "compuestosQuimicos": ["Ácido carnósico", "Carnosol", "Ácido rosmarínico", "1,8-cineol"]
  },
  "analisisAcademico": {
    "taxonomia": "Texto descriptivo...",
    "etnobotanica": "Texto descriptivo...",
    "fitoquimica": "Texto descriptivo...",
    "sostenibilidad": "Texto descriptivo..."
  },
  "multimediaPrincipal": {
    "imagenUrl": "https://...",
    "imagenPublicId": "",
    "videoUrl": "",
    "videoPublicId": "",
    "proveedor": "CLOUDINARY"
  },
  "estado": "ACTIVO",
  "fechaRegistro": "2024-01-15T10:00:00.000Z",
  "fechaActualizacion": "2024-01-15T10:00:00.000Z"
}
```

## 3. Tabla de equivalencia (schema viejo → nuevo)

| Campo actual (viejo) | Campo real del backend | Notas |
|---|---|---|
| `commonName` | `nombreComun` | |
| `scientificName` | `nombreCientifico` | |
| `family` (plano) | `taxonomia.familia` | ahora anidado dentro de `taxonomia` |
| — | `taxonomia.reino`, `.division`, `.clase`, `.genero` | nuevos, no existían antes |
| `description` | `etnobotanica.usoTradicional` | el más parecido conceptualmente |
| — | `etnobotanica.clasificacion`, `.parteUtilizada`, `.compuestosQuimicos` | nuevos |
| — | `analisisAcademico.{taxonomia, etnobotanica, fitoquimica, sostenibilidad}` | nuevo bloque de 4 textos largos |
| `image` (string suelto) | `multimediaPrincipal.imagenUrl` | anidado, con más campos (puede quedar el resto vacío/placeholder) |
| — | `slug` | necesario para la página de detalle (`/especies/[slug]`) |
| — | `estado` | usar siempre `"ACTIVO"` en los datos quemados |

## 4. Contenido resumido para las 4 especies actuales

> Fuente: fichas técnicas del equipo de documentación (`fichas_tecnicas_final.docx`),
> resumidas y adaptadas al esquema de arriba. **Romero, Manzanilla y Menta
> Piperita tienen ficha exacta en el documento**. Naranjo Amargo NO tiene
> ficha exacta (el documento solo cubre Naranja Dulce / Citrus sinensis, una
> especie distinta) — su contenido abajo es una adaptación razonable basada
> en la misma familia (Rutaceae), no una fuente verificada como las otras 3.

### Romero (`romero`)
- `nombreCientifico`: Salvia rosmarinus
- `taxonomia.familia`: Lamiaceae | `genero`: Salvia
- `etnobotanica.clasificacion`: Medicinal aromática
- `etnobotanica.parteUtilizada`: Hojas
- `etnobotanica.usoTradicional`: Estimulante del sistema circulatorio y cognitivo, tónico digestivo y antioxidante
- `etnobotanica.compuestosQuimicos`: ["Ácido carnósico", "Carnosol", "Ácido rosmarínico", "1,8-cineol"]

### Manzanilla (`manzanilla`)
- `nombreCientifico`: Matricaria chamomilla
- `taxonomia.familia`: Asteraceae | `genero`: Matricaria
- `etnobotanica.clasificacion`: Medicinal aromática
- `etnobotanica.parteUtilizada`: Flores
- `etnobotanica.usoTradicional`: Digestiva, antiespasmódica, sedante suave y antiinflamatoria tópica
- `etnobotanica.compuestosQuimicos`: ["Apigenina", "Bisabolol", "Camazuleno", "Cumarinas"]

### Menta Piperita (`menta-piperita`)
- `nombreCientifico`: Mentha piperita
- `taxonomia.familia`: Lamiaceae | `genero`: Mentha
- `etnobotanica.clasificacion`: Medicinal aromática
- `etnobotanica.parteUtilizada`: Hojas
- `etnobotanica.usoTradicional`: Digestiva, espasmolítica, carminativa y refrescante de vías respiratorias
- `etnobotanica.compuestosQuimicos`: ["Mentol", "Mentona", "Ácido rosmarínico", "Luteolina"]

### Naranjo Amargo (`naranjo-amargo`) — *adaptado, no verificado en fuente*
- `nombreCientifico`: Citrus aurantium
- `taxonomia.familia`: Rutaceae | `genero`: Citrus
- `etnobotanica.clasificacion`: Alimenticia - Medicinal
- `etnobotanica.parteUtilizada`: Flores (azahar) y fruto
- `etnobotanica.usoTradicional`: Sedante suave y digestivo, flores destiladas en fitoterapia
- `etnobotanica.compuestosQuimicos`: ["Hesperidina", "D-limoneno", "Ácido cítrico"]
