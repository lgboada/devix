import { ITipoCatalogo, NewTipoCatalogo } from './tipo-catalogo.model';

export const sampleWithRequiredData: ITipoCatalogo = {
  id: 5352,
  noCia: 21111,
  descripcion: 'truthfully the healthily',
  categoria: 'toward qua solemnly',
};

export const sampleWithPartialData: ITipoCatalogo = {
  id: 32475,
  noCia: 1478,
  descripcion: 'wetly vague and',
  categoria: 'unless whenever enchanting',
};

export const sampleWithFullData: ITipoCatalogo = {
  id: 11316,
  noCia: 8863,
  descripcion: 'another',
  categoria: 'shrill tennis',
};

export const sampleWithNewData: NewTipoCatalogo = {
  noCia: 4480,
  descripcion: 'burdensome',
  categoria: 'barring rationalize',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
