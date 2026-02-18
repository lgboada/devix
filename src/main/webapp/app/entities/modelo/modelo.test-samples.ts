import { IModelo, NewModelo } from './modelo.model';

export const sampleWithRequiredData: IModelo = {
  id: 26379,
  noCia: 2554,
  nombre: 'wherever',
  pathImagen: 'lean',
};

export const sampleWithPartialData: IModelo = {
  id: 24216,
  noCia: 11413,
  nombre: 'ugh irritably congregate',
  pathImagen: 'selfishly',
};

export const sampleWithFullData: IModelo = {
  id: 31392,
  noCia: 24723,
  nombre: 'to versus throughout',
  pathImagen: 'pace furthermore',
};

export const sampleWithNewData: NewModelo = {
  noCia: 19803,
  nombre: 'um',
  pathImagen: 'beret',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
