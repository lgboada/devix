import { IFactura } from 'app/entities/factura/factura.model';
import { IProducto } from 'app/entities/producto/producto.model';

export interface IDetalleFactura {
  id: number;
  noCia?: number | null;
  cantidad?: number | null;
  precioUnitario?: number | null;
  subtotal?: number | null;
  descuento?: number | null;
  impuesto?: number | null;
  total?: number | null;
  factura?: Pick<IFactura, 'id'> | null;
  producto?: Pick<IProducto, 'id'> | null;
}

export type NewDetalleFactura = Omit<IDetalleFactura, 'id'> & { id: null };
