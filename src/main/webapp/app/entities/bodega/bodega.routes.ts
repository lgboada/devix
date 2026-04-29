import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import CentroFromParentResolve from './route/centro-parent-routing-resolve.service';
import BodegaResolve from './route/bodega-routing-resolve.service';

const bodegaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bodega-list-centro.component').then(m => m.BodegaListCentroComponent),
    resolve: {
      centro: CentroFromParentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bodega-update.component').then(m => m.BodegaUpdateComponent),
    resolve: {
      centro: CentroFromParentResolve,
      bodega: BodegaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':bodegaId/edit',
    loadComponent: () => import('./update/bodega-update.component').then(m => m.BodegaUpdateComponent),
    resolve: {
      centro: CentroFromParentResolve,
      bodega: BodegaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bodegaRoute;
