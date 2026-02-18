import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TipoDireccionResolve from './route/tipo-direccion-routing-resolve.service';

const tipoDireccionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tipo-direccion.component').then(m => m.TipoDireccionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tipo-direccion-detail.component').then(m => m.TipoDireccionDetailComponent),
    resolve: {
      tipoDireccion: TipoDireccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tipo-direccion-update.component').then(m => m.TipoDireccionUpdateComponent),
    resolve: {
      tipoDireccion: TipoDireccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tipo-direccion-update.component').then(m => m.TipoDireccionUpdateComponent),
    resolve: {
      tipoDireccion: TipoDireccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tipoDireccionRoute;
