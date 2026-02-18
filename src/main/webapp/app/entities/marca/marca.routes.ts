import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MarcaResolve from './route/marca-routing-resolve.service';

const marcaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/marca.component').then(m => m.MarcaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/marca-detail.component').then(m => m.MarcaDetailComponent),
    resolve: {
      marca: MarcaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/marca-update.component').then(m => m.MarcaUpdateComponent),
    resolve: {
      marca: MarcaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/marca-update.component').then(m => m.MarcaUpdateComponent),
    resolve: {
      marca: MarcaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default marcaRoute;
