import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProveedorResolve from './route/proveedor-routing-resolve.service';

const proveedorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/proveedor.component').then(m => m.ProveedorComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/proveedor-detail.component').then(m => m.ProveedorDetailComponent),
    resolve: {
      proveedor: ProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/proveedor-update.component').then(m => m.ProveedorUpdateComponent),
    resolve: {
      proveedor: ProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/proveedor-update.component').then(m => m.ProveedorUpdateComponent),
    resolve: {
      proveedor: ProveedorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default proveedorRoute;
