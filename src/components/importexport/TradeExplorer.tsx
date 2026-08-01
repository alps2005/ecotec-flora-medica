import * as React from 'react';
import { Bar, BarChart, CartesianGrid, XAxis, YAxis } from 'recharts';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Badge } from '@/components/ui/badge';
import {
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
  ChartLegend,
  ChartLegendContent,
  type ChartConfig,
} from '@/components/ui/chart';
import type { SpeciesTradeRow } from '@/lib/trade-data';

const chartConfig = {
  exportQtyKg: { label: 'Exportación', color: '#0A5CA5' },
  importQtyKg: { label: 'Importación', color: '#2BBAE2' },
} satisfies ChartConfig;

const kgFormatter = new Intl.NumberFormat('es-EC', { maximumFractionDigits: 0 });
const kgCompactFormatter = new Intl.NumberFormat('es-EC', {
  notation: 'compact',
  maximumFractionDigits: 1,
});
const usdFormatter = new Intl.NumberFormat('es-EC', {
  style: 'currency',
  currency: 'USD',
  maximumFractionDigits: 0,
});

function formatKg(value: number) {
  return `${kgFormatter.format(value)} kg`;
}

interface Props {
  rows: SpeciesTradeRow[];
  defaultSlug: string;
}

export default function TradeExplorer({ rows, defaultSlug }: Props) {
  const [slug, setSlug] = React.useState(defaultSlug);
  const species = React.useMemo(
    () => rows.find((r) => r.slug === slug) ?? rows[0],
    [rows, slug],
  );
  const selectItems = React.useMemo(
    () =>
      Object.fromEntries(
        rows.map((r) => [r.slug, r.hasComtradeData ? r.nombreComun : `${r.nombreComun} · cualitativo`]),
      ),
    [rows],
  );

  if (!species) return null;

  const chartData = species.yearly.map((y) => ({
    year: String(y.year),
    exportQtyKg: y.exportQtyKg,
    importQtyKg: y.importQtyKg,
    exportValueUsd: y.exportValueUsd,
    importValueUsd: y.importValueUsd,
  }));

  return (
    <div className="flex flex-col gap-6">
      <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <p className="text-sm font-medium text-text-muted">Especie seleccionada</p>
          <h3 className="font-display text-2xl text-text">{species.nombreComun}</h3>
          <p className="text-sm italic text-text-muted">{species.nombreCientifico}</p>
        </div>
        <Select value={species.slug} onValueChange={setSlug} items={selectItems}>
          <SelectTrigger className="w-full sm:w-72" aria-label="Seleccionar especie">
            <SelectValue placeholder="Selecciona una especie" />
          </SelectTrigger>
          <SelectContent className="max-h-80">
            {rows.map((r) => (
              <SelectItem key={r.slug} value={r.slug}>
                {r.nombreComun}
                {!r.hasComtradeData ? ' · cualitativo' : ''}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      <div className="flex flex-wrap items-center gap-2">
        {species.hasComtradeData ? (
          <Badge className="bg-primary/10 text-primary border-primary/20">
            Comtrade · HS {species.hsCode}
          </Badge>
        ) : (
          <Badge variant="outline" className="border-tertiary/30 text-text-muted">
            Cualitativo · sin cifras de Comtrade
          </Badge>
        )}
        {species.approximate && species.hasComtradeData ? (
          <Badge variant="outline" className="border-tertiary/30 text-text-muted">
            Categoría aproximada: {species.hsDescription}
          </Badge>
        ) : null}
      </div>

      {species.hasComtradeData ? (
        <ChartContainer config={chartConfig} className="aspect-auto h-72 w-full">
          <BarChart data={chartData} margin={{ left: 4, right: 4 }}>
            <CartesianGrid vertical={false} strokeDasharray="3 3" />
            <XAxis dataKey="year" tickLine={false} axisLine={false} tickMargin={8} />
            <YAxis
              tickLine={false}
              axisLine={false}
              tickMargin={8}
              width={56}
              tickFormatter={(v: number) => kgCompactFormatter.format(v)}
            />
            <ChartTooltip
              content={
                <ChartTooltipContent
                  formatter={(value, name, item) => {
                    const isExport = name === 'exportQtyKg';
                    const usd = isExport ? item.payload.exportValueUsd : item.payload.importValueUsd;
                    const label = isExport ? 'Exportación' : 'Importación';
                    return (
                      <div className="flex w-full items-baseline justify-between gap-4">
                        <span className="text-muted-foreground">{label}</span>
                        <span className="font-mono font-medium tabular-nums text-foreground">
                          {formatKg(Number(value))}
                          <span className="ml-1 text-muted-foreground">
                            ({usdFormatter.format(usd)})
                          </span>
                        </span>
                      </div>
                    );
                  }}
                />
              }
            />
            <ChartLegend content={<ChartLegendContent />} />
            <Bar dataKey="exportQtyKg" fill="var(--color-exportQtyKg)" radius={4} />
            <Bar dataKey="importQtyKg" fill="var(--color-importQtyKg)" radius={4} />
          </BarChart>
        </ChartContainer>
      ) : (
        <div className="rounded-2xl border border-dashed border-tertiary/20 bg-surface-muted p-5 text-sm text-text-muted">
          No se encontraron cifras de Comtrade para esta especie (HS {species.hsCode}) en el
          periodo consultado. A continuación, la dinámica comercial descrita en la ficha de la
          especie:
        </div>
      )}

      <div className="grid gap-4 sm:grid-cols-2">
        <NarrativePanel
          title="Exportación (relato)"
          countries={species.narrative.exportPaises}
          detalle={species.narrative.exportDetalle}
        />
        <NarrativePanel
          title="Importación (relato)"
          countries={species.narrative.importPaises}
          detalle={species.narrative.importDetalle}
        />
      </div>
    </div>
  );
}

function NarrativePanel({
  title,
  countries,
  detalle,
}: {
  title: string;
  countries: string[];
  detalle: string[];
}) {
  if (countries.length === 0) {
    return (
      <div className="rounded-2xl border border-tertiary/10 bg-white/60 p-4 text-sm text-text-muted">
        <p className="mb-1 font-medium text-text">{title}</p>
        <p>Sin información registrada.</p>
      </div>
    );
  }

  return (
    <div className="rounded-2xl border border-tertiary/10 bg-white/60 p-4 text-sm">
      <p className="mb-2 font-medium text-text">{title}</p>
      <ul className="flex flex-col gap-2">
        {countries.map((pais, i) => (
          <li key={pais}>
            <p className="font-medium text-text-muted">{pais}</p>
            <p className="text-text-muted/90">{detalle[i]}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}
