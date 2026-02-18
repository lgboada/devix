import dayjs from 'dayjs/esm';
import { ICentro } from 'app/entities/centro/centro.model';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IFactura {
  id: number;
  noCia?: number | null;
  serie?: string | null;
  noFisico?: string | null;
  fecha?: dayjs.Dayjs | null;
  subtotal?: number | null;
  impuesto?: number | null;
  impuestoCero?: number | null;
  descuento?: number | null;
  total?: number | null;
  porcentajeImpuesto?: number | null;
  cedula?: string | null;
  direccion?: string | null;
  email?: string | null;
  estado?: string | null;
  centro?: Pick<ICentro, 'id'> | null;
  cliente?: Pick<ICliente, 'id'> | null;
}

export type NewFactura = Omit<IFactura, 'id'> & { id: null };
