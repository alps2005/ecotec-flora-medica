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
				compuestosQuimicos: z.array(z.string()),
			}),
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
