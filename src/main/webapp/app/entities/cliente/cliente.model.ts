import dayjs from 'dayjs/esm';
import { ITipoCliente } from 'app/entities/tipo-cliente/tipo-cliente.model';
import { ICiudad } from 'app/entities/ciudad/ciudad.model';

export interface ICliente {
  id: number;
  noCia?: number | null;
  dni?: string | null;
  nombres?: string | null;
  apellidos?: string | null;
  nombreComercial?: string | null;
  email?: string | null;
  telefono1?: string | null;
  telefono2?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  sexo?: string | null;
  estadoCivil?: string | null;
  tipoSangre?: string | null;
  pathImagen?: string | null;
  tipoCliente?: Pick<ITipoCliente, 'id'> | null;
  ciudad?: Pick<ICiudad, 'id'> | null;
}

export type NewCliente = Omit<ICliente, 'id'> & { id: null };
