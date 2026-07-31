#!/usr/bin/env node
/**
 * Build-time fetch: pulls Ecuador's (UN Comtrade reporter 218) import/export
 * volume and value from the UN Comtrade "preview" API for every unique HS
 * code used by src/data/hs-code-map.mjs, then writes a single cache file at
 * src/data/trade-data.json.
 *
 * This is intentionally NOT called at request time — the Astro page reads
 * the generated JSON file only. Re-run manually with:
 *
 *   node scripts/fetch-comtrade-data.mjs
 *
 * to refresh the cache (e.g. once a new species/HS mapping is added).
 *
 * Docs: https://comtradeplus.un.org/ (public "preview" endpoint, no API key
 * required, but rate-limited and capped at 1 reference period per request).
 */
import { writeFile } from 'node:fs/promises';
import { fileURLToPath } from 'node:url';
import { dirname, resolve } from 'node:path';
import { HS_CODE_MAP } from '../src/data/hs-code-map.mjs';

const __dirname = dirname(fileURLToPath(import.meta.url));
const OUTPUT_PATH = resolve(__dirname, '../src/data/trade-data.json');

const REPORTER_CODE = 218; // Ecuador
const REPORTER_NAME = 'Ecuador';
const YEARS = [2019, 2020, 2021, 2022, 2023];
const BASE_URL = 'https://comtradeapi.un.org/public/v1/preview/C/A/HS';
const PARTNER_REFERENCE_URL = 'https://comtradeapi.un.org/files/v1/app/reference/partnerAreas.json';

const REQUEST_DELAY_MS = 900;
const MAX_RETRIES = 5;
const TOP_PARTNERS = 5;

// A handful of common trade-partner names translated to Spanish for display;
// anything not listed here falls back to the English name from the
// Comtrade reference file.
const PARTNER_NAME_ES = {
  'World': 'Mundo',
  'USA': 'Estados Unidos',
  'United States of America': 'Estados Unidos',
  'China': 'China',
  'Germany': 'Alemania',
  'Netherlands': 'Países Bajos',
  'Spain': 'España',
  'France': 'Francia',
  'Italy': 'Italia',
  'Colombia': 'Colombia',
  'Peru': 'Perú',
  'Chile': 'Chile',
  'Japan': 'Japón',
  'Rep. of Korea': 'Corea del Sur',
  'Russian Federation': 'Rusia',
  'United Kingdom': 'Reino Unido',
  'Belgium': 'Bélgica',
  'Canada': 'Canadá',
  'Mexico': 'México',
  'Brazil': 'Brasil',
  'Panama': 'Panamá',
  'Areas, nes': 'Áreas no especificadas',
  'Other Asia, nes': 'Otras zonas de Asia (n.e.)',
};

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

async function fetchJSON(url, attempt = 1) {
  const res = await fetch(url);
  if (res.status === 429 || res.status >= 500) {
    if (attempt > MAX_RETRIES) {
      throw new Error(`Giving up on ${url} after ${attempt} attempts (HTTP ${res.status})`);
    }
    const backoff = 1500 * attempt;
    console.warn(`  HTTP ${res.status}, retrying in ${backoff}ms (attempt ${attempt}/${MAX_RETRIES})...`);
    await sleep(backoff);
    return fetchJSON(url, attempt + 1);
  }
  if (!res.ok) {
    const body = await res.text().catch(() => '');
    throw new Error(`HTTP ${res.status} for ${url}: ${body.slice(0, 200)}`);
  }
  return res.json();
}

function sumFlow(rows, flowCode) {
  const matching = rows.filter((r) => r.flowCode === flowCode);
  return {
    qtyKg: round2(matching.reduce((sum, r) => sum + (Number(r.netWgt) || 0), 0)),
    valueUsd: round2(matching.reduce((sum, r) => sum + (Number(r.primaryValue) || 0), 0)),
  };
}

function round2(n) {
  return Math.round(n * 100) / 100;
}

async function fetchYearlyWorldTotals(hsCode) {
  const yearly = [];
  for (const year of YEARS) {
    const url = `${BASE_URL}?reporterCode=${REPORTER_CODE}&period=${year}&partnerCode=0&cmdCode=${hsCode}&flowCode=M,X`;
    const json = await fetchJSON(url);
    const rows = json.data ?? [];
    const exp = sumFlow(rows, 'X');
    const imp = sumFlow(rows, 'M');
    yearly.push({
      year,
      exportQtyKg: exp.qtyKg,
      exportValueUsd: exp.valueUsd,
      importQtyKg: imp.qtyKg,
      importValueUsd: imp.valueUsd,
    });
    await sleep(REQUEST_DELAY_MS);
  }
  return yearly;
}

