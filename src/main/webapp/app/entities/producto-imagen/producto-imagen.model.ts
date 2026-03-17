export interface IProductoImagen {
  id: number;
  noCia?: number | null;
  pathImagen?: string | null;
  orden?: number | null;
  principal?: boolean | null;
  visible?: boolean | null;
  productoId?: number | null;
}

export type NewProductoImagen = Omit<IProductoImagen, 'id'> & { id: null };
