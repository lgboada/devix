import dayjs from 'dayjs/esm';

import { IEvento, NewEvento } from './evento.model';

export const sampleWithRequiredData: IEvento = {
  id: 21654,
  noCia: 11605,
  descripcion: 'politely',
  fecha: dayjs('2026-02-05T08:08'),
};

export const sampleWithPartialData: IEvento = {
  id: 3285,
  noCia: 24829,
  descripcion: 'masculinize cruelly',
  fecha: dayjs('2026-02-05T07:00'),
  estado: 'along',
  motivoConsulta: 'account whirlwind if',
  indicaciones: 'fishery absolve',
  diagnostico1: 'within wilt fooey',
  diagnostico2: 'submitter',
  diagnostico3: 'cook',
  diagnostico4: 'now wherever upon',
  diagnostico6: 'miserably after now',
  diagnostico7: 'decode dally gladly',
};

export const sampleWithFullData: IEvento = {
  id: 26545,
  noCia: 14949,
  descripcion: 'airbrush',
  fecha: dayjs('2026-02-05T09:35'),
  estado: 'absent below via',
  motivoConsulta: 'splosh only',
  tratamiento: 'packaging',
  indicaciones: 'leading now',
  diagnostico1: 'blah militate elegantly',
  diagnostico2: 'on woot floss',
  diagnostico3: 'economise ack',
  diagnostico4: 'polished',
  diagnostico5: 'duh creature',
  diagnostico6: 'experienced while orient',
  diagnostico7: 'noisily fumigate',
  observacion: 'dreamily',
};

export const sampleWithNewData: NewEvento = {
  noCia: 19822,
  descripcion: 'hmph gracefully stupendous',
  fecha: dayjs('2026-02-05T04:33'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