async function fetchPartnerBreakdown(hsCode, yearly) {
  // Prefer the most recent year that actually has data so the partner
  // breakdown isn't empty; fall back to the latest configured year.
  const yearWithData = [...yearly].reverse().find((y) => y.exportValueUsd > 0 || y.importValueUsd > 0);
  const period = (yearWithData ?? yearly[yearly.length - 1]).year;

  const url = `${BASE_URL}?reporterCode=${REPORTER_CODE}&period=${period}&cmdCode=${hsCode}&flowCode=M,X`;
  const json = await fetchJSON(url);
  await sleep(REQUEST_DELAY_MS);
  const rows = (json.data ?? []).filter((r) => r.partnerCode !== 0);

  const byPartner = (flowCode) => {
    const totals = new Map();
    for (const row of rows) {
      if (row.flowCode !== flowCode) continue;
      const key = row.partnerCode;
      const prev = totals.get(key) ?? { partnerCode: key, valueUsd: 0, qtyKg: 0 };
      prev.valueUsd += Number(row.primaryValue) || 0;
      prev.qtyKg += Number(row.netWgt) || 0;
      totals.set(key, prev);
    }
    return [...totals.values()]
      .map((t) => ({ ...t, valueUsd: round2(t.valueUsd), qtyKg: round2(t.qtyKg) }))
      .sort((a, b) => b.valueUsd - a.valueUsd)
      .slice(0, TOP_PARTNERS);
  };

  return { period, export: byPartner('X'), import: byPartner('M') };
}

async function main() {
  console.log('Fetching UN Comtrade partner name reference...');
  const partnerRef = await fetchJSON(PARTNER_REFERENCE_URL);
  const partnerNames = new Map(
    (partnerRef.results ?? []).map((p) => [Number(p.PartnerCode), (p.PartnerDesc ?? p.text ?? '').trim()]),
  );
  const nameFor = (code) => {
    const en = partnerNames.get(code) ?? `Código ${code}`;
    return PARTNER_NAME_ES[en] ?? en;
  };

  const uniqueHsCodes = [...new Set(Object.values(HS_CODE_MAP).map((v) => v.hsCode))];
  console.log(`Fetching data for ${uniqueHsCodes.length} unique HS codes (${Object.keys(HS_CODE_MAP).length} species)...`);

  const hsCache = {};
  for (const [index, hsCode] of uniqueHsCodes.entries()) {
    console.log(`[${index + 1}/${uniqueHsCodes.length}] HS ${hsCode}`);
    const yearly = await fetchYearlyWorldTotals(hsCode);
    const partners = await fetchPartnerBreakdown(hsCode, yearly);
    hsCache[hsCode] = {
      yearly,
      partnerDataYear: partners.period,
      partners: {
        export: partners.export.map((p) => ({ ...p, partnerName: nameFor(p.partnerCode) })),
        import: partners.import.map((p) => ({ ...p, partnerName: nameFor(p.partnerCode) })),
      },
    };
  }

  const species = {};
  for (const [slug, meta] of Object.entries(HS_CODE_MAP)) {
    const cached = hsCache[meta.hsCode];
    const hasData = cached.yearly.some((y) => y.exportValueUsd > 0 || y.importValueUsd > 0);
    species[slug] = {
      hsCode: meta.hsCode,
      hsDescription: meta.hsDescription,
      approximate: meta.approximate,
      hasData,
      yearly: cached.yearly,
      partnerDataYear: cached.partnerDataYear,
      partners: cached.partners,
    };
  }

  const output = {
    generatedAt: new Date().toISOString(),
    reporterCode: REPORTER_CODE,
    reporterName: REPORTER_NAME,
    years: YEARS,
    source: 'UN Comtrade (comtradeapi.un.org public preview endpoint)',
    species,
  };

  await writeFile(OUTPUT_PATH, JSON.stringify(output, null, 2) + '\n', 'utf-8');
  console.log(`\nWrote ${OUTPUT_PATH}`);
  const withData = Object.values(species).filter((s) => s.hasData).length;
  console.log(`${withData}/${Object.keys(species).length} species have non-zero Comtrade figures for ${YEARS[0]}-${YEARS[YEARS.length - 1]}.`);
}

main().catch((err) => {
  console.error(err);
  process.exit(1);
});
