import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ModeloResolve from './route/modelo-routing-resolve.service';

const modeloRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/modelo.component').then(m => m.ModeloComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/modelo-detail.component').then(m => m.ModeloDetailComponent),
    resolve: {
      modelo: ModeloResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/modelo-update.component').then(m => m.ModeloUpdateComponent),
    resolve: {
      modelo: ModeloResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/modelo-update.component').then(m => m.ModeloUpdateComponent),
    resolve: {
      modelo: ModeloResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default modeloRoute;
