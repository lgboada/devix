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
  id: 28503,
  noCia: 19216,
  dni: 'battle',
  nombres: 'painfully probe gut',
  apellidos: 'made-up whoever where',
  email: '2@txg.aeq',
  telefono1: 'fairly woefully astonishing',
  telefono2: 'next since yahoo',
  fechaNacimiento: dayjs('2026-02-05'),
  sexo: 'slip',
  estadoCivil: 'amongst allocation snuggle',
  tipoSangre: 'bend',
  pathImagen: 'furthermore like whether',
};

export const sampleWithFullData: ICliente = {
  id: 17129,
  noCia: 1359,
  dni: 'nor',
  nombres: 'as sew',
  apellidos: 'arcade',
  nombreComercial: 'cinema garrote phooey',
  email: 'fd@kffc.be',
  telefono1: 'obtrude wee why',
  telefono2: 'until',
  fechaNacimiento: dayjs('2026-02-04'),
  sexo: 'stuff cash',
  estadoCivil: 'kowtow joyfully defendant',
  tipoSangre: 'aboard fooey',
  pathImagen: 'glow',
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
