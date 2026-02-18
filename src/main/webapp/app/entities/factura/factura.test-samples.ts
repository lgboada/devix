import dayjs from 'dayjs/esm';

import { IFactura, NewFactura } from './factura.model';

export const sampleWithRequiredData: IFactura = {
  id: 24443,
  noCia: 14360,
  serie: 'ragged',
  noFisico: 'stark',
  fecha: dayjs('2026-02-05T08:53'),
  subtotal: 29046.28,
  impuesto: 8328.52,
  impuestoCero: 22330.37,
  descuento: 6298.76,
  total: 25451,
  porcentajeImpuesto: 32186.46,
  cedula: 'meh',
  direccion: 'slide',
  email: 'Hernan_RomeroLedesma@hotmail.com',
  estado: 'gracefully jovially',
};

export const sampleWithPartialData: IFactura = {
  id: 14938,
  noCia: 1089,
  serie: 'disgorge opposite make',
  noFisico: 'unibody',
  fecha: dayjs('2026-02-05T01:59'),
  subtotal: 22346.68,
  impuesto: 3082.5,
  impuestoCero: 5486.45,
  descuento: 16100.92,
  total: 24867.63,
  porcentajeImpuesto: 592.8,
  cedula: 'mountain even',
  direccion: 'upliftingly thunderbolt',
  email: 'Luisa_RomeroCardona32@gmail.com',
  estado: 'lest ew great',
};

export const sampleWithFullData: IFactura = {
  id: 12483,
  noCia: 24817,
  serie: 'tame er overcooked',
  noFisico: 'weekly whoa',
  fecha: dayjs('2026-02-04T23:44'),
  subtotal: 19870.77,
  impuesto: 27176.84,
  impuestoCero: 24299.6,
  descuento: 8563.06,
  total: 4769.67,
  porcentajeImpuesto: 17429.22,
  cedula: 'excitedly when minus',
  direccion: 'adjourn seemingly why',
  email: 'Mateo_RoybalEstrada16@hotmail.com',
  estado: 'daily',
};

export const sampleWithNewData: NewFactura = {
  noCia: 30787,
  serie: 'mmm',
  noFisico: 'pricey',
  fecha: dayjs('2026-02-05T15:15'),
  subtotal: 823.75,
  impuesto: 21422.7,
  impuestoCero: 12743.79,
  descuento: 29936.3,
  total: 19001.39,
  porcentajeImpuesto: 32706.25,
  cedula: 'pomelo',
  direccion: 'scent',
  email: 'Veronica.MontezQuinones@hotmail.com',
  estado: 'yawningly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
