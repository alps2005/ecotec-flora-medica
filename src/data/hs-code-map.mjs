/**
 * Maps each species (by content-collection slug) to the closest matching
 * HS (Harmonized System) 6-digit commodity code used to query the UN
 * Comtrade database for Ecuador's trade figures.
 *
 * `approximate: true` means there is no HS line specific to the species
 * itself (most raw medicinal herbs are not broken out individually in
 * trade statistics) — the code covers a broader category it falls under,
 * most commonly HS 1211.90 "Plants and parts of plants used primarily in
 * perfumery, pharmacy or similar purposes, n.e.c." This should be
 * surfaced in the UI as an approximation, not a species-specific figure.
 */
export const HS_CODE_MAP = {
  'achiote': { hsCode: '140490', hsDescription: 'Productos vegetales n.c.o.p. (incl. semilla de achiote/annatto)', approximate: true },
  'ajo': { hsCode: '070320', hsDescription: 'Ajos, frescos o refrigerados', approximate: false },
  'aloe-vera': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'apio': { hsCode: '070940', hsDescription: 'Apio (excepto apionabo), fresco o refrigerado', approximate: false },
  'babaco': { hsCode: '081090', hsDescription: 'Las demás frutas frescas n.c.o.p.', approximate: true },
  'cebolla': { hsCode: '070310', hsDescription: 'Cebollas y chalotes, frescos o refrigerados', approximate: false },
  'cedron': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'cola-de-caballo': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'culantro': { hsCode: '070999', hsDescription: 'Las demás hortalizas frescas o refrigeradas n.c.o.p.', approximate: true },
  'dulcamara': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'eucalipto': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'guanabana': { hsCode: '081090', hsDescription: 'Las demás frutas frescas n.c.o.p.', approximate: true },
  'guayaba': { hsCode: '080450', hsDescription: 'Guayabas, mangos y mangostanes, frescos o secos', approximate: false },
  'hierba-luisa': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'hortensia': { hsCode: '060290', hsDescription: 'Las demás plantas vivas (esquejes, injertos, ornamentales)', approximate: true },
  'jengibre': { hsCode: '091010', hsDescription: 'Jengibre', approximate: false },
  'lazo-de-amor': { hsCode: '060290', hsDescription: 'Las demás plantas vivas (esquejes, injertos, ornamentales)', approximate: true },
  'limon': { hsCode: '080550', hsDescription: 'Limones y limas, frescos o secos', approximate: false },
  'llanten': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'madre-de-miles': { hsCode: '060290', hsDescription: 'Las demás plantas vivas (esquejes, injertos, ornamentales)', approximate: true },
  'mandarina': { hsCode: '080520', hsDescription: 'Mandarinas, tangerinas y satsumas', approximate: false },
  'manzanilla': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'matico': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'menta-piperita': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'naranja-dulce': { hsCode: '080510', hsDescription: 'Naranjas, frescas o secas', approximate: false },
  'naranjo-amargo': { hsCode: '080510', hsDescription: 'Naranjas, frescas o secas (comparte código con naranja dulce)', approximate: true },
  'neem': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'ortiga': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'papaya': { hsCode: '080720', hsDescription: 'Papayas, frescas', approximate: false },
  'pepino': { hsCode: '070700', hsDescription: 'Pepinos y pepinillos, frescos o refrigerados', approximate: false },
  'perejil': { hsCode: '070999', hsDescription: 'Las demás hortalizas frescas o refrigeradas n.c.o.p.', approximate: true },
  'pimiento': { hsCode: '070960', hsDescription: 'Pimientos del género Capsicum o Pimenta, frescos', approximate: false },
  'romero': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'ruda': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'tomate-de-arbol': { hsCode: '081090', hsDescription: 'Las demás frutas frescas n.c.o.p.', approximate: true },
  'tomate': { hsCode: '070200', hsDescription: 'Tomates, frescos o refrigerados', approximate: false },
  'tomillo': { hsCode: '091099', hsDescription: 'Las demás especias n.c.o.p. (incl. tomillo)', approximate: true },
  'una-de-gato': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'uvilla': { hsCode: '081090', hsDescription: 'Las demás frutas frescas n.c.o.p.', approximate: true },
  'valeriana': { hsCode: '121190', hsDescription: 'Plantas y partes de plantas para perfumería/farmacia, otras', approximate: true },
  'zapallo': { hsCode: '070993', hsDescription: 'Calabazas, zapallos y calabazas confiteras, frescos o refrigerados', approximate: false },
};
