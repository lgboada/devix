import { ITipoEvento, NewTipoEvento } from './tipo-evento.model';

export const sampleWithRequiredData: ITipoEvento = {
  id: 7410,
  noCia: 20686,
  nombre: 'untrue freely pushy',
};

export const sampleWithPartialData: ITipoEvento = {
  id: 31455,
  noCia: 15584,
  nombre: 'safely alert bide',
};

export const sampleWithFullData: ITipoEvento = {
  id: 21108,
  noCia: 504,
  nombre: 'gad',
};

export const sampleWithNewData: NewTipoEvento = {
  noCia: 1662,
  nombre: 'list after fooey',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
