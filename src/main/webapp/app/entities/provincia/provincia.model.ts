import { IPais } from 'app/entities/pais/pais.model';

export interface IProvincia {
  id: number;
  noCia?: number | null;
  descripcion?: string | null;
  pais?: Pick<IPais, 'id'> | null;
}

export type NewProvincia = Omit<IProvincia, 'id'> & { id: null };
