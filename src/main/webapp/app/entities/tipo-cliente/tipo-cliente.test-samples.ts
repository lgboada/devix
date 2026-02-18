import { ITipoCliente, NewTipoCliente } from './tipo-cliente.model';

export const sampleWithRequiredData: ITipoCliente = {
  id: 984,
  noCia: 10493,
  descripcion: 'overconfidently',
};

export const sampleWithPartialData: ITipoCliente = {
  id: 14747,
  noCia: 24116,
  descripcion: 'meh',
};

export const sampleWithFullData: ITipoCliente = {
  id: 29876,
  noCia: 24039,
  descripcion: 'when',
};

export const sampleWithNewData: NewTipoCliente = {
  noCia: 30815,
  descripcion: 'except pish',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
