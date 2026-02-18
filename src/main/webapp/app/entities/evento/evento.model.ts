import dayjs from 'dayjs/esm';
import { ITipoEvento } from 'app/entities/tipo-evento/tipo-evento.model';
import { ICentro } from 'app/entities/centro/centro.model';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IEvento {
  id: number;
  noCia?: number | null;
  descripcion?: string | null;
  fecha?: dayjs.Dayjs | null;
  estado?: string | null;
  motivoConsulta?: string | null;
  tratamiento?: string | null;
  indicaciones?: string | null;
  diagnostico1?: string | null;
  diagnostico2?: string | null;
  diagnostico3?: string | null;
  diagnostico4?: string | null;
  diagnostico5?: string | null;
  diagnostico6?: string | null;
  diagnostico7?: string | null;
  observacion?: string | null;
  tipoEvento?: Pick<ITipoEvento, 'id'> | null;
  centro?: Pick<ICentro, 'id'> | null;
  cliente?: Pick<ICliente, 'id'> | null;
}

export type NewEvento = Omit<IEvento, 'id'> & { id: null };
