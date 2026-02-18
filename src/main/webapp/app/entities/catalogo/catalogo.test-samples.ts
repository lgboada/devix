import { ICatalogo, NewCatalogo } from './catalogo.model';

export const sampleWithRequiredData: ICatalogo = {
  id: 14117,
  noCia: 25404,
  descripcion1: 'even',
};

export const sampleWithPartialData: ICatalogo = {
  id: 21198,
  noCia: 26240,
  descripcion1: 'rectangular whoa compete',
  estado: 'usefully unless substantiate',
  texto1: 'however trash surprise',
  texto2: 'worth team finally',
};

export const sampleWithFullData: ICatalogo = {
  id: 31246,
  noCia: 5483,
  descripcion1: 'within',
  descripcion2: 'including',
  estado: 'venom outsource',
  orden: 3539,
  texto1: 'tinted',
  texto2: 'finally',
};

export const sampleWithNewData: NewCatalogo = {
  noCia: 30854,
  descripcion1: 'blah',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
