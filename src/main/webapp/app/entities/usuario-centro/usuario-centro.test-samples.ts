import { IUsuarioCentro, NewUsuarioCentro } from './usuario-centro.model';

export const sampleWithRequiredData: IUsuarioCentro = {
  id: 25001,
  noCia: 16370,
  principal: true,
};

export const sampleWithPartialData: IUsuarioCentro = {
  id: 28462,
  noCia: 20917,
  principal: true,
};

export const sampleWithFullData: IUsuarioCentro = {
  id: 2951,
  noCia: 7218,
  principal: true,
};

export const sampleWithNewData: NewUsuarioCentro = {
  noCia: 24229,
  principal: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
