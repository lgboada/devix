import { ITipoProducto, NewTipoProducto } from './tipo-producto.model';

export const sampleWithRequiredData: ITipoProducto = {
  id: 8782,
  noCia: 8540,
  nombre: 'regarding',
};

export const sampleWithPartialData: ITipoProducto = {
  id: 16727,
  noCia: 3961,
  nombre: 'verbally',
};

export const sampleWithFullData: ITipoProducto = {
  id: 23050,
  noCia: 26424,
  nombre: 'jazz finally',
};

export const sampleWithNewData: NewTipoProducto = {
  noCia: 7318,
  nombre: 'kissingly repentant',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
