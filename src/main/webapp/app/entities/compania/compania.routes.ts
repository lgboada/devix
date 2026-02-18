import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CompaniaResolve from './route/compania-routing-resolve.service';

const companiaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/compania.component').then(m => m.CompaniaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/compania-detail.component').then(m => m.CompaniaDetailComponent),
    resolve: {
      compania: CompaniaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/compania-update.component').then(m => m.CompaniaUpdateComponent),
    resolve: {
      compania: CompaniaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/compania-update.component').then(m => m.CompaniaUpdateComponent),
    resolve: {
      compania: CompaniaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default companiaRoute;
