import { IEmpleado, NewEmpleado } from './empleado.model';

export const sampleWithRequiredData: IEmpleado = {
  id: 9977,
  noCia: 11533,
  dni: 'fooey joyfully brown',
  nombre: 'palate same solidly',
  email: '_94.9@-72.fqk',
  pathImagen: 'divine about who',
  telefono: 'irk anenst',
};

export const sampleWithPartialData: IEmpleado = {
  id: 7440,
  noCia: 22194,
  dni: 'unwilling while restaurant',
  nombre: 'but',
  contacto: 'inure boo legitimize',
  email: 'qwhdsd@dmgz.afzv',
  pathImagen: 'disconnection in',
  telefono: 'oof uncover',
};

export const sampleWithFullData: IEmpleado = {
  id: 30067,
  noCia: 20087,
  dni: 'infatuated mealy',
  nombre: 'step except',
  contacto: 'really',
  email: '40ru_@jb.zw',
  pathImagen: 'enthusiastically pish overconfidently',
  telefono: 'gift',
};

export const sampleWithNewData: NewEmpleado = {
  noCia: 12406,
  dni: 'papa including around',
  nombre: 'grandson fake pfft',
  email: 'v0g%c@eshw.er',
  pathImagen: 'rigidly now',
  telefono: 'following',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
