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

const taxonomySchema = z.object({
	reino: z.string(),
	division: z.string(),
	clase: z.string(),
	familia: z.string(),
	genero: z.string(),
});

const etnobotanicaSchema = z.object({
	clasificacion: z.string().optional(),
	parteUtilizada: z.string().optional(),
	usoTradicional: z.string().optional(),
	compuestosQuimicos: z.array(z.string()).default([]),
}).optional();

const analisisAcademicoSchema = z.object({
	taxonomia: z.string().optional(),
	etnobotanica: z.string().optional(),
	fitoquimica: z.string().optional(),
	sostenibilidad: z.string().optional(),
}).optional();

const multimediaPrincipalSchema = z.object({
	imagenUrl: z.string().optional(),
	imagenPublicId: z.string().optional(),
	videoUrl: z.string().optional(),
	videoPublicId: z.string().optional(),
	proveedor: z.enum(['CLOUDINARY', 'NINGUNO']).default('NINGUNO'),
}).optional();

const species = defineCollection({
	loader: glob({ pattern: '**/*.json', base: './src/content/species' }),
	schema: z.object({
		slug: z.string(),
		nombreComun: z.string(),
		nombreCientifico: z.string(),
		nombresAlternativos: z.array(z.string()).default([]),
		taxonomia: taxonomySchema,
		etnobotanica: etnobotanicaSchema,
		analisisAcademico: analisisAcademicoSchema,
		multimediaPrincipal: multimediaPrincipalSchema,
		estado: z.enum(['ACTIVO', 'INACTIVO']).default('ACTIVO'),
	}),
});

export const collections = { blog, species };