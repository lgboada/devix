import { IDetalleFactura, NewDetalleFactura } from './detalle-factura.model';

export const sampleWithRequiredData: IDetalleFactura = {
  id: 6770,
  noCia: 30468,
  cantidad: 4895,
  precioUnitario: 7189.26,
  subtotal: 14869.05,
  descuento: 23019.97,
  impuesto: 3133.9,
  total: 37.91,
};

export const sampleWithPartialData: IDetalleFactura = {
  id: 11299,
  noCia: 14177,
  cantidad: 20334,
  precioUnitario: 13442.29,
  subtotal: 8576.86,
  descuento: 13968.45,
  impuesto: 4962.57,
  total: 505,
};

export const sampleWithFullData: IDetalleFactura = {
  id: 18317,
  noCia: 17451,
  cantidad: 10438,
  precioUnitario: 16163.46,
  subtotal: 8580.24,
  descuento: 32705.03,
  impuesto: 13962.23,
  total: 8730.72,
};

export const sampleWithNewData: NewDetalleFactura = {
  noCia: 2651,
  cantidad: 21855,
  precioUnitario: 9207.34,
  subtotal: 15773.67,
  descuento: 800.87,
  impuesto: 8184.39,
  total: 13967.62,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
