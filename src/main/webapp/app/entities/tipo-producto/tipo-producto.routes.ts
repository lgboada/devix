import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TipoProductoResolve from './route/tipo-producto-routing-resolve.service';

const tipoProductoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tipo-producto.component').then(m => m.TipoProductoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tipo-producto-detail.component').then(m => m.TipoProductoDetailComponent),
    resolve: {
      tipoProducto: TipoProductoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tipo-producto-update.component').then(m => m.TipoProductoUpdateComponent),
    resolve: {
      tipoProducto: TipoProductoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tipo-producto-update.component').then(m => m.TipoProductoUpdateComponent),
    resolve: {
      tipoProducto: TipoProductoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tipoProductoRoute;
