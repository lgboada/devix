export interface ITipoDireccion {
  id: number;
  noCia?: number | null;
  descripcion?: string | null;
}

export type NewTipoDireccion = Omit<ITipoDireccion, 'id'> & { id: null };
