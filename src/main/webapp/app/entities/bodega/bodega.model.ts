import { ICentro } from 'app/entities/centro/centro.model';

export interface IBodega {
  id: number;
  noCia?: number | null;
  codigo?: string | null;
  nombre?: string | null;
  activa?: boolean | null;
  centro?: Pick<ICentro, 'id'> | null;
}

export type NewBodega = Omit<IBodega, 'id'> & { id: null };
