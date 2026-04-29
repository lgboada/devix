import { ICentro } from 'app/entities/centro/centro.model';
import { IUser } from 'app/entities/user/user.model';
import { IBodega } from 'app/entities/bodega/bodega.model';

export interface IUsuarioCentroBodega {
  id: number;
  noCia?: number | null;
  principal?: boolean | null;
  centro?: Pick<ICentro, 'id' | 'descripcion'> | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  bodega?: Pick<IBodega, 'id' | 'codigo' | 'nombre'> | null;
}

export type NewUsuarioCentroBodega = Omit<IUsuarioCentroBodega, 'id'> & { id: null };
