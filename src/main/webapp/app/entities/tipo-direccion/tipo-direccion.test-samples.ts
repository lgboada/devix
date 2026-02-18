import { ITipoDireccion, NewTipoDireccion } from './tipo-direccion.model';

export const sampleWithRequiredData: ITipoDireccion = {
  id: 25316,
  noCia: 24034,
  descripcion: 'aw round',
};

export const sampleWithPartialData: ITipoDireccion = {
  id: 25774,
  noCia: 16114,
  descripcion: 'sightseeing',
};

export const sampleWithFullData: ITipoDireccion = {
  id: 2394,
  noCia: 14748,
  descripcion: 'edge',
};

export const sampleWithNewData: NewTipoDireccion = {
  noCia: 30432,
  descripcion: 'or',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
