import dayjs from 'dayjs/esm';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { IEvento } from 'app/entities/evento/evento.model';

export interface IDocumento {
  id: number;
  noCia?: number | null;
  tipo?: string | null;
  observacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  fechaVencimiento?: dayjs.Dayjs | null;
  path?: string | null;
  cliente?: Pick<ICliente, 'id'> | null;
  evento?: Pick<IEvento, 'id'> | null;
}

export type NewDocumento = Omit<IDocumento, 'id'> & { id: null };
