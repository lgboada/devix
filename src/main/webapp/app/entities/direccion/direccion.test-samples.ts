import { IDireccion, NewDireccion } from './direccion.model';

export const sampleWithRequiredData: IDireccion = {
  id: 9445,
  noCia: 11885,
  descripcion: 'warmly testify uh-huh',
};

export const sampleWithPartialData: IDireccion = {
  id: 12950,
  noCia: 17628,
  descripcion: 'but',
  telefono: 'pasta hmph er',
  latitud: 25868.05,
  longitud: 10585.31,
};

export const sampleWithFullData: IDireccion = {
  id: 12937,
  noCia: 11103,
  descripcion: 'despite shrilly',
  telefono: 'handle',
  latitud: 4317.77,
  longitud: 16877.06,
};

export const sampleWithNewData: NewDireccion = {
  noCia: 23613,
  descripcion: 'vivaciously oddball',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
