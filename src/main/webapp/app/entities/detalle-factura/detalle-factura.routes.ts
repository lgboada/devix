import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DetalleFacturaResolve from './route/detalle-factura-routing-resolve.service';

const detalleFacturaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/detalle-factura.component').then(m => m.DetalleFacturaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/detalle-factura-detail.component').then(m => m.DetalleFacturaDetailComponent),
    resolve: {
      detalleFactura: DetalleFacturaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/detalle-factura-update.component').then(m => m.DetalleFacturaUpdateComponent),
    resolve: {
      detalleFactura: DetalleFacturaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/detalle-factura-update.component').then(m => m.DetalleFacturaUpdateComponent),
    resolve: {
      detalleFactura: DetalleFacturaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default detalleFacturaRoute;
