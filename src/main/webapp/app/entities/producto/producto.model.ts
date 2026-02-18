import { IModelo } from 'app/entities/modelo/modelo.model';
import { ITipoProducto } from 'app/entities/tipo-producto/tipo-producto.model';
import { IProveedor } from 'app/entities/proveedor/proveedor.model';

export interface IProducto {
  id: number;
  noCia?: number | null;
  nombre?: string | null;
  descripcion?: string | null;
  precio?: number | null;
  stock?: number | null;
  pathImagen?: string | null;
  codigo?: string | null;
  modelo?: Pick<IModelo, 'id'> | null;
  tipoProducto?: Pick<ITipoProducto, 'id'> | null;
  proveedor?: Pick<IProveedor, 'id'> | null;
}

export type NewProducto = Omit<IProducto, 'id'> & { id: null };
