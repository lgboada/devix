import { IProveedor, NewProveedor } from './proveedor.model';

export const sampleWithRequiredData: IProveedor = {
  id: 1215,
  noCia: 25698,
  dni: 'amongst throughout',
  nombre: 'dazzling forenenst beside',
  email: '9ra@yoq.sb',
  pathImagen: 'barring unit',
  telefono: 'pleasant gosh',
};

export const sampleWithPartialData: IProveedor = {
  id: 2911,
  noCia: 11549,
  dni: 'between emerge below',
  nombre: 'stormy',
  email: 'mnk@.4zp.ake',
  pathImagen: 'boo provided',
  telefono: 'lovingly next',
};

export const sampleWithFullData: IProveedor = {
  id: 2129,
  noCia: 27948,
  dni: 'hence ravage whispered',
  nombre: 'SUV punctually last',
  contacto: 'once near',
  email: 'n%9.@k-jey.mo',
  pathImagen: 'before pace',
  telefono: 'pfft upon',
};

export const sampleWithNewData: NewProveedor = {
  noCia: 6794,
  dni: 'ha amid yowza',
  nombre: 'mechanically',
  email: 'axlv@gh811.nk',
  pathImagen: 'sonnet',
  telefono: 'athwart battle',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
