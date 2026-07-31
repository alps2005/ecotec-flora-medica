import { getCollection } from 'astro:content';
import tradeData from '@/data/trade-data.json';

export interface TradeYearPoint {
  year: number;
  exportQtyKg: number;
  exportValueUsd: number;
  importQtyKg: number;
  importValueUsd: number;
}

export interface TradePartner {
  partnerCode: number;
  partnerName: string;
  valueUsd: number;
  qtyKg: number;
}

export interface SpeciesTradeRow {
  slug: string;
  nombreComun: string;
  nombreCientifico: string;
  familia: string;
  hasComtradeData: boolean;
  hsCode: string;
  hsDescription: string;
  approximate: boolean;
  yearly: TradeYearPoint[];
  partnerDataYear: number;
  partners: { export: TradePartner[]; import: TradePartner[] };
  narrative: {
    exportDetalle: string[];
    exportPaises: string[];
    importDetalle: string[];
    importPaises: string[];
  };
}

export interface TradeDataset {
  generatedAt: string;
  years: number[];
  reporterName: string;
  rows: SpeciesTradeRow[];
}

export async function loadTradeDataset(): Promise<TradeDataset> {
  const speciesEntries = await getCollection('species', ({ data }) => data.estado === 'ACTIVO');

  const rows: SpeciesTradeRow[] = speciesEntries
    .map((entry) => {
      const slug = entry.data.slug ?? entry.id;
      const trade = (tradeData.species as Record<string, any>)[slug];
      if (!trade) return null;

      return {
        slug,
        nombreComun: entry.data.nombreComun,
        nombreCientifico: entry.data.nombreCientifico,
        familia: entry.data.taxonomia.familia,
        hasComtradeData: Boolean(trade.hasData),
        hsCode: trade.hsCode,
        hsDescription: trade.hsDescription,
        approximate: Boolean(trade.approximate),
        yearly: trade.yearly,
        partnerDataYear: trade.partnerDataYear,
        partners: trade.partners,
        narrative: {
          exportDetalle: entry.data.comercio.exportacion.map((e) => e.detalle),
          exportPaises: entry.data.comercio.exportacion.map((e) => e.pais),
          importDetalle: entry.data.comercio.importacion.map((i) => i.detalle),
          importPaises: entry.data.comercio.importacion.map((i) => i.pais),
        },
      } satisfies SpeciesTradeRow;
    })
    .filter((row): row is SpeciesTradeRow => row !== null)
    .sort((a, b) => a.nombreComun.localeCompare(b.nombreComun, 'es'));

  return {
    generatedAt: tradeData.generatedAt,
    years: tradeData.years,
    reporterName: tradeData.reporterName,
    rows,
  };
}

export function computeKpis(rows: SpeciesTradeRow[]) {
  const totalSpecies = rows.length;
  const withComtradeData = rows.filter((r) => r.hasComtradeData).length;
  const narrativeOnly = totalSpecies - withComtradeData;

  const exportTotals = new Map<string, number>();
  const importTotals = new Map<string, number>();

  for (const row of rows) {
    for (const p of row.partners.export) {
      exportTotals.set(p.partnerName, (exportTotals.get(p.partnerName) ?? 0) + p.valueUsd);
    }
    for (const p of row.partners.import) {
      importTotals.set(p.partnerName, (importTotals.get(p.partnerName) ?? 0) + p.valueUsd);
    }
  }

  const topOf = (totals: Map<string, number>) => {
    const sorted = [...totals.entries()].sort((a, b) => b[1] - a[1]);
    return sorted[0]?.[0] ?? 'Sin datos';
  };

  return {
    totalSpecies,
    withComtradeData,
    narrativeOnly,
    topExportDestination: topOf(exportTotals),
    topImportOrigin: topOf(importTotals),
  };
}
