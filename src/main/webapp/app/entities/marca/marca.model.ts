export interface IMarca {
  id: number;
  noCia?: number | null;
  nombre?: string | null;
  pathImagen?: string | null;
}

export type NewMarca = Omit<IMarca, 'id'> & { id: null };
