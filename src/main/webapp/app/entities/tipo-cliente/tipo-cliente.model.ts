export interface ITipoCliente {
  id: number;
  noCia?: number | null;
  descripcion?: string | null;
}

export type NewTipoCliente = Omit<ITipoCliente, 'id'> & { id: null };
