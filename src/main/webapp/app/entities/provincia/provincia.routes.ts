import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProvinciaResolve from './route/provincia-routing-resolve.service';

const provinciaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/provincia.component').then(m => m.ProvinciaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/provincia-detail.component').then(m => m.ProvinciaDetailComponent),
    resolve: {
      provincia: ProvinciaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/provincia-update.component').then(m => m.ProvinciaUpdateComponent),
    resolve: {
      provincia: ProvinciaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/provincia-update.component').then(m => m.ProvinciaUpdateComponent),
    resolve: {
      provincia: ProvinciaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default provinciaRoute;
