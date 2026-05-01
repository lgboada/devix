export interface ICategoriaPaciente {
  id: number;
  noCia?: number | null;
  nombre?: string | null;
}

export type NewCategoriaPaciente = Omit<ICategoriaPaciente, 'id'> & { id: null };
