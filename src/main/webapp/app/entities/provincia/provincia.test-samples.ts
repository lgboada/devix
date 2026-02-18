import { IProvincia, NewProvincia } from './provincia.model';

export const sampleWithRequiredData: IProvincia = {
  id: 8780,
  noCia: 25235,
  descripcion: 'searchingly fatally',
};

export const sampleWithPartialData: IProvincia = {
  id: 17074,
  noCia: 2324,
  descripcion: 'enrage boo carelessly',
};

export const sampleWithFullData: IProvincia = {
  id: 11606,
  noCia: 4074,
  descripcion: 'alongside',
};

export const sampleWithNewData: NewProvincia = {
  noCia: 14269,
  descripcion: 'versus',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
