import { ITipoDireccion } from 'app/entities/tipo-direccion/tipo-direccion.model';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IDireccion {
  id: number;
  noCia?: number | null;
  descripcion?: string | null;
  pais?: string | null;
  provincia?: string | null;
  tipoDireccion?: Pick<ITipoDireccion, 'id'> | null;
  cliente?: Pick<ICliente, 'id'> | null;
}

export type NewDireccion = Omit<IDireccion, 'id'> & { id: null };
