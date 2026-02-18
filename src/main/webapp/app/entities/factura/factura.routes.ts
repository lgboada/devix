import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FacturaResolve from './route/factura-routing-resolve.service';

const facturaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/factura.component').then(m => m.FacturaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/factura-detail.component').then(m => m.FacturaDetailComponent),
    resolve: {
      factura: FacturaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/factura-update.component').then(m => m.FacturaUpdateComponent),
    resolve: {
      factura: FacturaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/factura-update.component').then(m => m.FacturaUpdateComponent),
    resolve: {
      factura: FacturaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default facturaRoute;
