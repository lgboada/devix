export interface ICompania {
  id: number;
  noCia?: number | null;
  dni?: string | null;
  nombre?: string | null;
  direccion?: string | null;
  email?: string | null;
  telefono?: string | null;
  pathImage?: string | null;
  activa?: boolean | null;
}

export type NewCompania = Omit<ICompania, 'id'> & { id: null };
