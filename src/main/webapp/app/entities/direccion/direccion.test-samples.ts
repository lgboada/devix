import { IDireccion, NewDireccion } from './direccion.model';

export const sampleWithRequiredData: IDireccion = {
  id: 9445,
  noCia: 11885,
  descripcion: 'warmly testify uh-huh',
};

export const sampleWithPartialData: IDireccion = {
  id: 5517,
  noCia: 12950,
  descripcion: 'husband unabashedly',
  pais: 'aboard',
  provincia: 'er aha miserable',
};

export const sampleWithFullData: IDireccion = {
  id: 12937,
  noCia: 11103,
  descripcion: 'despite shrilly',
  pais: 'handle',
  provincia: 'modulo',
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
