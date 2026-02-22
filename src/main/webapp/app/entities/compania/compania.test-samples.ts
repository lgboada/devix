import { ICompania, NewCompania } from './compania.model';

export const sampleWithRequiredData: ICompania = {
  id: 23801,
  noCia: 18311,
  dni: 'dispose switchboard lotion',
  nombre: 'whenever who',
  direccion: 'when',
  email: 'iv7+@gu25o.ej',
  telefono: 'jubilantly',
  pathImage: 'firm',
  activa: false,
};

export const sampleWithPartialData: ICompania = {
  id: 7958,
  noCia: 8911,
  dni: 'meaningfully diagram',
  nombre: 'gallery above',
  direccion: 'excepting',
  email: 'zue-k@jocq.cy',
  telefono: 'clueless',
  pathImage: 'woot oof pepper',
  activa: false,
};

export const sampleWithFullData: ICompania = {
  id: 5705,
  noCia: 21859,
  dni: 'woot',
  nombre: 'astride terraform',
  direccion: 'jogging lest',
  email: 'yd_16p@0.sn',
  telefono: 'nasalise pertain solace',
  pathImage: 'writ outside',
  activa: false,
};

export const sampleWithNewData: NewCompania = {
  noCia: 28722,
  dni: 'border amidst ack',
  nombre: 'vice soupy because',
  direccion: 'even',
  email: '+a@y..idvj',
  telefono: 'plus gracefully',
  pathImage: 'worth definitive',
  activa: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
