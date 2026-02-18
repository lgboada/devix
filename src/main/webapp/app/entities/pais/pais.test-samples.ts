import { IPais, NewPais } from './pais.model';

export const sampleWithRequiredData: IPais = {
  id: 1578,
  noCia: 2650,
  descripcion: 'puff brr if',
};

export const sampleWithPartialData: IPais = {
  id: 13106,
  noCia: 3125,
  descripcion: 'jubilantly',
};

export const sampleWithFullData: IPais = {
  id: 26897,
  noCia: 14604,
  descripcion: 'apud',
};

export const sampleWithNewData: NewPais = {
  noCia: 4753,
  descripcion: 'now toward if',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
