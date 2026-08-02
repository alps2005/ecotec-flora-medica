	import { defineCollection } from 'astro:content';
	import { glob } from 'astro/loaders';
	import { z } from 'astro/zod';

	const species = defineCollection({
		loader: glob({ pattern: '**/*.md', base: './src/content/species' }),
		schema: z.object({
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
			}),
			perfilEtnobotanico: z.string(),
			historiaEvolucion: z.object({
				origen: z.string(),
				dispersion: z.string(),
				evolucion: z.string(),
			}),
			comercio: z.object({
				exportacion: z.array(
					z.object({
						pais: z.string(),
						detalle: z.string(),
					}),
				),
				importacion: z.array(
					z.object({
						pais: z.string(),
						detalle: z.string(),
					}),
				),
			}),
			compuestosQuimicos: z.array(
				z.object({
					nombre: z.string(),
					detalle: z.string(),
				}),
			),
			multimediaPrincipal: z.object({
				imagenUrl: z.string().url(),
				imagenPublicId: z.string().optional(),
				videoUrl: z.string().optional(),
				videoPublicId: z.string().optional(),
				proveedor: z.string(),
			}),
			estado: z.enum(['ACTIVO', 'INACTIVO', 'BORRADOR']),
		}),
	});

export const collections = { species };
