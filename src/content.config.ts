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
	loader: glob({ pattern: '**/*.json', base: './src/content/species' }),
	schema: z.object({
		commonName: z.string(),
		scientificName: z.string(),
		family: z.string(),
		description: z.string(),
		image: z.string().url(),
	}),
});

export const collections = { blog, species };