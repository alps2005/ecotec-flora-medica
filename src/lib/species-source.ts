/**
 * Fuente de datos única de especies (plantas).
 *
 * Toda página/componente debe leer especies desde aquí en lugar de llamar a
 * `getCollection('species')` directamente. Esta capa:
 *   1. Intenta consumir el backend (`GET /api/plantas` y `/api/plantas/:slug`).
 *   2. Hace un *merge por campo* sobre el contenido `.md`: usa el valor de la API
 *      salvo que venga vacío/ausente, en cuyo caso conserva el valor del `.md`.
 *   3. Ante cualquier error (API caída, timeout, respuesta inválida) cae al `.md`.
 *
 * Los archivos `.md` de `src/content/species` siguen siendo el respaldo y no se
 * modifican. La forma de datos que se entrega es idéntica al frontmatter, así que
 * los componentes de UI no cambian.
 */
import { getCollection, type CollectionEntry } from 'astro:content';
import { api } from './api';

/** Forma de una especia = frontmatter `.md` + análisis académico opcional de la API. */
export type SpeciesData = CollectionEntry<'species'>['data'] & {
	analisisAcademico?: {
		taxonomia?: string;
		etnobotanica?: string;
		fitoquimica?: string;
		sostenibilidad?: string;
	};
};

/** Entrada normalizada, compatible con `CollectionEntry<'species'>` (`id` + `data`). */
export interface SpeciesEntry {
	id: string;
	data: SpeciesData;
}

/** Placeholder inline (sin assets externos) para especies sin imagen ni en API ni en `.md`. */
export const IMAGE_PLACEHOLDER =
	'data:image/svg+xml;utf8,' +
	encodeURIComponent(
		'<svg xmlns="http://www.w3.org/2000/svg" width="16" height="9"><rect width="16" height="9" fill="#e2e8f0"/></svg>',
	);

// --- helpers de merge por campo -------------------------------------------------

const isBlank = (value: unknown): boolean =>
	value === undefined ||
	value === null ||
	value === '' ||
	(Array.isArray(value) && value.length === 0);

/** Devuelve el valor de la API salvo que esté vacío; en ese caso el valor del `.md`. */
const pick = <T>(apiValue: T, mdValue: T): T => (isBlank(apiValue) ? mdValue : apiValue);

/**
 * Merge por índice de arrays de objetos (`comercio.*`, `compuestosQuimicos`).
 * Si la API trae el array vacío, se conserva el del `.md`. Si trae contenido, se
 * combina campo a campo por posición para no perder detalle que el `.md` sí tiene
 * (ej. la API devuelve `compuestosQuimicos[].detalle` vacío).
 */
function mergeItemArray<T extends Record<string, unknown>>(
	apiArr: T[] | undefined,
	mdArr: T[] | undefined,
	keys: (keyof T)[],
): T[] {
	const a = apiArr ?? [];
	const m = mdArr ?? [];
	if (a.length === 0) return m;

	const length = Math.max(a.length, m.length);
	const out: T[] = [];
	for (let i = 0; i < length; i++) {
		const apiItem = a[i];
		const mdItem = m[i];
		if (apiItem && mdItem) {
			const merged = { ...mdItem } as T;
			for (const key of keys) merged[key] = pick(apiItem[key], mdItem[key]);
			out.push(merged);
		} else {
			out.push((apiItem ?? mdItem) as T);
		}
	}
	return out;
}

function skeleton(api: Record<string, any> | null): SpeciesData {
	return {
		slug: api?.slug,
		nombreComun: api?.nombreComun ?? '',
		nombreCientifico: api?.nombreCientifico ?? '',
		nombresAlternativos: [],
		taxonomia: { reino: '', division: '', clase: '', familia: '', genero: '' },
		etnobotanica: { clasificacion: '', parteUtilizada: '', usoTradicional: '' },
		perfilEtnobotanico: '',
		historiaEvolucion: { origen: '', dispersion: '', evolucion: '' },
		comercio: { exportacion: [], importacion: [] },
		compuestosQuimicos: [],
		multimediaPrincipal: {
			imagenUrl: '',
			imagenPublicId: '',
			videoUrl: '',
			videoPublicId: '',
			proveedor: '',
		},
		estado: 'ACTIVO',
	};
}

/**
 * Combina un registro de la API sobre el `.md` (merge por campo). Cualquiera de los
 * dos puede ser `null`: si la API es `null` se devuelve el `.md`; si el `.md` es
 * `null` (especie que solo existe en la API) se parte de un esqueleto vacío.
 */
