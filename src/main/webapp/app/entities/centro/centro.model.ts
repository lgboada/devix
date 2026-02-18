import { ICompania } from 'app/entities/compania/compania.model';

export interface ICentro {
  id: number;
  noCia?: number | null;
  descripcion?: string | null;
  compania?: Pick<ICompania, 'id'> | null;
}

export type NewCentro = Omit<ICentro, 'id'> & { id: null };
