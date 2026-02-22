import { ICentro } from 'app/entities/centro/centro.model';
import { IUser } from 'app/entities/user/user.model';

export interface IUsuarioCentro {
  id: number;
  noCia?: number | null;
  principal?: boolean | null;
  centro?: Pick<ICentro, 'id' | 'descripcion'> | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewUsuarioCentro = Omit<IUsuarioCentro, 'id'> & { id: null };
