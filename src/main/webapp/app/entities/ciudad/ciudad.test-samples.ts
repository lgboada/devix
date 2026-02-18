import { ICiudad, NewCiudad } from './ciudad.model';

export const sampleWithRequiredData: ICiudad = {
  id: 1773,
  noCia: 8189,
  descripcion: 'yippee although given',
};

export const sampleWithPartialData: ICiudad = {
  id: 11121,
  noCia: 7320,
  descripcion: 'hovel',
};

export const sampleWithFullData: ICiudad = {
  id: 2767,
  noCia: 30077,
  descripcion: 'where yowza a',
};

export const sampleWithNewData: NewCiudad = {
  noCia: 1537,
  descripcion: 'ouch',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
