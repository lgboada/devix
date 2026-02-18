export interface IPais {
  id: number;
  noCia?: number | null;
  descripcion?: string | null;
}

export type NewPais = Omit<IPais, 'id'> & { id: null };