export function mergeSpecies(
	apiData: Record<string, any> | null,
	mdData: SpeciesData | null,
): SpeciesData {
	const base: SpeciesData = mdData ?? skeleton(apiData);
	if (!apiData) return base;

	return {
		...base,
		slug: pick(apiData.slug, base.slug),
		nombreComun: pick(apiData.nombreComun, base.nombreComun),
		nombreCientifico: pick(apiData.nombreCientifico, base.nombreCientifico),
		nombresAlternativos: pick(apiData.nombresAlternativos, base.nombresAlternativos),
		taxonomia: {
			reino: pick(apiData.taxonomia?.reino, base.taxonomia.reino),
			division: pick(apiData.taxonomia?.division, base.taxonomia.division),
			clase: pick(apiData.taxonomia?.clase, base.taxonomia.clase),
			familia: pick(apiData.taxonomia?.familia, base.taxonomia.familia),
			genero: pick(apiData.taxonomia?.genero, base.taxonomia.genero),
		},
		etnobotanica: {
			clasificacion: pick(apiData.etnobotanica?.clasificacion, base.etnobotanica.clasificacion),
			parteUtilizada: pick(apiData.etnobotanica?.parteUtilizada, base.etnobotanica.parteUtilizada),
			usoTradicional: pick(apiData.etnobotanica?.usoTradicional, base.etnobotanica.usoTradicional),
		},
		perfilEtnobotanico: pick(apiData.perfilEtnobotanico, base.perfilEtnobotanico),
		historiaEvolucion: {
			origen: pick(apiData.historiaEvolucion?.origen, base.historiaEvolucion.origen),
			dispersion: pick(apiData.historiaEvolucion?.dispersion, base.historiaEvolucion.dispersion),
			evolucion: pick(apiData.historiaEvolucion?.evolucion, base.historiaEvolucion.evolucion),
		},
		comercio: {
			exportacion: mergeItemArray(apiData.comercio?.exportacion, base.comercio.exportacion, [
				'pais',
				'detalle',
			]),
			importacion: mergeItemArray(apiData.comercio?.importacion, base.comercio.importacion, [
				'pais',
				'detalle',
			]),
		},
		compuestosQuimicos: mergeItemArray(apiData.compuestosQuimicos, base.compuestosQuimicos, [
			'nombre',
			'detalle',
		]),
		multimediaPrincipal: {
			imagenUrl: pick(apiData.multimediaPrincipal?.imagenUrl, base.multimediaPrincipal.imagenUrl),
			imagenPublicId: pick(
				apiData.multimediaPrincipal?.imagenPublicId,
				base.multimediaPrincipal.imagenPublicId,
			),
			videoUrl: pick(apiData.multimediaPrincipal?.videoUrl, base.multimediaPrincipal.videoUrl),
			videoPublicId: pick(
				apiData.multimediaPrincipal?.videoPublicId,
				base.multimediaPrincipal.videoPublicId,
			),
			proveedor: pick(apiData.multimediaPrincipal?.proveedor, base.multimediaPrincipal.proveedor),
		},
		estado: pick(apiData.estado, base.estado),
		analisisAcademico: apiData.analisisAcademico ?? base.analisisAcademico,
	};
}

const sortByNombre = (rows: SpeciesEntry[]): SpeciesEntry[] =>
	[...rows].sort((a, b) => a.data.nombreComun.localeCompare(b.data.nombreComun, 'es'));

async function getActiveMdBySlug(): Promise<Map<string, CollectionEntry<'species'>>> {
	const mdEntries = await getCollection('species', ({ data }) => data.estado === 'ACTIVO');
	const bySlug = new Map<string, CollectionEntry<'species'>>();
	for (const entry of mdEntries) bySlug.set(entry.data.slug ?? entry.id, entry);
	return bySlug;
}

/**
 * Lista de especies para catálogo, home, atlas y panel de comercio.
 * Intenta `GET /api/plantas` (proyección liviana) y hace merge por slug con el
 * contenido `.md`. Los slugs son la unión (API ∪ md). Ante error → solo `.md`.
 */
export async function getSpeciesList(): Promise<{ rows: SpeciesEntry[]; source: 'api' | 'md' }> {
	const mdBySlug = await getActiveMdBySlug();

	let apiList: Record<string, any>[] | null = null;
	try {
		const data = await api.get('/api/plantas');
		if (Array.isArray(data)) {
			// El listado liviano no trae `estado`; se conserva salvo que venga marcado inactivo.
			apiList = data.filter((p) => !p.estado || p.estado === 'ACTIVO');
		}
	} catch {
		apiList = null;
	}

	if (!apiList) {
		const rows = [...mdBySlug.values()].map((entry) => ({
			id: entry.id,
			data: entry.data as SpeciesData,
		}));
		return { rows: sortByNombre(rows), source: 'md' };
	}

	const apiBySlug = new Map<string, Record<string, any>>();
	for (const item of apiList) if (item.slug) apiBySlug.set(item.slug, item);

	const slugs = new Set<string>([...mdBySlug.keys(), ...apiBySlug.keys()]);
	const rows: SpeciesEntry[] = [];
	for (const slug of slugs) {
		const mdEntry = mdBySlug.get(slug);
		const apiItem = apiBySlug.get(slug) ?? null;
		const data = mergeSpecies(apiItem, (mdEntry?.data as SpeciesData) ?? null);
		rows.push({ id: mdEntry?.id ?? slug, data });
	}
	return { rows: sortByNombre(rows), source: 'api' };
}

/**
 * Detalle completo de una especie para la ficha. Intenta `GET /api/plantas/:slug`
 * y hace merge por campo sobre `mdData`. Ante error devuelve `mdData` tal cual
 * (o un esqueleto si tampoco hay `.md`).
 */
export async function getSpeciesDetail(
	slug: string,
	mdData: SpeciesData | null,
): Promise<SpeciesData> {
	try {
		const apiDetail = await api.get(`/api/plantas/${slug}`);
		if (apiDetail && typeof apiDetail === 'object' && !Array.isArray(apiDetail)) {
			return mergeSpecies(apiDetail as Record<string, any>, mdData);
		}
	} catch {
		// fallthrough al fallback
	}
	return mdData ?? skeleton(null);
}
