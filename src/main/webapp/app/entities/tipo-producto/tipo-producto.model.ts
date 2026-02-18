export interface ITipoProducto {
  id: number;
  noCia?: number | null;
  nombre?: string | null;
}

export type NewTipoProducto = Omit<ITipoProducto, 'id'> & { id: null };
