import dayjs from 'dayjs/esm';

import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 2897,
  noCia: 30633,
  dni: 'natural',
  nombres: 'yuck',
  apellidos: 'helplessly yahoo noisily',
  email: '2qbb@-.yn',
  fechaNacimiento: dayjs('2026-02-05'),
  sexo: 'per slake',
  estadoCivil: 'blah',
  tipoSangre: 'huzzah',
  pathImagen: 'yahoo er',
};

export const sampleWithPartialData: ICliente = {
  id: 10652,
  noCia: 28503,
  dni: 'boo bob',
  nombres: 'veg whoa',
  apellidos: 'ugh nearly',
  email: 'z2zsm-@f0ptx.na',
  telefono: 'times',
  fechaNacimiento: dayjs('2026-02-05'),
  sexo: 'woefully',
  estadoCivil: 'unethically next since',
  tipoSangre: 'huzzah',
  pathImagen: 'slip',
};

export const sampleWithFullData: ICliente = {
  id: 17129,
  noCia: 1359,
  dni: 'nor',
  nombres: 'as sew',
  apellidos: 'arcade',
  nombreComercial: 'cinema garrote phooey',
  email: 'fd@kffc.be',
  telefono: 'obtrude wee why',
  fechaNacimiento: dayjs('2026-02-05'),
  sexo: 'assail since',
  estadoCivil: 'cash yuck embody',
  tipoSangre: 'defendant gaseous',
  pathImagen: 'fooey',
};

export const sampleWithNewData: NewCliente = {
  noCia: 9610,
  dni: 'boo gratefully',
  nombres: 'amid',
  apellidos: 'off disrespect',
  email: 'j%q@h.mdot',
  fechaNacimiento: dayjs('2026-02-05'),
  sexo: 'honestly',
  estadoCivil: 'whether behind joyful',
  tipoSangre: 'huzzah',
  pathImagen: 'ultimate rag',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
