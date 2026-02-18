import { ITipoCatalogo } from 'app/entities/tipo-catalogo/tipo-catalogo.model';

export interface ICatalogo {
  id: number;
  noCia?: number | null;
  descripcion1?: string | null;
  descripcion2?: string | null;
  estado?: string | null;
  orden?: number | null;
  texto1?: string | null;
  texto2?: string | null;
  tipoCatalogo?: Pick<ITipoCatalogo, 'id'> | null;
}

export type NewCatalogo = Omit<ICatalogo, 'id'> & { id: null };
