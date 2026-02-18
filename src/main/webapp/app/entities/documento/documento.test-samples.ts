import dayjs from 'dayjs/esm';

import { IDocumento, NewDocumento } from './documento.model';

export const sampleWithRequiredData: IDocumento = {
  id: 11188,
  noCia: 31414,
  path: 'ugh censor',
};

export const sampleWithPartialData: IDocumento = {
  id: 4017,
  noCia: 774,
  tipo: 'consequently vengeful',
  observacion: 'blah',
  path: 'laughter nor',
};

export const sampleWithFullData: IDocumento = {
  id: 31980,
  noCia: 30674,
  tipo: 'than boastfully sans',
  observacion: 'aw',
  fechaCreacion: dayjs('2026-02-04T23:14'),
  fechaVencimiento: dayjs('2026-02-05T11:42'),
  path: 'palatable',
};

export const sampleWithNewData: NewDocumento = {
  noCia: 9294,
  path: 'regularly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
