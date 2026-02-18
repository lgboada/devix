import { IMarca, NewMarca } from './marca.model';

export const sampleWithRequiredData: IMarca = {
  id: 3237,
  noCia: 25229,
  nombre: 'fatal irk',
  pathImagen: 'reconstitute ocelot anenst',
};

export const sampleWithPartialData: IMarca = {
  id: 3584,
  noCia: 8409,
  nombre: 'despite above aboard',
  pathImagen: 'abscond convalesce heartbeat',
};

export const sampleWithFullData: IMarca = {
  id: 29751,
  noCia: 21210,
  nombre: 'book sizzling knavishly',
  pathImagen: 'buzzing',
};

export const sampleWithNewData: NewMarca = {
  noCia: 29907,
  nombre: 'the passionate trench',
  pathImagen: 'quintessential',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
