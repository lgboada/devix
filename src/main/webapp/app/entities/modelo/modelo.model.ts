import { IMarca } from 'app/entities/marca/marca.model';

export interface IModelo {
  id: number;
  noCia?: number | null;
  nombre?: string | null;
  pathImagen?: string | null;
  marca?: Pick<IMarca, 'id'> | null;
}

export type NewModelo = Omit<IModelo, 'id'> & { id: null };
