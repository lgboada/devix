export interface ITipoEvento {
  id: number;
  noCia?: number | null;
  nombre?: string | null;
}

export type NewTipoEvento = Omit<ITipoEvento, 'id'> & { id: null };
