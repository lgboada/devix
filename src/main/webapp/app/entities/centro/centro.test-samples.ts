import { ICentro, NewCentro } from './centro.model';

export const sampleWithRequiredData: ICentro = {
  id: 14353,
  noCia: 22412,
  descripcion: 'strictly likewise yahoo',
};

export const sampleWithPartialData: ICentro = {
  id: 10435,
  noCia: 4657,
  descripcion: 'when',
};

export const sampleWithFullData: ICentro = {
  id: 11111,
  noCia: 2573,
  descripcion: 'eventually ordinary boo',
};

export const sampleWithNewData: NewCentro = {
  noCia: 12173,
  descripcion: 'within',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
