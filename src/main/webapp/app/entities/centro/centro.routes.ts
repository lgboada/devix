import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CentroResolve from './route/centro-routing-resolve.service';

const centroRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/centro.component').then(m => m.CentroComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/centro-detail.component').then(m => m.CentroDetailComponent),
    resolve: {
      centro: CentroResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/centro-update.component').then(m => m.CentroUpdateComponent),
    resolve: {
      centro: CentroResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/centro-update.component').then(m => m.CentroUpdateComponent),
    resolve: {
      centro: CentroResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default centroRoute;
