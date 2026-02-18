export interface IProveedor {
  id: number;
  noCia?: number | null;
  dni?: string | null;
  nombre?: string | null;
  contacto?: string | null;
  email?: string | null;
  pathImagen?: string | null;
  telefono?: string | null;
}

export type NewProveedor = Omit<IProveedor, 'id'> & { id: null };
