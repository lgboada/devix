import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UsuarioCentroResolve from './route/usuario-centro-routing-resolve.service';

const usuarioCentroRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/usuario-centro.component').then(m => m.UsuarioCentroComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/usuario-centro-detail.component').then(m => m.UsuarioCentroDetailComponent),
    resolve: {
      usuarioCentro: UsuarioCentroResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/usuario-centro-update.component').then(m => m.UsuarioCentroUpdateComponent),
    resolve: {
      usuarioCentro: UsuarioCentroResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/usuario-centro-update.component').then(m => m.UsuarioCentroUpdateComponent),
    resolve: {
      usuarioCentro: UsuarioCentroResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default usuarioCentroRoute;
