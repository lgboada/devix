import { IProducto, NewProducto } from './producto.model';

export const sampleWithRequiredData: IProducto = {
  id: 26142,
  noCia: 2366,
  nombre: 'that',
  precio: 12603.72,
  stock: 21221,
  pathImagen: 'inside',
  codigo: 'skean separate mostly',
};

export const sampleWithPartialData: IProducto = {
  id: 478,
  noCia: 9891,
  nombre: 'catalog diagram rapidly',
  precio: 16696.25,
  stock: 9675,
  pathImagen: 'orderly down',
  codigo: 'likewise zowie triumphantly',
};

export const sampleWithFullData: IProducto = {
  id: 31795,
  noCia: 22400,
  nombre: 'hotfoot provided considering',
  descripcion: 'sock urban lest',
  precio: 3595.23,
  stock: 27012,
  pathImagen: 'astride fisherman',
  codigo: 'sweatshop regarding between',
};

export const sampleWithNewData: NewProducto = {
  noCia: 32263,
  nombre: 'concerning',
  precio: 25181.53,
  stock: 17688,
  pathImagen: 'sailor stable righteously',
  codigo: 'eek impish any',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
