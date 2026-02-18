import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CiudadResolve from './route/ciudad-routing-resolve.service';

const ciudadRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ciudad.component').then(m => m.CiudadComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ciudad-detail.component').then(m => m.CiudadDetailComponent),
    resolve: {
      ciudad: CiudadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ciudad-update.component').then(m => m.CiudadUpdateComponent),
    resolve: {
      ciudad: CiudadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ciudad-update.component').then(m => m.CiudadUpdateComponent),
    resolve: {
      ciudad: CiudadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ciudadRoute;
