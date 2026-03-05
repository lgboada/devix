export interface ICentro {
  id: number;
  noCia?: number | null;
  descripcion?: string | null;
}

export type NewCentro = Omit<ICentro, 'id'> & { id: null };
