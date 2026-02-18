export interface ITipoCatalogo {
  id: number;
  noCia?: number | null;
  descripcion?: string | null;
  categoria?: string | null;
}

export type NewTipoCatalogo = Omit<ITipoCatalogo, 'id'> & { id: null };
