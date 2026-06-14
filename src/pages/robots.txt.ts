import type { APIRoute } from 'astro';

export const prerender = true;

export const GET: APIRoute = ({ request }) => {
	const sitemapURL = new URL('/sitemap-index.xml', request.url).href;

	return new Response(
		`User-agent: *\nAllow: /\nSitemap: ${sitemapURL}\n`,
		{
			headers: {
				'Content-Type': 'text/plain; charset=utf-8',
			},
		}
	);
};