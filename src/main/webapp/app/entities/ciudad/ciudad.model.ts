import { IProvincia } from 'app/entities/provincia/provincia.model';

export interface ICiudad {
  id: number;
  noCia?: number | null;
  descripcion?: string | null;
  provincia?: Pick<IProvincia, 'id'> | null;
}

export type NewCiudad = Omit<ICiudad, 'id'> & { id: null };
